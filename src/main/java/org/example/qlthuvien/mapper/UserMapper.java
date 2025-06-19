package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.user.CreateUserRequest;
import org.example.qlthuvien.dto.user.UpdateUserRequest;
import org.example.qlthuvien.dto.user.UserResponse;
import org.example.qlthuvien.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target="image", ignore=true)
    User toEntity(CreateUserRequest dto);
    @Mapping(target="image", ignore=true)
    User toEntity(UpdateUserRequest dto);

    User updateEntity(@MappingTarget User target, User source);
}
