package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.dto.book.UpdateBookRequest;
import org.example.qlthuvien.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    @Mapping(target="image", ignore=true)
    Book toEntity(CreateBookRequest dto);

    @Mapping(target="image", ignore = true)
    Book toEntity (UpdateBookRequest dto);

    BookResponse toResponse (Book book);

    Book updateEntity(@MappingTarget Book target, Book newBook);
}
