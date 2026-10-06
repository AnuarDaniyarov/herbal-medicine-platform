package kz.iitu.herbalmedicineplatform.medicine;

import static org.assertj.core.api.Assertions.assertThat;

import kz.iitu.herbalmedicineplatform.category.Category;
import kz.iitu.herbalmedicineplatform.category.CategoryRepository;
import kz.iitu.herbalmedicineplatform.ingredient.ActiveSubstance;
import kz.iitu.herbalmedicineplatform.ingredient.ActiveSubstanceRepository;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchRequest;
import kz.iitu.herbalmedicineplatform.medicine.dto.MedicineSearchResponse;
import kz.iitu.herbalmedicineplatform.plant.Plant;
import kz.iitu.herbalmedicineplatform.plant.PlantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MedicineSearchIntegrationTests {

    @Autowired
    private MedicineSearchService searchService;

    @Autowired
    private HerbalMedicineRepository medicineRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private ActiveSubstanceRepository activeSubstanceRepository;

    @Autowired
    private MedicinePlantRepository medicinePlantRepository;

    @Autowired
    private MedicineActiveSubstanceRepository medicineActiveSubstanceRepository;

    @Test
    void findsMedicineByNameAndRegistrationNumberWithPlantAndSubstance() {
        Category category = categoryRepository.save(new Category("Гепатопротекторы", "Тестовая категория"));
        HerbalMedicine medicine = medicineRepository.save(new HerbalMedicine(
                "Карсил тестовый", "Таблетки, покрытые оболочкой", "Экстракт плодов расторопши",
                null, null, "РК-ЛС-5№019885", category
        ));
        Plant plant = plantRepository.save(new Plant(
                "Расторопша пятнистая", "Silybum marianum", "Тестовая запись", "Плоды"
        ));
        ActiveSubstance substance = activeSubstanceRepository.save(
                new ActiveSubstance("Силимарин", "Тестовая запись")
        );
        medicinePlantRepository.save(new MedicinePlant(medicine, plant, "Плоды", null, null));
        medicineActiveSubstanceRepository.save(
                new MedicineActiveSubstance(medicine, substance, null, null)
        );

        MedicineSearchRequest request = new MedicineSearchRequest();
        request.setQuery("Карсил");
        request.setRegistrationNumber("019885");
        MedicineSearchResponse response = searchService.search(request);

        assertThat(response.totalItems()).isEqualTo(1);
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().getFirst().name()).isEqualTo("Карсил тестовый");
        assertThat(response.items().getFirst().registrationNumber()).isEqualTo("РК-ЛС-5№019885");
        assertThat(response.items().getFirst().nddaRegistryUrl()).isEqualTo("https://register.ndda.kz/#/reestr");
        assertThat(response.items().getFirst().plants()).containsExactly("Расторопша пятнистая");
        assertThat(response.items().getFirst().activeSubstances()).containsExactly("Силимарин");
    }
}
