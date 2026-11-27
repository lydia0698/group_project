package use_case.generate_shopping_list;

import entity.IngredientQuantity;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.MathContext;

public class RecipeAmountParser {
    private static final Set<String> SUPPORTED_UNITS =
            Set.of("g", "ml", "pcs", "cup", "tbsp", "tsp");

    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "^(\\d+\\s+\\d+/\\d+|\\d+/\\d+|\\d+(?:\\.\\d+)?|\\.\\d+)"
                    + "\\s*([a-zA-Z]+)?$");

    public Optional<IngredientQuantity> parse(String recipeAmount) {
        if (recipeAmount == null || recipeAmount.isBlank()) {
            return Optional.empty();
        }

        Matcher matcher =
                AMOUNT_PATTERN.matcher(recipeAmount.trim());

        if (!matcher.matches()) {
            return Optional.empty();
        }

        String amountText = matcher.group(1);
        String unit = matcher.group(2);

        if (unit == null) {
            // A whole number without a unit represents a piece count.
            if (!amountText.matches("\\d+")) {
                return Optional.empty();
            }
            unit = "pcs";
        }

        try {
            BigDecimal amount = parseAmount(amountText);

            if (amount.signum() <= 0) {
                return Optional.empty();
            }

            IngredientQuantity quantity =
                    new IngredientQuantity(amount, unit);

            if (!SUPPORTED_UNITS.contains(quantity.getUnit())) {
                return Optional.empty();
            }

            return Optional.of(quantity);
        } catch (IllegalArgumentException | ArithmeticException exception) {
            return Optional.empty();
        }
    }

    private BigDecimal parseAmount(String amountText) {
        String[] parts = amountText.split("\\s+");
        String number = parts[parts.length - 1];
        BigDecimal amount;

        if (number.contains("/")) {
            String[] fraction = number.split("/");
            BigDecimal numerator = new BigDecimal(fraction[0]);
            BigDecimal denominator = new BigDecimal(fraction[1]);

            // Avoid rounding quantities when the fraction is not exact.
            amount = numerator.divide(
                    denominator, MathContext.DECIMAL128);
        } else {
            amount = new BigDecimal(number);
        }

        if (parts.length == 2) {
            amount = amount.add(new BigDecimal(parts[0]));
        }

        return amount;
    }
}
