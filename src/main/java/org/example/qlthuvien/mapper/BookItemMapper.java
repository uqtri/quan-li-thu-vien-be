package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.bookitem.BookItemResponse;
import org.example.qlthuvien.dto.bookitem.CreateBookItemRequest;
import org.example.qlthuvien.dto.bookitem.UpdateBookItemRequest;
import org.example.qlthuvien.entity.BookItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface BookItemMapper {

    BookItem toEntity (CreateBookItemRequest dto);
    BookItem toEntity (UpdateBookItemRequest dto);
    BookItemResponse toResponse (BookItem bookItem);

    BookItem updateEntity(@MappingTarget BookItem oldBookItem, BookItem newBookItem);
}
