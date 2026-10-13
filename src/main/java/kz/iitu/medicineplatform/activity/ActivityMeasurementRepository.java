package kz.iitu.medicineplatform.activity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMeasurementRepository extends JpaRepository<ActivityMeasurement, Long> {
    List<ActivityMeasurement> findAllByCompoundIdOrderByCreatedAtDesc(Long compoundId);
}
