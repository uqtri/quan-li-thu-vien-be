package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.qlthuvien.entity.Badge;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.entity.UserBadgeId;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @EmbeddedId
    private UserBadgeId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("badgeId")
    @JoinColumn(name = "badge_id")
    private Badge badge;

    private LocalDateTime grantedAt;
}
