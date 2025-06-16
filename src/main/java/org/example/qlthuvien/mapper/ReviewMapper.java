package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.review.ReviewResponse;
import org.example.qlthuvien.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ReviewMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(source = "book", target = "book")
    ReviewResponse toResponse(Review review);

}
