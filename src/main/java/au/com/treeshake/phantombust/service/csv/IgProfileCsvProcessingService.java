package au.com.treeshake.phantombust.service.csv;

import au.com.treeshake.phantombust.config.PhantomBusterConfigProps;
import au.com.treeshake.phantombust.dto.IgProfileDto;
import au.com.treeshake.phantombust.entity.IgProfile;
import au.com.treeshake.phantombust.model.ProcessingConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Service class.
 */
@Service
public record IgProfileCsvProcessingService(PhantomBusterConfigProps configProps,
                                            ProcessingConfig<IgProfileDto, IgProfile> processing) {

    public void importFile() throws IOException {
//        File file = ResourceUtils.getFile("file:/data/instagram-profile-scraper-1-2022-fixed.csv");
        URL resource = Objects.requireNonNull(IgFollowingCsvProcessingService.class.getResource("/data/ig-profile/instagram-profile-scraper-3-2022-fixed.csv"));
        File file = new File(resource.getFile());
        processing.csvProcessor().processFile(file, processing.converter(), processing.repository());
//        @NotEmpty List<Resource> resources = configProps.getDataImport().getFollowingCollectors();
    }

}