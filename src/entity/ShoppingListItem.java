package entity;

import java.util.List;
import java.util.Optional;

public class ShoppingListItem {
    private final String name;
    private final IngredientQuantity missingQuantity;
    private final List<String> recipeAmounts;

    private ShoppingListItem(
            String name,
            IngredientQuantity missingQuantity,
            List<String> recipeAmounts) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Ingredient name is required.");
        }

        this.name = name.trim();
        this.missingQuantity = missingQuantity;
        this.recipeAmounts = List.copyOf(recipeAmounts);
    }

    public static ShoppingListItem toBuy(
            String name,
            IngredientQuantity missingQuantity) {

        if (missingQuantity == null
                || missingQuantity.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Missing quantity must be positive.");
        }

        return new ShoppingListItem(
                name, missingQuantity, List.of());
    }

    public static ShoppingListItem needsReview(
            String name,
            List<String> recipeAmounts) {

        return new ShoppingListItem(name, null, recipeAmounts);
    }

    public String getName() {
        return name;
    }

    public Optional<IngredientQuantity> getMissingQuantity() {
        return Optional.ofNullable(missingQuantity);
    }

    public List<String> getRecipeAmounts() {
        return recipeAmounts;
    }

    public boolean requiresReview() {
        return missingQuantity == null;
    }
}
