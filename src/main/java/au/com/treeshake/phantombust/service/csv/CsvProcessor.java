package au.com.treeshake.phantombust.service.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import au.com.treeshake.phantombust.exception.IgQueryInvalidRecordException;
import au.com.treeshake.phantombust.model.CsvDataEntry;
import au.com.treeshake.phantombust.model.CsvMetadata;
import au.com.treeshake.phantombust.model.DataEntryStatistics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Service class.
 */
@Slf4j
public record CsvProcessor<D>(CsvDataEntry dataEntry, CsvMapper csvMapper, Class<D> incomingType) {

    public <E> void processFile(File file, Converter<D, E> converter, JpaRepository<E, Long> repository) throws IOException {
        processFile(file, converter, repository, new HashMap<>());
    }

    public <E> void processFile(
        File file, Converter<D, E> converter, JpaRepository<E, Long> repository,
        Map<String, String> cachedQueries) throws IOException {

        LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");
        while (lineIterator.hasNext()) {
            try {
                dataEntry.setEntry(lineIterator.nextLine());
                if (dataEntry.isHeader()) {
                    continue;
                }
                CsvMetadata metadata = mapMetadata(dataEntry);
                if (StringUtils.isBlank(metadata.getQuery())) {
                    log.debug("Row {} skipped - Could not locate query metadata", dataEntry.getLineNumber());
                    dataEntry.getCurrentLine().markAsSkipped();
                    continue;
                }
                if (StringUtils.isNotBlank(metadata.getError())) {
                    log.debug("Row {} with query = {} skipped - Error = {}", dataEntry.getLineNumber(), metadata.getQuery(), metadata.getError());
                    dataEntry.getCurrentLine().markAsError(new IgQueryInvalidRecordException(metadata.getQuery(), metadata.getError()));
                    continue;
                }
                // Already processed.
                if (cachedQueries.get(metadata.getQuery()) != null) {
                    log.debug("Row {} with query = {} skipped - already processed.", dataEntry.getLineNumber(), metadata.getQuery());
                    dataEntry.getCurrentLine().markAsSkipped();
                    continue;
                }
                E entity = Objects.requireNonNull(converter.convert(mapLine(dataEntry)));
                repository.save(entity);
                log.debug("Row {} processed", dataEntry.getLineNumber());
            } catch (RuntimeException e) {
                log.warn("Could not process record at row = {} data = '{}'. Error = {}", dataEntry.getLineNumber(),
                    dataEntry.getCurrentLine().getData(),
                    e.getMessage()
                );
                dataEntry.getCurrentLine().markAsError(e);
            }
        }
        DataEntryStatistics statistics = dataEntry.getStatistics();
        log.info("Finished. {} rows processed. {} rows saved ({}%). {} errors ({}%). {} rows skipped ({}%).",
            statistics.getTotalProcessed(),
            statistics.getSuccessCount(),
            statistics.getSuccessPercentage(),
            statistics.getErrorCount(),
            statistics.getErrorPercentage(),
            statistics.getSkippedCount(),
            statistics.getSkippedPercentage()
        );
    }

    private D mapLine(CsvDataEntry dataEntry) throws IOException {
        MappingIterator<D> mappingIterator = csvMapper
            .readerFor(incomingType)
            .with(dataEntry.getCsvSchema())
            .readValues(dataEntry.getCurrentLine().getData());
        return mappingIterator.next();
    }

    private CsvMetadata mapMetadata(CsvDataEntry dataEntry) throws IOException {
        MappingIterator<CsvMetadata> mappingIterator = csvMapper
            .readerFor(CsvMetadata.class)
            .with(dataEntry.getCsvSchema())
            .readValues(dataEntry.getCurrentLine().getData());
        return mappingIterator.next();
    }
}
