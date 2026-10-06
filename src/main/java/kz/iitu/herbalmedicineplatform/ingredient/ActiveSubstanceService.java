package kz.iitu.herbalmedicineplatform.ingredient;

import kz.iitu.herbalmedicineplatform.common.exception.ResourceNotFoundException;
import kz.iitu.herbalmedicineplatform.ingredient.dto.ActiveSubstanceRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ActiveSubstanceService {

    private final ActiveSubstanceRepository repository;

    public ActiveSubstanceService(ActiveSubstanceRepository repository) { this.repository = repository; }

    public List<ActiveSubstance> findAll() { return repository.findAll(); }

    public ActiveSubstance findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active substance", id));
    }

    @Transactional
    public ActiveSubstance create(ActiveSubstanceRequest request) {
        return repository.save(new ActiveSubstance(request.name().trim(), request.description()));
    }

    @Transactional
    public ActiveSubstance update(Long id, ActiveSubstanceRequest request) {
        ActiveSubstance substance = findById(id);
        substance.setName(request.name().trim());
        substance.setDescription(request.description());
        return repository.save(substance);
    }

    @Transactional
    public void delete(Long id) { repository.delete(findById(id)); }
}
