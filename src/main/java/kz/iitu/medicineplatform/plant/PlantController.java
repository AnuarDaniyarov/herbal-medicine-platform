package kz.iitu.medicineplatform.plant;

import kz.iitu.medicineplatform.api.PageResponse;
import kz.iitu.medicineplatform.compound.CompoundSummaryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService service;

    public PlantController(PlantService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<PlantResponse> findAll(@RequestParam(required = false) String query,
                                               @PageableDefault(size = 20, sort = "scientificName") Pageable pageable) {
        return service.findAll(query, pageable);
    }

    @GetMapping("/{id}")
    public PlantResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/compounds")
    public PageResponse<CompoundSummaryResponse> findCompounds(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findCompounds(id, pageable);
    }
}
