package kz.iitu.herbalmedicineplatform.medicine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class MedicineSearchRequest {

    @Size(max = 200)
    private String query;

    @Min(1)
    private Long categoryId;

    @Min(1)
    private Long plantId;

    @Min(1)
    private Long activeSubstanceId;

    @Size(max = 100)
    private String dosageForm;

    @Size(max = 100)
    private String registrationNumber;

    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 20;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getPlantId() { return plantId; }
    public void setPlantId(Long plantId) { this.plantId = plantId; }
    public Long getActiveSubstanceId() { return activeSubstanceId; }
    public void setActiveSubstanceId(Long activeSubstanceId) { this.activeSubstanceId = activeSubstanceId; }
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
