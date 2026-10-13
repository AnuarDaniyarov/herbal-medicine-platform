package kz.iitu.medicineplatform.activity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import kz.iitu.medicineplatform.source.SourceResponse;

public record ActivityMeasurementResponse(
        Long id,
        Long compoundId,
        AssayResponse assay,
        SourceResponse source,
        BigDecimal value,
        String unit,
        String relation,
        String experimentalConditions,
        String qualityStatus,
        OffsetDateTime createdAt
) {
    public static ActivityMeasurementResponse from(ActivityMeasurement measurement) {
        return new ActivityMeasurementResponse(
                measurement.getId(), measurement.getCompound().getId(), AssayResponse.from(measurement.getAssay()),
                SourceResponse.from(measurement.getSource()), measurement.getValue(), measurement.getUnit(),
                measurement.getRelation(), measurement.getExperimentalConditions(), measurement.getQualityStatus(),
                measurement.getCreatedAt());
    }
}
