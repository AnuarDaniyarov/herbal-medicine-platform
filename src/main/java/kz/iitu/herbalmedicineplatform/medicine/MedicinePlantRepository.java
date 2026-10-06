package kz.iitu.herbalmedicineplatform.medicine;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicinePlantRepository extends JpaRepository<MedicinePlant, Long> {

    List<MedicinePlant> findAllByMedicine_IdIn(Collection<Long> medicineIds);
}
