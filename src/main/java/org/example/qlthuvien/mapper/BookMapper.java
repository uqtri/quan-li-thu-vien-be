package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target="image", ignore=true)
    Book toEntity(CreateBookRequest dto);
    BookResponse toResponse (Book book);
}
