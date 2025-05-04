package au.com.treeshake.phantombust.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity class.
 */
@Entity(name = "ig_follower")
@Table(
    name = "ig_follower",
    uniqueConstraints = @UniqueConstraint(columnNames = {"source_profile", "ig_user_id"})
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class IgFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_profile", nullable = false)
    private String sourceProfile;

    @OneToOne
    @JoinColumn(name = "ig_user_id", referencedColumnName = "id", nullable = false)
    private IgUser igUser;

    @OneToOne
    @JoinColumn(name = "ig_profile_id", referencedColumnName = "id")
    private IgProfile igProfile;
}
