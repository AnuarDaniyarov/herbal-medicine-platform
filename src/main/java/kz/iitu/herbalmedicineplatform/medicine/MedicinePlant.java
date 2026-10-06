package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.plant.Plant;
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
import java.math.BigDecimal;

@Entity
@Table(name = "medicine_plants", uniqueConstraints = {
        @UniqueConstraint(name = "uk_medicine_plants_pair", columnNames = {"medicine_id", "plant_id"})
})
public class MedicinePlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private HerbalMedicine medicine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "plant_part", length = 100)
    private String plantPart;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantity;

    @Column(length = 30)
    private String unit;

    protected MedicinePlant() {
    }

    public MedicinePlant(HerbalMedicine medicine, Plant plant, String plantPart,
                         BigDecimal quantity, String unit) {
        this.medicine = medicine;
        this.plant = plant;
        this.plantPart = plantPart;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public HerbalMedicine getMedicine() { return medicine; }
    public void setMedicine(HerbalMedicine medicine) { this.medicine = medicine; }
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public String getPlantPart() { return plantPart; }
    public void setPlantPart(String plantPart) { this.plantPart = plantPart; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
