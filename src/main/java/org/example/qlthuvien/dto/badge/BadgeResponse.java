package org.example.qlthuvien.dto.badge;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.BADGE_CATEGORY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponse {
    private Long id;

    private String name;
    private String description;
    private BADGE_CATEGORY category;
    private Integer xpAwarded;
    private String icon_url;

    private Integer xpRequired;
    private Integer reviewsRequired;
    private Integer borrowedBooksRequired;


}
