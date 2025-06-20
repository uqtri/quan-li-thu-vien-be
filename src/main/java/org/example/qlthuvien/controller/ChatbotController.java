package org.example.qlthuvien.controller;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.chatbot.ChatbotResponse;
import org.example.qlthuvien.dto.chatbot.CreateChatbotRequest;
import org.example.qlthuvien.entity.Chatbot;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.ChatbotMapper;
import org.example.qlthuvien.repository.ChatbotRepository;
import org.example.qlthuvien.services.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotRepository chatbotRepository;
    private final ChatbotMapper chatbotMapper;
    private final OpenAIService openAIService;
    private final EntityManager entityManager;
    @GetMapping("/user/{id}")
    List<ChatbotResponse> getMessages(@PathVariable Long id) {
//        System.out.println(id);
        return chatbotRepository.findChatbotByUser_Id(id).stream().map(chatbotMapper::toResponse).toList();
    }
    @PostMapping()
    ResponseEntity<Map<String, String>> createMessage(@RequestBody CreateChatbotRequest data) {

        try {
            User user = entityManager.find(User.class, data.getUser_id());

            String response = openAIService.sendMessage(data.getMessage());

            Chatbot chatbotUser = chatbotMapper.toEntity(data);
            Chatbot chatbotResponse = new Chatbot();
            chatbotUser.setUser(user);
            chatbotUser.set_bot(false);
            // save user message
            chatbotRepository.save(chatbotUser);


            // save bot message

            chatbotResponse.setUser(user);
            chatbotResponse.set_bot(true);
            chatbotResponse.setMessage(response);
//            System.out.println(chatbotResponse);
            chatbotRepository.save(chatbotResponse);
            return ResponseEntity.ok().body(Map.of("message", response));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }

    }
}
