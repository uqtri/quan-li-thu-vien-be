package org.example.qlthuvien.mapper;


import org.example.qlthuvien.dto.user.UserResponse;
import org.example.qlthuvien.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
