package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.message.CreateMessage;
import org.example.qlthuvien.dto.message.MessageResponse;
import org.example.qlthuvien.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    ChatMessage toEntity(CreateMessage dto);

    MessageResponse toResponse(ChatMessage chatMessage);
}

