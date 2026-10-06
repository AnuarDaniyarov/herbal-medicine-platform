package kz.iitu.herbalmedicineplatform.plant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlantRequest(
        @NotBlank @Size(max = 150) String commonName,
        @NotBlank @Size(max = 200) String latinName,
        @Size(max = 2000) String description,
        @Size(max = 500) String usedParts
) {
}
