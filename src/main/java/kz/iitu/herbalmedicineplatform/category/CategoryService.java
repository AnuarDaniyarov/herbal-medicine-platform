package kz.iitu.herbalmedicineplatform.category;

import kz.iitu.herbalmedicineplatform.category.dto.CategoryRequest;
import kz.iitu.herbalmedicineplatform.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Transactional
    public Category create(CategoryRequest request) {
        return repository.save(new Category(request.name().trim(), request.description()));
    }

    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = findById(id);
        category.setName(request.name().trim());
        category.setDescription(request.description());
        return repository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        repository.delete(category);
    }
}
