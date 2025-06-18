package org.example.qlthuvien.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserBadgeId implements Serializable {
    private Long userId;
    private Long badgeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBadgeId that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(badgeId, that.badgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, badgeId);
    }
}
