package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.borrowedbook.BorrowedBookResponse;
import org.example.qlthuvien.dto.borrowedbook.CreateBorrowedBookRequest;
import org.example.qlthuvien.dto.borrowedbook.UpdateBorrowedBookRequest;
import org.example.qlthuvien.entity.BorrowedBook;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BorrowedBookMapper {

    // Cần custom method ngoài MapStruct để convert từ ID sang entity

    BorrowedBookResponse toResponse(BorrowedBook entity);

    BorrowedBook toEntity(CreateBorrowedBookRequest dto);

    BorrowedBook toEntity(UpdateBorrowedBookRequest dto);

    BorrowedBook updateEntity(@MappingTarget BorrowedBook target, BorrowedBook source);
}
