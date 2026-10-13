package kz.iitu.medicineplatform;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import kz.iitu.medicineplatform.activity.ActivityMeasurement;
import kz.iitu.medicineplatform.activity.ActivityMeasurementRepository;
import kz.iitu.medicineplatform.activity.Assay;
import kz.iitu.medicineplatform.activity.AssayRepository;
import kz.iitu.medicineplatform.compound.Compound;
import kz.iitu.medicineplatform.compound.CompoundRepository;
import kz.iitu.medicineplatform.plant.Plant;
import kz.iitu.medicineplatform.plant.PlantCompoundRelationship;
import kz.iitu.medicineplatform.plant.PlantCompoundRelationshipRepository;
import kz.iitu.medicineplatform.plant.PlantRepository;
import kz.iitu.medicineplatform.prediction.PredictionRepository;
import kz.iitu.medicineplatform.prediction.ModelVersionRepository;
import kz.iitu.medicineplatform.source.DataSource;
import kz.iitu.medicineplatform.source.DataSourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicinePlatformApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plants;

    @Autowired
    private CompoundRepository compounds;

    @Autowired
    private PlantCompoundRelationshipRepository relationships;

    @Autowired
    private DataSourceRepository sources;

    @Autowired
    private AssayRepository assays;

    @Autowired
    private ActivityMeasurementRepository measurements;

    @Autowired
    private PredictionRepository predictions;

    @Autowired
    private ModelVersionRepository modelVersions;

    @BeforeEach
    void clearDatabase() {
        predictions.deleteAll();
        measurements.deleteAll();
        relationships.deleteAll();
        compounds.deleteAll();
        plants.deleteAll();
        sources.deleteAll();
    }

    @Test
    void catalogSearchReturnsPlantCompoundAndSourceProvenance() throws Exception {
        Plant plant = plants.save(new Plant("Example species", "Example plant", "Example family", "Test plant"));
        Compound compound = compounds.save(new Compound(
                "Example compound", "CCO", null, "TEST-1", "C2H6O", "UNVERIFIED"));
        DataSource source = sources.save(new DataSource(
                "Example paper", "A. Author", 2024, "10.0000/example", "https://example.org/paper",
                "ARTICLE", OffsetDateTime.now(ZoneOffset.UTC)));
        relationships.save(new PlantCompoundRelationship(
                plant, compound, "leaf", "batch-1", source, "Example provenance record"));
        Assay assay = assays.save(new Assay(
                "test-assay", "Test assay", "Test target", "IN_VITRO", "Test description", "uM"));
        measurements.save(new ActivityMeasurement(
                compound, assay, source, new BigDecimal("1.25000000"), "uM", "=", "Test conditions", "UNVERIFIED"));

        mockMvc.perform(get("/api/plants").param("query", "example"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].scientificName").value("Example species"));

        mockMvc.perform(get("/api/plants/{id}/compounds", plant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Example compound"));

        mockMvc.perform(get("/api/compounds").param("query", "example").param("plantId", plant.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].structureStatus").value("UNVERIFIED"));

        mockMvc.perform(get("/api/compounds/{id}", compound.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plantOccurrences[0].source.doi").value("10.0000/example"));

        mockMvc.perform(get("/api/compounds/{id}/measurements", compound.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assay.activityCode").value("test-assay"))
                .andExpect(jsonPath("$[0].source.id").value(source.getId()));

        mockMvc.perform(get("/api/sources/{id}", source.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Example paper"));
    }

    @Test
    void predictionStubIsExplicitlyMarkedAndSavedToHistory() throws Exception {
        assertEquals(1, modelVersions.findAll().size());
        assertEquals("demo", modelVersions.findAll().getFirst().getActivityCode());
        assertTrue(modelVersions.findAll().getFirst().isActive());

        String request = """
                {
                  "smiles": "CCO",
                  "activityCode": "demo"
                }
                """;

        mockMvc.perform(post("/api/predictions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("STUB"))
                .andExpect(jsonPath("$.probability").doesNotExist())
                .andExpect(jsonPath("$.warning").value(org.hamcrest.Matchers.containsString("no inference model")))
                .andExpect(jsonPath("$.predictionId").exists());

        long id = predictions.findAll().getFirst().getId();
        mockMvc.perform(get("/api/predictions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelName").value("prediction-api-placeholder"));
    }

    @Test
    void invalidPredictionAndMissingPlantReturnStructuredErrors() throws Exception {
        mockMvc.perform(post("/api/predictions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"smiles\":\" \",\"activityCode\":\"demo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("smiles"));

        mockMvc.perform(get("/api/plants/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/plants/999999"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void openApiDocumentIsAvailable() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.paths['/api/plants']").exists())
                .andExpect(jsonPath("$.paths['/api/predictions']").exists());
    }
}
