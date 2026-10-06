package kz.iitu.herbalmedicineplatform.medicine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HerbalMedicineRequest(
        @NotBlank @Size(max = 200) String name,
        @Size(max = 100) String dosageForm,
        @Size(max = 2000) String description,
        @Size(max = 2000) String dosageInstructions,
        @Size(max = 2000) String contraindications,
        @Size(max = 100) String registrationNumber,
        @Positive Long categoryId
) {
}
