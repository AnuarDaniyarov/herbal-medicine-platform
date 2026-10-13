package kz.iitu.medicineplatform.compound;

import java.util.List;
import kz.iitu.medicineplatform.activity.ActivityMeasurementResponse;
import kz.iitu.medicineplatform.api.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compounds")
public class CompoundController {

    private final CompoundService service;

    public CompoundController(CompoundService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<CompoundSummaryResponse> findAll(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long plantId,
            @RequestParam(required = false) StructureStatus structureStatus,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findAll(query, plantId, structureStatus, pageable);
    }

    @GetMapping("/{id}")
    public CompoundDetailsResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/measurements")
    public List<ActivityMeasurementResponse> findMeasurements(@PathVariable Long id) {
        return service.findMeasurements(id);
    }
}
