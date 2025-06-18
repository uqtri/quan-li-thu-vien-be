package org.example.qlthuvien.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDistribution {
    private String name;
    private int value;
}