package org.example.qlthuvien.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.Catalog;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogResponse {
    private String name;
    private Long id;
}
