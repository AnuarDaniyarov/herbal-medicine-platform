package kz.iitu.herbalmedicineplatform.plant.dto;

import kz.iitu.herbalmedicineplatform.plant.Plant;

public record PlantResponse(Long id, String commonName, String latinName, String description, String usedParts) {
    public static PlantResponse from(Plant plant) {
        return new PlantResponse(plant.getId(), plant.getCommonName(), plant.getLatinName(),
                plant.getDescription(), plant.getUsedParts());
    }
}
