package org.example.qlthuvien.dto.badge;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class BadgeWithUserResponse {
    private BadgeResponse badge;
    private List<BadgeOwner> users;
}

