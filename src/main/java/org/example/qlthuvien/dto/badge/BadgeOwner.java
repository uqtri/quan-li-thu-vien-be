package org.example.qlthuvien.dto.badge;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BadgeOwner {
    private Long userId;
    private String email;
    private String fullname;
    private LocalDateTime grantedAt;
}
