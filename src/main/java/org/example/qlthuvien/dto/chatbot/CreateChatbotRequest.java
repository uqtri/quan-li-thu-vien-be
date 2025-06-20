package org.example.qlthuvien.dto.chatbot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatbotRequest {
    private Long user_id;
    private String message;
    private  boolean is_bot;
}
