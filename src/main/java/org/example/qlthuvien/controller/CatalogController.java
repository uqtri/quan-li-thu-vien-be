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
@RequiredArgsConstructor

public class CatalogController {
    private final CatalogRepository catalogRepository;
    private final CatalogMapper catalogMapper;

    @GetMapping
    public List<CatalogResponse> getAllCatalogs() {
        return catalogRepository.findAll().stream().map(catalogMapper::toResponse).toList();
    }

    @PostMapping
    public CatalogResponse createCatalog (@RequestBody CreateCatalogRequest data) {
        Catalog catalogEntity = catalogMapper.toEntity(data);
        Catalog savedCatalog = catalogRepository.save(catalogEntity);
        return catalogMapper.toResponse(savedCatalog);
    }
    @PutMapping("/{id}")
    public CatalogResponse updateCatalog (@PathVariable long id, @RequestBody UpdateCatalogRequest data) {

        Catalog existingCatalog = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalog not found with ID: " + id));
        existingCatalog = catalogMapper.updateEntity(existingCatalog, catalogMapper.toEntity(data));
        System.out.println(existingCatalog);
        Catalog updatedCatalog = catalogRepository.save(existingCatalog);
        return catalogMapper.toResponse(updatedCatalog);
    }
    @DeleteMapping("/{id}")
    public  CatalogResponse deleteCatalog (@PathVariable long id) {
        catalogRepository.deleteById(id);
        return catalogMapper.toResponse(null);
    }
}
