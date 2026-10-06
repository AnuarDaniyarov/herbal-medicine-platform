package kz.iitu.herbalmedicineplatform.medicine.dto;

import java.util.List;
import kz.iitu.herbalmedicineplatform.medicine.HerbalMedicine;

public record HerbalMedicineResponse(
        Long id,
        String name,
        String dosageForm,
        String description,
        String dosageInstructions,
        String contraindications,
        String registrationNumber,
        String nddaRegistryUrl,
        Long categoryId,
        String categoryName,
        List<String> plants,
        List<String> activeSubstances
) {
    public HerbalMedicineResponse {
        plants = plants == null ? List.of() : List.copyOf(plants);
        activeSubstances = activeSubstances == null ? List.of() : List.copyOf(activeSubstances);
    }

    public static HerbalMedicineResponse from(HerbalMedicine medicine) {
        return from(medicine, List.of(), List.of());
    }

    public static HerbalMedicineResponse from(HerbalMedicine medicine,
                                              List<String> plants,
                                              List<String> activeSubstances) {
        return new HerbalMedicineResponse(
                medicine.getId(), medicine.getName(), medicine.getDosageForm(), medicine.getDescription(),
                medicine.getDosageInstructions(), medicine.getContraindications(),
                medicine.getRegistrationNumber(),
                medicine.getRegistrationNumber() == null ? null : "https://register.ndda.kz/#/reestr",
                medicine.getCategory() == null ? null : medicine.getCategory().getId(),
                medicine.getCategory() == null ? null : medicine.getCategory().getName(),
                plants,
                activeSubstances
        );
    }
}
