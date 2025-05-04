package au.com.treeshake.phantombust.service.export;

import au.com.treeshake.phantombust.entity.IgFollower;
import au.com.treeshake.phantombust.repository.IgFollowerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class IgFollowerCsvExportService {

    private final IgFollowerRepository followerRepository;

    public IgFollowerCsvExportService(IgFollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public void export() {
        List<IgFollower> followersWithoutProfile = followerRepository.findFollowersWithoutProfileInformation();
        Path filePath = Paths.get("data", "exports", "followers_without_profile.csv");

        try {
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());

            // Create and write to file
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.append("username\n"); // Header
                for (IgFollower follower : followersWithoutProfile) {
                    writer.append("https://www.instagram.com/" + follower.getIgUser().getUsername()).append("\n");
                }
                log.info("Export completed: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Error while exporting data: {}", e.getMessage());
        }
    }
}
