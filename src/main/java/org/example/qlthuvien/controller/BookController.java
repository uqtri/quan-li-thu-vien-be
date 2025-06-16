package org.example.qlthuvien.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.config.CloudinaryConfig;
import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.dto.book.UpdateBookRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.Catalog;
import org.example.qlthuvien.mapper.BookMapper;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.CatalogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Cloudinary cloudinary;
    private final EntityManager entityManager;
    private final CatalogRepository catalogRepository;

    @GetMapping
    Page<BookResponse> getAllBooks(@RequestParam(required = false) String title, Pageable pageable) {

        return bookRepository.searchBooks(title, pageable).map(bookMapper::toResponse);
    }
    @PostMapping
    BookResponse createBook(@ModelAttribute CreateBookRequest data) {

        Book book = bookMapper.toEntity(data);

        Long catalog_id = data.getCatalog_id();
        Catalog catalog = entityManager.find(Catalog.class, catalog_id);

        Catalog catalog1 = catalogRepository.findById(catalog_id).orElse(null);


        book.setCatalog(catalog);

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
    @PutMapping("/{id}")
    BookResponse updateBook(@PathVariable Long id, @ModelAttribute UpdateBookRequest data) {

        Book book = bookMapper.toEntity(data);
        Book exsitedBook = bookRepository.findById(id).orElse(null);

        System.out.println(data);
        if (exsitedBook == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        if(data.getImage() != null) {
            try {
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
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi đọc file upload", e);
            }
        }
        exsitedBook = bookMapper.updateEntity(exsitedBook, book);
        return bookMapper.toResponse(bookRepository.save(exsitedBook));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        try {
            bookRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }
}
