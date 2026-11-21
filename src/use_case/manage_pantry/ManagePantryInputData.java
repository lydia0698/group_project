
package use_case.manage_pantry;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ManagePantryInputData {

    private final String id;
    private final String name;
    private final BigDecimal quantity;
    private final String unit;
    private final LocalDate expirationDate;

    public ManagePantryInputData(
            String id,
            String name,
            BigDecimal quantity,
            String unit,
            LocalDate expirationDate) {

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}

