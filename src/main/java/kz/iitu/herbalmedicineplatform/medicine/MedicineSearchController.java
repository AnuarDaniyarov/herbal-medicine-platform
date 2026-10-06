package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/medicines")
public class MedicineSearchController {

    private final MedicineSearchService medicineSearchService;

    public MedicineSearchController(MedicineSearchService medicineSearchService) {
        this.medicineSearchService = medicineSearchService;
    }

    @GetMapping("/search")
    public MedicineSearchResponse search(@Valid @ModelAttribute MedicineSearchRequest request) {
        return medicineSearchService.search(request);
    }
}
