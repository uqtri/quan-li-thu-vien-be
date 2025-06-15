package org.example.qlthuvien.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {
    private String author;
    private String title;
    private String description;
    private String catalog_id;
    private MultipartFile image;
}


