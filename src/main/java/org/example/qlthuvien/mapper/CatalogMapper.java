package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.catalog.*;
import org.example.qlthuvien.entity.Catalog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    Catalog toEntity(CreateCatalogRequest dto);
    CatalogResponse toResponse(Catalog catalog);
}
