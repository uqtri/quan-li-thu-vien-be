package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.catalog.*;
import org.example.qlthuvien.entity.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CatalogMapper {
    Catalog toEntity(CreateCatalogRequest dto);
    Catalog toEntity(UpdateCatalogRequest dto);
    CatalogResponse toResponse(Catalog catalog);
    Catalog updateEntity(@MappingTarget Catalog target, Catalog catalog);
}

