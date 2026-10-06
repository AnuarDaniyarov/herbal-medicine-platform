package kz.iitu.herbalmedicineplatform.medicine;

import kz.iitu.herbalmedicineplatform.ingredient.ActiveSubstance;
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
@Table(name = "medicine_active_substances", uniqueConstraints = {
        @UniqueConstraint(name = "uk_medicine_substance_pair", columnNames = {"medicine_id", "substance_id"})
})
public class MedicineActiveSubstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private HerbalMedicine medicine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "substance_id", nullable = false)
    private ActiveSubstance substance;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantity;

    @Column(length = 30)
    private String unit;

    protected MedicineActiveSubstance() {
    }

    public MedicineActiveSubstance(HerbalMedicine medicine, ActiveSubstance substance,
                                   BigDecimal quantity, String unit) {
        this.medicine = medicine;
        this.substance = substance;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public HerbalMedicine getMedicine() { return medicine; }
    public void setMedicine(HerbalMedicine medicine) { this.medicine = medicine; }
    public ActiveSubstance getSubstance() { return substance; }
    public void setSubstance(ActiveSubstance substance) { this.substance = substance; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
