package org.example.qlthuvien.dto.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
}
