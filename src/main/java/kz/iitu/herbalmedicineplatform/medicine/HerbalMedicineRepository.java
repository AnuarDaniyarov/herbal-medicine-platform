package kz.iitu.herbalmedicineplatform.medicine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HerbalMedicineRepository
        extends JpaRepository<HerbalMedicine, Long>, JpaSpecificationExecutor<HerbalMedicine> {
}
