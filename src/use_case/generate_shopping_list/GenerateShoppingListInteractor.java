package use_case.generate_shopping_list;

import entity.IngredientQuantity;
import entity.PantryItem;
import entity.Recipe;
import entity.RecipeIngredient;
import entity.ShoppingListItem;
import use_case.pantry.PantryRepository;
import use_case.recipe.RecipeGateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class GenerateShoppingListInteractor
        implements GenerateShoppingListInputBoundary {

    private final PantryRepository pantryRepository;
    private final RecipeGateway recipeGateway;
    private final GenerateShoppingListOutputBoundary presenter;
    private final RecipeAmountParser amountParser =
            new RecipeAmountParser();

    public GenerateShoppingListInteractor(
            PantryRepository pantryRepository,
            RecipeGateway recipeGateway,
            GenerateShoppingListOutputBoundary presenter) {

        this.pantryRepository = pantryRepository;
        this.recipeGateway = recipeGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateShoppingListInputData inputData) {
        if (inputData == null || inputData.getRecipeId() == null
                || inputData.getRecipeId().isBlank()) {
            presenter.presentError("Please select a recipe.");
            return;
        }

        Recipe recipe;

        try {
            recipe = recipeGateway.findById(
                    inputData.getRecipeId().trim());
        } catch (IllegalStateException exception) {
            presenter.presentError(
                    "Unable to load the recipe. Please try again.");
            return;
        }

        if (recipe == null) {
            presenter.presentError("Recipe not found.");
            return;
        }

        Map<String, List<RecipeIngredient>> requiredByName =
                groupIngredients(recipe);

        if (requiredByName.isEmpty()) {
            presenter.presentError(
                    "Ingredient information is unavailable.");
            return;
        }

        Map<String, List<IngredientQuantity>> stockByName =
                readPantry();

        List<ShoppingListItem> items = new ArrayList<>();

        for (Map.Entry<String, List<RecipeIngredient>> entry
                : requiredByName.entrySet()) {

            List<IngredientQuantity> stock =
                    stockByName.getOrDefault(
                            entry.getKey(), List.of());

            calculateItem(entry.getValue(), stock)
                    .ifPresent(items::add);
        }

        presenter.presentSuccess(
                new GenerateShoppingListOutputData(
                        recipe.getName(), items));
    }

    private Map<String, List<RecipeIngredient>> groupIngredients(
            Recipe recipe) {

        Map<String, List<RecipeIngredient>> groups =
                new LinkedHashMap<>();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            String name = ingredient.getName();

            if (name != null && !name.isBlank()) {
                groups.computeIfAbsent(
                        normalizeName(name),
                        key -> new ArrayList<>()
                ).add(ingredient);
            }
        }

        return groups;
    }

    private Map<String, List<IngredientQuantity>> readPantry() {
        Map<String, List<IngredientQuantity>> stock =
                new LinkedHashMap<>();

        for (PantryItem item : pantryRepository.findAll()) {
            if (item.getQuantity().signum() > 0) {
                IngredientQuantity quantity =
                        new IngredientQuantity(
                                item.getQuantity(), item.getUnit());

                stock.computeIfAbsent(
                        normalizeName(item.getName()),
                        key -> new ArrayList<>()
                ).add(quantity);
            }
        }

        return stock;
    }

    private Optional<ShoppingListItem> calculateItem(
            List<RecipeIngredient> ingredients,
            List<IngredientQuantity> stock) {

        String name = ingredients.get(0).getName().trim();

        List<String> recipeAmounts = ingredients.stream()
                .map(ingredient ->
                        ingredient.getRecipeAmount() == null
                                ? "" : ingredient.getRecipeAmount())
                .toList();

        IngredientQuantity required = null;

        for (RecipeIngredient ingredient : ingredients) {
            Optional<IngredientQuantity> parsed =
                    amountParser.parse(
                            ingredient.getRecipeAmount());

            if (parsed.isEmpty()) {
                return Optional.of(
                        ShoppingListItem.needsReview(
                                name, recipeAmounts));
            }

            IngredientQuantity quantity = parsed.get();

            if (required == null) {
                required = quantity;
            } else if (required.canCompareTo(quantity)) {
                required = new IngredientQuantity(
                        required.getAmount().add(
                                quantity.getAmount()),
                        required.getUnit());
            } else {
                return Optional.of(
                        ShoppingListItem.needsReview(
                                name, recipeAmounts));
            }
        }

        BigDecimal available = BigDecimal.ZERO;
        boolean hasOtherUnits = false;

        for (IngredientQuantity quantity : stock) {
            if (required.canCompareTo(quantity)) {
                available = available.add(quantity.getAmount());
            } else {
                hasOtherUnits = true;
            }
        }

        BigDecimal missing =
                required.getAmount().subtract(available);

        if (missing.signum() <= 0) {
            return Optional.empty();
        }

        if (hasOtherUnits) {
            return Optional.of(
                    ShoppingListItem.needsReview(
                            name, recipeAmounts));
        }

        IngredientQuantity missingQuantity =
                new IngredientQuantity(
                        missing, required.getUnit());

        return Optional.of(
                ShoppingListItem.toBuy(name, missingQuantity));
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
