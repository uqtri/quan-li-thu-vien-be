package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.dashboard.CategoryDistribution;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DashboardMapper {

    @Named("objectToCategoryDistribution")
    default CategoryDistribution objectToCategoryDistribution(Object[] obj) {
        return new CategoryDistribution((String) obj[0], ((Number) obj[1]).intValue());
    }
}