package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.message.CreateMessage;
import org.example.qlthuvien.dto.message.MessageResponse;
import org.example.qlthuvien.entity.ChatMessage;
import org.example.qlthuvien.mapper.MessageMapper;
import org.example.qlthuvien.repository.ChatMessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat.sendMessage") // client gửi vào /app/chat.sendMessage
    @SendTo("/topic/public")             // broadcast tới tất cả client đang sub /topic/public
    public MessageResponse sendMessage(CreateMessage message) {
        ChatMessage messageEntity = messageMapper.toEntity(message);
        ChatMessage newMessage = messageRepository.save(messageEntity);
        MessageResponse res = messageMapper.toResponse(newMessage);
        return  res;
    }

    @GetMapping("/api/chat/messages")
    public List<ChatMessage> getMessages() {
        return messageRepository.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));
    }

}
