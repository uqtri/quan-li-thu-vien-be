package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private BADGE_CATEGORY category;
    private Integer xpAwarded;
    private String icon_url;

    private Integer xpRequired;
    private Integer reviewsRequired;
    private Integer borrowedBooksRequired;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    public void onCreate() {
        isDeleted = false;
    }

}
