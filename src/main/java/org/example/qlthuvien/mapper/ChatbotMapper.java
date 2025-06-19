package org.example.qlthuvien.mapper;


import org.example.qlthuvien.dto.chatbot.ChatbotResponse;
import org.example.qlthuvien.dto.chatbot.CreateChatbotRequest;
import org.example.qlthuvien.entity.Chatbot;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface ChatbotMapper {

    ChatbotResponse toResponse (Chatbot chatbot);

    Chatbot toEntity(CreateChatbotRequest createChatbotRequest);

}
