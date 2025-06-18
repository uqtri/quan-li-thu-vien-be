package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.badge.BadgeResponse;
import org.example.qlthuvien.dto.badge.UserBadgeResponse;
import org.example.qlthuvien.entity.UserBadge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserBadgeMapper {
    @Mapping(source = "badge.id", target = "badgeId")
    @Mapping(source = "badge.name", target = "badgeName")
    @Mapping(source = "badge.icon_url", target = "iconUrl")
    @Mapping(source = "badge.description", target = "description")
    UserBadgeResponse toResponse(UserBadge userBadge);
}

