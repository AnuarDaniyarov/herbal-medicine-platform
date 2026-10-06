package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchItem;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;

@Service
public class JpaMedicineSearchService implements MedicineSearchService {

    private final HerbalMedicineRepository medicineRepository;
    private final MedicinePlantRepository medicinePlantRepository;
    private final MedicineActiveSubstanceRepository activeSubstanceRepository;

    public JpaMedicineSearchService(HerbalMedicineRepository medicineRepository,
                                    MedicinePlantRepository medicinePlantRepository,
                                    MedicineActiveSubstanceRepository activeSubstanceRepository) {
        this.medicineRepository = medicineRepository;
        this.medicinePlantRepository = medicinePlantRepository;
        this.activeSubstanceRepository = activeSubstanceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineSearchResponse search(MedicineSearchRequest request) {
        Specification<HerbalMedicine> specification = (root, query, criteria) -> {
            List<Predicate> predicates = new ArrayList<>();

            String text = trimToNull(request.getQuery());
            if (text != null) {
                String pattern = "%" + text.toLowerCase() + "%";
                predicates.add(criteria.or(
                        criteria.like(criteria.lower(root.get("name")), pattern),
                        criteria.like(criteria.lower(root.get("description")), pattern)
                ));
            }

            if (request.getCategoryId() != null) {
                predicates.add(criteria.equal(root.get("category").get("id"), request.getCategoryId()));
            }

            String dosageForm = trimToNull(request.getDosageForm());
            if (dosageForm != null) {
                predicates.add(criteria.like(
                        criteria.lower(root.get("dosageForm")),
                        "%" + dosageForm.toLowerCase() + "%"
                ));
            }

            String registrationNumber = trimToNull(request.getRegistrationNumber());
            if (registrationNumber != null) {
                predicates.add(criteria.like(
                        criteria.lower(root.get("registrationNumber")),
                        "%" + registrationNumber.toLowerCase() + "%"
                ));
            }

            if (request.getPlantId() != null) {
                Subquery<Long> plantMatch = query.subquery(Long.class);
                var link = plantMatch.from(MedicinePlant.class);
                plantMatch.select(link.get("medicine").get("id"));
                plantMatch.where(
                        criteria.equal(link.get("medicine").get("id"), root.get("id")),
                        criteria.equal(link.get("plant").get("id"), request.getPlantId())
                );
                predicates.add(criteria.exists(plantMatch));
            }

            if (request.getActiveSubstanceId() != null) {
                Subquery<Long> substanceMatch = query.subquery(Long.class);
                var link = substanceMatch.from(MedicineActiveSubstance.class);
                substanceMatch.select(link.get("medicine").get("id"));
                substanceMatch.where(
                        criteria.equal(link.get("medicine").get("id"), root.get("id")),
                        criteria.equal(link.get("substance").get("id"), request.getActiveSubstanceId())
                );
                predicates.add(criteria.exists(substanceMatch));
            }

            return criteria.and(predicates.toArray(Predicate[]::new));
        };

        Page<HerbalMedicine> resultPage = medicineRepository.findAll(
                specification,
                PageRequest.of(request.getPage(), request.getSize(), Sort.by("name").ascending())
        );

        List<HerbalMedicine> medicines = resultPage.getContent();
        List<Long> medicineIds = medicines.stream().map(HerbalMedicine::getId).toList();

        Map<Long, List<String>> plantsByMedicine = new HashMap<>();
        if (!medicineIds.isEmpty()) {
            for (MedicinePlant link : medicinePlantRepository.findAllByMedicine_IdIn(medicineIds)) {
                plantsByMedicine.computeIfAbsent(link.getMedicine().getId(), ignored -> new ArrayList<>())
                        .add(link.getPlant().getCommonName());
            }
        }

        Map<Long, List<String>> substancesByMedicine = new HashMap<>();
        if (!medicineIds.isEmpty()) {
            for (MedicineActiveSubstance link : activeSubstanceRepository.findAllByMedicine_IdIn(medicineIds)) {
                substancesByMedicine.computeIfAbsent(link.getMedicine().getId(), ignored -> new ArrayList<>())
                        .add(link.getSubstance().getName());
            }
        }

        List<MedicineSearchItem> items = medicines.stream()
                .map(medicine -> new MedicineSearchItem(
                        medicine.getId(),
                        medicine.getName(),
                        medicine.getRegistrationNumber(),
                        medicine.getRegistrationNumber() == null ? null : "https://register.ndda.kz/#/reestr",
                        medicine.getDosageForm(),
                        medicine.getDescription(),
                        medicine.getCategory() == null ? null : medicine.getCategory().getName(),
                        plantsByMedicine.getOrDefault(medicine.getId(), List.of()),
                        substancesByMedicine.getOrDefault(medicine.getId(), List.of())
                ))
                .toList();

        return new MedicineSearchResponse(
                items,
                request.getPage(),
                request.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages()
        );
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
