package kz.iitu.herbalmedicineplatform.plant;

import kz.iitu.herbalmedicineplatform.plant.dto.PlantRequest;
import kz.iitu.herbalmedicineplatform.plant.dto.PlantResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService service;

    public PlantController(PlantService service) { this.service = service; }

    @GetMapping
    public List<PlantResponse> findAll() { return service.findAll().stream().map(PlantResponse::from).toList(); }

    @GetMapping("/{id}")
    public PlantResponse findById(@PathVariable Long id) { return PlantResponse.from(service.findById(id)); }

    @PostMapping
    public ResponseEntity<PlantResponse> create(@Valid @RequestBody PlantRequest request) {
        Plant created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(PlantResponse.from(created));
    }

    @PutMapping("/{id}")
    public PlantResponse update(@PathVariable Long id, @Valid @RequestBody PlantRequest request) {
        return PlantResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
