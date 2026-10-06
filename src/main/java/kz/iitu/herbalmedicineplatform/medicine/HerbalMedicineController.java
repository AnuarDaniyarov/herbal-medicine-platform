package kz.iitu.herbalmedicineplatform.medicine;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import kz.iitu.herbalmedicineplatform.medicine.dto.HerbalMedicineRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.HerbalMedicineResponse;
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
@RequestMapping("/api/medicines")
public class HerbalMedicineController {

    private final HerbalMedicineService service;

    public HerbalMedicineController(HerbalMedicineService service) { this.service = service; }

    @GetMapping
    public List<HerbalMedicineResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public HerbalMedicineResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<HerbalMedicineResponse> create(@Valid @RequestBody HerbalMedicineRequest request) {
        HerbalMedicineResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public HerbalMedicineResponse update(@PathVariable Long id,
                                         @Valid @RequestBody HerbalMedicineRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
