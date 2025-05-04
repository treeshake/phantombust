package au.com.treeshake.phantombust.service.csv;

import au.com.treeshake.phantombust.dto.IgUserDto;
import au.com.treeshake.phantombust.repository.IgUserRepository;
import au.com.treeshake.phantombust.typeconverter.IgUserDtoToEntityConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Service class.
 */
@Slf4j
@Service
public record IgProfileLineFormatService(IgUserRepository repository,
                                         IgUserDtoToEntityConverter converter,
                                         CsvProcessor<IgUserDto> csvProcessor) {

    public void importFile() throws IOException {
        URL resource = Objects.requireNonNull(IgFollowingCsvProcessingService.class.getResource("/data/ig-profile/instagram-profile-scraper-3-2022.csv"));
        File file = new File(resource.getFile());
        sanitiseFile(file);
    }

    /**
     * This method helps to sanitise the given data file.
     * If the user profiles span through multiple files, this method combine them into
     * a single file. Although the resulting file will contain those profiles with
     * missing new line characters.
     *
     * @param file The dirty file
     * @return The sanitised file
     * @throws IOException If file is not found or something else bad happens
     */
    public File sanitiseFile(File file) throws IOException {

        File newFile = new File(file.getParent(), file.getName().replaceFirst("(\\.[^.]*)?$", "-fixed$1"));
        FileWriter fileWriter = new FileWriter(newFile);
        
        log.info("Created file {} for sanitising", newFile.getCanonicalFile());

        LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");

        String headerRow = lineIterator.next();
        int columnCount = (int) (headerRow.chars().filter(num -> num == ',').count() + 1);

        StringBuilder currentProfile;

        fileWriter.write(headerRow);
        fileWriter.write("\n");

        int i = 0;
        while (lineIterator.hasNext()) {

            currentProfile = new StringBuilder(lineIterator.next());
            log.info("Looking at row: {}", i);
            int columnsInRow = (int) (currentProfile.chars().filter(num -> num == ',').count() + 1);

            while (columnsInRow < columnCount) {
                // Insert space before appending next line
                currentProfile.append(" ");
                currentProfile.append(lineIterator.next());
                columnsInRow = (int) (currentProfile.chars().filter(num -> num == ',').count() + 1);
            }

            fileWriter.write(currentProfile.toString());
            fileWriter.write("\n");
            i++;
        }

        fileWriter.flush();
        
        log.info("Finished processing file {}", newFile.getCanonicalFile());
        return newFile;
    }
}
