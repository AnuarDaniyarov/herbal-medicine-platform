package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "herbal_medicines", uniqueConstraints = {
        @UniqueConstraint(name = "uk_herbal_medicines_name", columnNames = "name"),
        @UniqueConstraint(name = "uk_herbal_medicines_registration_number", columnNames = "registration_number")
})
public class HerbalMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "dosage_form", length = 100)
    private String dosageForm;

    @Column(length = 2000)
    private String description;

    @Column(name = "dosage_instructions", length = 2000)
    private String dosageInstructions;

    @Column(length = 2000)
    private String contraindications;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected HerbalMedicine() {
    }

    public HerbalMedicine(String name, String dosageForm, String description,
                          String dosageInstructions, String contraindications,
                          String registrationNumber, Category category) {
        this.name = name;
        this.dosageForm = dosageForm;
        this.description = description;
        this.dosageInstructions = dosageInstructions;
        this.contraindications = contraindications;
        this.registrationNumber = registrationNumber;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDosageInstructions() { return dosageInstructions; }
    public void setDosageInstructions(String dosageInstructions) { this.dosageInstructions = dosageInstructions; }
    public String getContraindications() { return contraindications; }
    public void setContraindications(String contraindications) { this.contraindications = contraindications; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
