package use_case.generate_shopping_list;

import entity.PantryItem;
import entity.Recipe;
import entity.RecipeIngredient;
import use_case.pantry.PantryRepository;
import use_case.recipe.RecipeGateway;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GenerateShoppingListInteractor implements GenerateShoppingListInputBoundary {
    private final PantryRepository pantryRepository;
    private final RecipeGateway recipeGateway;
    private final GenerateShoppingListOutputBoundary presenter;

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
            recipe = recipeGateway.findById(inputData.getRecipeId().trim());
        } catch (IllegalStateException exception) {
            presenter.presentError(
                    "Unable to load the recipe. Please try again.");
            return;
        }

        if (recipe == null) {
            presenter.presentError("Recipe not found.");
            return;
        }

        Set<String> availableNames = new HashSet<>();

        for (PantryItem item : pantryRepository.findAll()) {
            if (item.getQuantity().signum() > 0) {
                availableNames.add(
                        item.getName().trim().toLowerCase(Locale.ROOT));
            }
        }

        Set<String> requiredNames = new HashSet<>();
        List<String> missingIngredients = new ArrayList<>();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            String name = ingredient.getName();

            if (name == null || name.isBlank()) {
                continue;
            }

            String normalizedName =
                    name.trim().toLowerCase(Locale.ROOT);

            if (requiredNames.add(normalizedName)
                    && !availableNames.contains(normalizedName)) {
                missingIngredients.add(name.trim());
            }
        }

        if (requiredNames.isEmpty()) {
            presenter.presentError(
                    "Ingredient information is unavailable.");
            return;
        }

        presenter.presentSuccess(
                new GenerateShoppingListOutputData(
                        recipe.getName(), missingIngredients));
    }
}
