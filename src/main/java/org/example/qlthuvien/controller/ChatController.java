package org.example.qlthuvien.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.message.CreateMessage;
import org.example.qlthuvien.dto.message.MessageResponse;
import org.example.qlthuvien.entity.ChatMessage;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.MessageMapper;
import org.example.qlthuvien.repository.ChatMessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final EntityManager entityManager;

    @MessageMapping("/chat.sendMessage") // client gửi vào /app/chat.sendMessage
    @SendTo("/topic/public")
    // broadcast tới tất cả client đang sub /topic/public
    public MessageResponse sendMessage(CreateMessage message) {
        User user = entityManager.find(User.class, message.getSender_id());

        ChatMessage messageEntity = new ChatMessage();
        messageEntity.setSenderId(user.getId());
        messageEntity.setImage(user.getImage());
        messageEntity.setSenderName(user.getName());
        messageEntity.setContent(message.getContent());
        ChatMessage newMessage = messageRepository.save(messageEntity);
        MessageResponse res = messageMapper.toResponse(newMessage);
        return res;
    }

    @GetMapping("/api/chat/messages")
    public List<ChatMessage> getMessages() {
        return messageRepository.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));
    }

    @PutMapping("/api/chat/messages/{id}")
    public ResponseEntity<MessageResponse> updateMessage(
            @PathVariable Long id,
            @RequestBody CreateMessage updatedMessage
    ) {
        Optional<ChatMessage> existingMessageOpt = messageRepository.findById(id);

        if (existingMessageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ChatMessage existingMessage = existingMessageOpt.get();
        existingMessage.setContent(updatedMessage.getContent()); // assuming only content is updated
        ChatMessage updatedMessageEntity = messageRepository.save(existingMessage);
        MessageResponse res = messageMapper.toResponse(updatedMessageEntity);

        return ResponseEntity.ok(res);
    }

    // Delete message by id
    @DeleteMapping("/api/chat/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        Optional<ChatMessage> existingMessageOpt = messageRepository.findById(id);

        if (existingMessageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        messageRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content indicates successful deletion
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
