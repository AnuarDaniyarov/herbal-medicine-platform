package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchResponse;

public interface MedicineSearchService {

    MedicineSearchResponse search(MedicineSearchRequest request);
}
