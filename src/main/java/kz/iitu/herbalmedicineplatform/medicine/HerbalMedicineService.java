package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.category.Category;
import kz.iitu.herbalmedicineplatform.category.CategoryRepository;
import kz.iitu.herbalmedicineplatform.common.exception.ResourceNotFoundException;
import kz.iitu.herbalmedicineplatform.medicine.dto.HerbalMedicineRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.HerbalMedicineResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HerbalMedicineService {

    private final HerbalMedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;
    private final MedicinePlantRepository medicinePlantRepository;
    private final MedicineActiveSubstanceRepository medicineActiveSubstanceRepository;

    public HerbalMedicineService(HerbalMedicineRepository medicineRepository,
                                 CategoryRepository categoryRepository,
                                 MedicinePlantRepository medicinePlantRepository,
                                 MedicineActiveSubstanceRepository medicineActiveSubstanceRepository) {
        this.medicineRepository = medicineRepository;
        this.categoryRepository = categoryRepository;
        this.medicinePlantRepository = medicinePlantRepository;
        this.medicineActiveSubstanceRepository = medicineActiveSubstanceRepository;
    }

    public List<HerbalMedicineResponse> findAll() {
        return medicineRepository.findAll().stream().map(HerbalMedicineResponse::from).toList();
    }

    public HerbalMedicineResponse findById(Long id) {
        HerbalMedicine medicine = requireMedicine(id);
        List<String> plants = medicinePlantRepository.findAllByMedicine_IdIn(List.of(id)).stream()
                .map(link -> link.getPlant().getCommonName())
                .toList();
        List<String> substances = medicineActiveSubstanceRepository.findAllByMedicine_IdIn(List.of(id)).stream()
                .map(link -> link.getSubstance().getName())
                .toList();
        return HerbalMedicineResponse.from(medicine, plants, substances);
    }

    @Transactional
    public HerbalMedicineResponse create(HerbalMedicineRequest request) {
        HerbalMedicine medicine = new HerbalMedicine(
                request.name().trim(), request.dosageForm(), request.description(),
                request.dosageInstructions(), request.contraindications(),
                trimToNull(request.registrationNumber()), findCategory(request.categoryId())
        );
        return HerbalMedicineResponse.from(medicineRepository.save(medicine));
    }

    @Transactional
    public HerbalMedicineResponse update(Long id, HerbalMedicineRequest request) {
        HerbalMedicine medicine = requireMedicine(id);
        medicine.setName(request.name().trim());
        medicine.setDosageForm(request.dosageForm());
        medicine.setDescription(request.description());
        medicine.setDosageInstructions(request.dosageInstructions());
        medicine.setContraindications(request.contraindications());
        medicine.setRegistrationNumber(trimToNull(request.registrationNumber()));
        medicine.setCategory(findCategory(request.categoryId()));
        return HerbalMedicineResponse.from(medicineRepository.save(medicine));
    }

    @Transactional
    public void delete(Long id) {
        medicineRepository.delete(requireMedicine(id));
    }

    private HerbalMedicine requireMedicine(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Herbal medicine", id));
    }

    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
