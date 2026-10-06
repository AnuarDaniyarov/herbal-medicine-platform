package kz.iitu.herbalmedicineplatform.medicine;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineActiveSubstanceRepository
        extends JpaRepository<MedicineActiveSubstance, Long> {

    List<MedicineActiveSubstance> findAllByMedicine_IdIn(Collection<Long> medicineIds);
}
