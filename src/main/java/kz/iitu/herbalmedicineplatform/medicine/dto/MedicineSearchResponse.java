package kz.iitu.herbalmedicineplatform.medicine.dto;

import java.util.List;

public record MedicineSearchResponse(
        List<MedicineSearchItem> items,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
    public MedicineSearchResponse {
        items = List.copyOf(items);
    }
}
