package kz.iitu.medicineplatform.prediction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PredictionResponse(
        Long predictionId,
        String status,
        String activityCode,
        String inputSmiles,
        String predictedLabel,
        BigDecimal predictedValue,
        BigDecimal probability,
        String confidenceLevel,
        String modelName,
        String modelVersion,
        String warning,
        OffsetDateTime createdAt
) {
    public static PredictionResponse from(Prediction prediction) {
        return new PredictionResponse(
                prediction.getId(), prediction.getStatus(), prediction.getActivityCode(), prediction.getInputSmiles(),
                prediction.getPredictedLabel(), prediction.getPredictedValue(), prediction.getProbability(),
                prediction.getConfidenceLevel(), prediction.getModelVersion().getName(),
                prediction.getModelVersion().getVersion(), prediction.getWarning(), prediction.getCreatedAt());
    }
}
