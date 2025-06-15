package org.example.qlthuvien.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.config.CloudinaryConfig;
import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.mapper.BookMapper;
import org.example.qlthuvien.repository.BookRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Cloudinary cloudinary;

//    BookController(BookRepository bookRepository, BookMapper bookMapper) {
//        this.bookRepository = bookRepository;
//        this.bookMapper = bookMapper;
//
//    }
    @GetMapping
    List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toResponse).toList();
    }
    @PostMapping
    BookResponse createBook(@ModelAttribute CreateBookRequest data) {

        Book book = bookMapper.toEntity(data);

        try{
            MultipartFile image = data.getImage();

            System.out.println(image);
            System.out.println(image.getOriginalFilename());
            byte[] byteArray = image.getBytes();

            String base64String = "data:" + image.getContentType() + ";base64," + Base64.getEncoder().encodeToString(data.getImage().getBytes());

            Map uploadResult = cloudinary.uploader().upload(base64String, ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            ));

            String secure_url = uploadResult.get("secure_url").toString();
            System.out.println(secure_url);
            book.setImage(secure_url);
        }
        catch(Exception e){
            System.out.println(e);
        }
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }
}
