package org.example.qlthuvien.dto.badge;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBadgeResponse {
    private Long badgeId;
    private String badgeName;
    private String iconUrl;
    private String description;
    private LocalDateTime grantedAt;
}

