package org.example.qlthuvien.dto.bookitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.Book;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookItemResponse {

    private  Long id;

    private STATUS status;

    private Date created_at;

    private Book book;
}

