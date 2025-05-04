package au.com.treeshake.phantombust.service.json;

import au.com.treeshake.phantombust.dto.IgExportFollower;
import au.com.treeshake.phantombust.entity.IgFollower;
import au.com.treeshake.phantombust.entity.IgProfile;
import au.com.treeshake.phantombust.entity.IgUser;
import au.com.treeshake.phantombust.repository.IgFollowerRepository;
import au.com.treeshake.phantombust.repository.IgProfileRepository;
import au.com.treeshake.phantombust.repository.IgUserRepository;
import au.com.treeshake.phantombust.service.csv.IgFollowingCsvProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class JsonImportRunner {

    private final IgFollowerImportService importService;
    private final IgUserRepository igUserRepository;
    private final IgProfileRepository igProfileRepository;
    private final IgFollowerRepository igFollowerRepository;

    public JsonImportRunner(IgFollowerImportService importService, IgUserRepository igUserRepository, IgProfileRepository igProfileRepository, IgFollowerRepository igFollowerRepository) {
        this.importService = importService;
        this.igUserRepository = igUserRepository;
        this.igProfileRepository = igProfileRepository;
        this.igFollowerRepository = igFollowerRepository;
    }

    public void runImport() throws Exception {

        final String igProfileSource = "vthelabel_";

        URL resource = Objects.requireNonNull(IgFollowingCsvProcessingService.class.getResource("/data/ig-follower/followers_5.json"));
        File file = new File(resource.getFile());
        List<IgExportFollower> followers = importService.importFollowers(file);
        followers.stream()
                .flatMap(follower -> follower.getStringListData().stream())
                .map(IgExportFollower.StringListData::getValue)
                .forEach(value -> {
                    IgUser igUser = findOrCreateUser(value);
                    Optional<IgProfile> igProfile = igProfileRepository.findOneByProfileName(value);
                    createOrUpdateFollower(igProfileSource, igUser, igProfile);
                });
    }

    private IgUser findOrCreateUser(String username) {
        Optional<IgUser> existingUser = igUserRepository.findOneByUsername(username);
        return existingUser.orElseGet(() -> {
            log.info("Created new user: {}", username);
            IgUser newUser = new IgUser();
            newUser.setUsername(username);
            return igUserRepository.save(newUser);
        });
    }

    private void createOrUpdateFollower(String sourceProfile, IgUser igUser, Optional<IgProfile> igProfile) {
        Optional<IgFollower> existingFollower = igFollowerRepository.findBySourceProfileAndIgUser(sourceProfile, igUser);

        IgFollower follower;
        if (existingFollower.isPresent()) {
            log.info("Follower already exists: {}", igUser.getUsername());
            follower = existingFollower.get();
        } else {
            log.info("Creating new follower for source {}, user: {}", sourceProfile, igUser.getUsername());
            follower = new IgFollower();
            follower.setSourceProfile(sourceProfile);
            follower.setIgUser(igUser);
        }

        if (igProfile.isPresent()) {
            log.info("Found profile for follower: {}", igUser.getUsername());
            IgProfile profile = igProfile.get();
            follower.setIgProfile(profile);
        }
        igFollowerRepository.save(follower);
    }
}