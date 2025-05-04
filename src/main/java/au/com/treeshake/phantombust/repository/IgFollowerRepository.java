package au.com.treeshake.phantombust.repository;

import au.com.treeshake.phantombust.entity.IgFollower;
import au.com.treeshake.phantombust.entity.IgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IgFollowerRepository extends JpaRepository<IgFollower, Long> {

    Optional<IgFollower> findBySourceProfileAndIgUser(String sourceProfile, IgUser igUser);

    @Query("SELECT f FROM ig_follower f WHERE f.igProfile IS NULL")
    List<IgFollower> findFollowersWithoutProfileInformation();
}
