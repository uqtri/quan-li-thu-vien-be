package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.notification.NotificationResponse;
import org.example.qlthuvien.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "user", target = "user")

    NotificationResponse toResponse(Notification notification);
}
