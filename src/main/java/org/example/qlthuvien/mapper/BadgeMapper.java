package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.badge.BadgeResponse;
import org.example.qlthuvien.dto.badge.CreateBadgeRequest;
import org.example.qlthuvien.dto.badge.UpdateBadgeRequest;
import org.example.qlthuvien.entity.Badge;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BadgeMapper {

    Badge toEntity(CreateBadgeRequest dto);

    Badge toEntity(UpdateBadgeRequest dto);

    BadgeResponse toResponse(Badge badge);

    Badge updateEntity(@MappingTarget Badge oldBadge, Badge newBadge);
}
