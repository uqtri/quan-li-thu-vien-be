package org.example.qlthuvien.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.dto.book.UpdateBookRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.Catalog;
import org.example.qlthuvien.helper.MultipartInputStreamFileResource;
import org.example.qlthuvien.mapper.BookMapper;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.CatalogRepository;
import org.example.qlthuvien.services.PythonApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Cloudinary cloudinary;
    private final EntityManager entityManager;
    private final CatalogRepository catalogRepository;
    private final PythonApiService pythonApiService;
//
//    @Value("${python.backend_url}")
//    private String pythonUrl;

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
        }
        catch(Exception e){
            System.out.println(e);
        }
        // xu li tao index

        Book savedBook = bookRepository.save(book);

        pythonApiService.sendImageAddIndex(data.getImage(), savedBook.getId());

        return bookMapper.toResponse(savedBook);
    }
    @GetMapping("/{id}")
    BookResponse getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        return bookMapper.toResponse(book);
    }
    @PutMapping("/{id}")
    BookResponse updateBook(@PathVariable Long id, @ModelAttribute UpdateBookRequest data) {

        Book book = bookMapper.toEntity(data);
        Book exsitedBook = bookRepository.findById(id).orElse(null);

        if (exsitedBook == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        if(data.getCatalog_id() != null) {

            Catalog catalog = entityManager.find(Catalog.class, data.getCatalog_id());
            System.out.println(catalog);
            if(catalog != null) {
                exsitedBook.setCatalog(catalog);
                System.out.println(exsitedBook);
            }
        }
        if(data.getImage() != null && !data.getImage().isEmpty()) {
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

//        System.out.println(exsitedBook);
        return bookMapper.toResponse(bookRepository.save(exsitedBook));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        try {
            bookRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");

            pythonApiService.deleteIndex(id);

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }
    @PostMapping("/similar")
    List<BookResponse> getSimilarBooks(@ModelAttribute CreateBookRequest data) {

        if(data.getImage() != null && !data.getImage().isEmpty()) {
            try {
//                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//                body.add("image", new MultipartInputStreamFileResource(data.getImage().getInputStream(), data.getImage().getOriginalFilename()));

                List<Integer> result = pythonApiService.sendImageSearchSimilar(data.getImage());
                System.out.println(result);
                System.out.println("HERE");
                List<BookResponse> books = result.stream()
                        .map(id -> bookMapper.toResponse(bookRepository.findById(Long.valueOf(id)).orElse(null)))
                        .toList();
                return books;
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        return new ArrayList<BookResponse>();
    }

}
