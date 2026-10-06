package kz.iitu.herbalmedicineplatform.medicine.dto;

import java.util.List;

public record MedicineSearchItem(
        Long id,
        String name,
        String registrationNumber,
        String nddaRegistryUrl,
        String dosageForm,
        String description,
        String categoryName,
        List<String> plants,
        List<String> activeSubstances
) {
    public MedicineSearchItem {
        plants = List.copyOf(plants);
        activeSubstances = List.copyOf(activeSubstances);
    }
}
