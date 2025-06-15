package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.catalog.CatalogResponse;
import org.example.qlthuvien.dto.catalog.CreateCatalogRequest;
import org.example.qlthuvien.dto.catalog.UpdateCatalogRequest;
import org.example.qlthuvien.entity.Catalog;
import org.example.qlthuvien.mapper.CatalogMapper;
import org.example.qlthuvien.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {
    private final CatalogRepository catalogRepository;
    private final CatalogMapper catalogMapper;
    @Autowired
    public CatalogController(CatalogRepository catalogRepository, CatalogMapper catalogMapper) {
        this.catalogRepository = catalogRepository;
        this.catalogMapper = catalogMapper;
    }
    @GetMapping
    public List<CatalogResponse> getAllCatalogs() {
        return catalogRepository.findAll().stream().map(catalogMapper::toResponse).toList();
    }
    @PostMapping
    public CatalogResponse createCatalog (@RequestBody CreateCatalogRequest data) {

        System.out.print(data);
        Catalog catalogEntity = catalogMapper.toEntity(data);
        System.out.print(catalogEntity);

        System.out.print(catalogEntity);

        Catalog savedCatalog = catalogRepository.save(catalogEntity);

        return catalogMapper.toResponse(savedCatalog);
    }
//    @PutMapping
//    public CatalogResponse updateCatalog (@RequestBody UpdateCatalogRequest data) {
//
//
//    }
}
