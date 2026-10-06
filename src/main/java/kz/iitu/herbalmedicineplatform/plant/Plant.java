package kz.iitu.herbalmedicineplatform.plant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "plants", uniqueConstraints = {
        @UniqueConstraint(name = "uk_plants_latin_name", columnNames = "latin_name")
})
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "common_name", nullable = false, length = 150)
    private String commonName;

    @Column(name = "latin_name", nullable = false, length = 200)
    private String latinName;

    @Column(length = 2000)
    private String description;

    @Column(name = "used_parts", length = 500)
    private String usedParts;

    protected Plant() {
    }

    public Plant(String commonName, String latinName, String description, String usedParts) {
        this.commonName = commonName;
        this.latinName = latinName;
        this.description = description;
        this.usedParts = usedParts;
    }

    public Long getId() { return id; }
    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    public String getLatinName() { return latinName; }
    public void setLatinName(String latinName) { this.latinName = latinName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUsedParts() { return usedParts; }
    public void setUsedParts(String usedParts) { this.usedParts = usedParts; }
}
