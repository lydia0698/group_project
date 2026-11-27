package entity;

import java.math.BigDecimal;
import java.util.Locale;

public class IngredientQuantity {
    private final BigDecimal amount;
    private final String unit;

    public IngredientQuantity(BigDecimal amount, String unit) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Quantity must be non-negative.");
        }

        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit is required.");
        }

        BigDecimal normalizedAmount = amount;
        String normalizedUnit =
                unit.trim().toLowerCase(Locale.ROOT);

        switch (normalizedUnit) {
            case "kg", "kilogram", "kilograms" -> {
                normalizedAmount =
                        amount.multiply(BigDecimal.valueOf(1000));
                normalizedUnit = "g";
            }

            case "g", "gram", "grams" ->
                    normalizedUnit = "g";

            case "l", "liter", "liters", "litre", "litres" -> {
                normalizedAmount =
                        amount.multiply(BigDecimal.valueOf(1000));
                normalizedUnit = "ml";
            }

            case "ml", "milliliter", "milliliters",
                 "millilitre", "millilitres" ->
                    normalizedUnit = "ml";

            case "pc", "pcs", "piece", "pieces" ->
                    normalizedUnit = "pcs";

            case "cup", "cups" ->
                    normalizedUnit = "cup";

            case "tbsp", "tbs", "tablespoon", "tablespoons" -> {
                normalizedAmount =
                        amount.multiply(BigDecimal.valueOf(3));
                normalizedUnit = "tsp";
            }

            case "tsp", "teaspoon", "teaspoons" ->
                    normalizedUnit = "tsp";

            default -> throw new IllegalArgumentException(
                    "Unsupported unit: " + unit);
        }

        this.amount = normalizedAmount;
        this.unit = normalizedUnit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public boolean canCompareTo(IngredientQuantity other) {
        return other != null && unit.equals(other.unit);
    }

    @Override
    public String toString() {
        return amount.stripTrailingZeros().toPlainString()
                + " " + unit;
    }
}
