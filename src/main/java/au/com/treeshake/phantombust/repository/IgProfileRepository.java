package au.com.treeshake.phantombust.repository;

import au.com.treeshake.phantombust.entity.IgProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Repository interface.
 */
@Repository
public interface IgProfileRepository extends JpaRepository<IgProfile, Long> {
    Optional<IgProfile> findOneByInstagramID(BigInteger instagramId);
    
    Optional<IgProfile> findOneByProfileName(String username);
}
