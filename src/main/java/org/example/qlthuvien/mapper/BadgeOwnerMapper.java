package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.badge.BadgeOwner;
import org.example.qlthuvien.entity.UserBadge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BadgeOwnerMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.name", target = "fullname") // thêm dòng này để lấy fullname
    @Mapping(source = "grantedAt", target = "grantedAt")
    BadgeOwner toResponse(UserBadge userBadge);
}
