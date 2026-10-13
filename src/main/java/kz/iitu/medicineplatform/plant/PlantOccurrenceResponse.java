package kz.iitu.medicineplatform.plant;

import kz.iitu.medicineplatform.source.SourceResponse;

public record PlantOccurrenceResponse(
        Long plantId,
        String scientificName,
        String displayName,
        String plantPart,
        String sampleOrBatch,
        SourceResponse source,
        String notes
) {
    public static PlantOccurrenceResponse from(PlantCompoundRelationship relationship) {
        Plant plant = relationship.getPlant();
        return new PlantOccurrenceResponse(
                plant.getId(), plant.getScientificName(), plant.getDisplayName(), relationship.getPlantPart(),
                relationship.getSampleOrBatch(),
                relationship.getSource() == null ? null : SourceResponse.from(relationship.getSource()),
                relationship.getNotes());
    }
}
