package au.com.treeshake.phantombust;

import au.com.treeshake.phantombust.service.export.IgFollowerCsvExportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main class.
 */
@SpringBootApplication
public class PhantomBustApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(PhantomBustApplication.class, args);
//        IgFollowingCsvProcessingService bean = ctx.getBean(IgFollowingCsvProcessingService.class);
//        bean.importFile();

//        IgUserCsvProcessingService bean = ctx.getBean(IgUserCsvProcessingService.class);
//        bean.importFile();

        // Import profiles
//        IgProfileCsvProcessingService bean = ctx.getBean(IgProfileCsvProcessingService.class);
//        bean.importFile();

        // Fix profiles (too many lines on the profile)
//        IgProfileLineFormatService formatProfile = ctx.getBean(IgProfileLineFormatService.class);
//        formatProfile.importFile();

//        JsonImportRunner runner = ctx.getBean(JsonImportRunner.class);
//        runner.runImport();

        IgFollowerCsvExportService exporter = ctx.getBean(IgFollowerCsvExportService.class);
        exporter.export();
    }
}
