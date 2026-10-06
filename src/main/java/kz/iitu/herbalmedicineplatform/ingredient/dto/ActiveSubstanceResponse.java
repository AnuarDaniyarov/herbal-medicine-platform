package kz.iitu.herbalmedicineplatform.ingredient.dto;

import kz.iitu.herbalmedicineplatform.ingredient.ActiveSubstance;

public record ActiveSubstanceResponse(Long id, String name, String description) {
    public static ActiveSubstanceResponse from(ActiveSubstance substance) {
        return new ActiveSubstanceResponse(substance.getId(), substance.getName(), substance.getDescription());
    }
}
