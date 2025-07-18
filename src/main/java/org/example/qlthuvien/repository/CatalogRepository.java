package org.example.qlthuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.qlthuvien.entity.Catalog;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
}
