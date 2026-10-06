package kz.iitu.herbalmedicineplatform.plant;

import kz.iitu.herbalmedicineplatform.common.exception.ResourceNotFoundException;
import kz.iitu.herbalmedicineplatform.plant.dto.PlantRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlantService {

    private final PlantRepository repository;

    public PlantService(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> findAll() { return repository.findAll(); }

    public Plant findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plant", id));
    }

    @Transactional
    public Plant create(PlantRequest request) {
        return repository.save(new Plant(request.commonName().trim(), request.latinName().trim(),
                request.description(), request.usedParts()));
    }

    @Transactional
    public Plant update(Long id, PlantRequest request) {
        Plant plant = findById(id);
        plant.setCommonName(request.commonName().trim());
        plant.setLatinName(request.latinName().trim());
        plant.setDescription(request.description());
        plant.setUsedParts(request.usedParts());
        return repository.save(plant);
    }

    @Transactional
    public void delete(Long id) { repository.delete(findById(id)); }
}
