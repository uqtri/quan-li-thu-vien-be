package org.example.qlthuvien.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.dto.catalog.CatalogResponse;
import org.example.qlthuvien.entity.BookItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String author;
    private String title;
    private String description;
    private String image;
    private List<BookItem> bookItems;
    private CatalogResponse catalog;
}



