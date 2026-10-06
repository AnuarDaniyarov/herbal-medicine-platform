package kz.iitu.herbalmedicineplatform.ingredient;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import kz.iitu.herbalmedicineplatform.ingredient.dto.ActiveSubstanceRequest;
import kz.iitu.herbalmedicineplatform.ingredient.dto.ActiveSubstanceResponse;
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
@RequestMapping("/api/active-substances")
public class ActiveSubstanceController {

    private final ActiveSubstanceService service;

    public ActiveSubstanceController(ActiveSubstanceService service) { this.service = service; }

    @GetMapping
    public List<ActiveSubstanceResponse> findAll() {
        return service.findAll().stream().map(ActiveSubstanceResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ActiveSubstanceResponse findById(@PathVariable Long id) {
        return ActiveSubstanceResponse.from(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActiveSubstanceResponse> create(@Valid @RequestBody ActiveSubstanceRequest request) {
        ActiveSubstance created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(ActiveSubstanceResponse.from(created));
    }

    @PutMapping("/{id}")
    public ActiveSubstanceResponse update(@PathVariable Long id,
                                          @Valid @RequestBody ActiveSubstanceRequest request) {
        return ActiveSubstanceResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
