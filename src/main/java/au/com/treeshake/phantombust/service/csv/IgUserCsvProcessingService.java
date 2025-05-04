package au.com.treeshake.phantombust.service.csv;

import au.com.treeshake.phantombust.dto.IgUserDto;
import au.com.treeshake.phantombust.repository.IgUserRepository;
import au.com.treeshake.phantombust.typeconverter.IgUserDtoToEntityConverter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * Service class.
 */
@Service
public record IgUserCsvProcessingService(IgUserRepository repository,
                                         IgUserDtoToEntityConverter converter,
                                         CsvProcessor<IgUserDto> csvProcessor) {

    public void importFile() throws IOException {
        URL resource = Objects.requireNonNull(IgFollowingCsvProcessingService.class.getResource("/data/ig-user/instagram-follower-collector-2022-parsed.csv"));
        File file = new File(resource.getFile());
        csvProcessor.processFile(file, converter, repository);
    }
}
