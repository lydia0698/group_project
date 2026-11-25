package test;

import data_access.InMemoryPantryRepository;
import entity.PantryItem;
import entity.Recipe;
import entity.RecipeIngredient;
import use_case.generate_shopping_list.GenerateShoppingListInputData;
import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.generate_shopping_list.GenerateShoppingListOutputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListOutputData;
import use_case.recipe.RecipeGateway;

import java.math.BigDecimal;
import java.util.List;

public class GenerateShoppingListInteractorTest {
    public static void main(String[] args) {
        InMemoryPantryRepository pantry = new InMemoryPantryRepository();

        pantry.save(new PantryItem(
                "1", " chicken ", BigDecimal.ONE, "kg", null));
        pantry.save(new PantryItem(
                "2", "Soy Sauce", BigDecimal.ZERO, "mL", null));

        Recipe recipe = new Recipe(
                "test-1", "Chicken with Soy Sauce",
                List.of(
                        new RecipeIngredient("Chicken", "500 g"),
                        new RecipeIngredient("Soy Sauce", "2 tbsp"),
                        new RecipeIngredient(" soy sauce ", "1 tbsp")));

        RecipeGateway gateway = recipeId -> recipe;
        TestPresenter presenter = new TestPresenter();

        GenerateShoppingListInteractor interactor =
                new GenerateShoppingListInteractor(
                        pantry, gateway, presenter);

        interactor.execute(new GenerateShoppingListInputData("test-1"));

        if (presenter.errorMessage != null
                || presenter.outputData == null
                || !List.of("Soy Sauce").equals(
                presenter.outputData.getMissingIngredients())) {
            throw new AssertionError("Expected only Soy Sauce to be missing.");
        }

        System.out.println("PASS: missing ingredients");

        pantry.save(new PantryItem(
                "2", "Soy Sauce", new BigDecimal("100"), "mL", null));

        presenter.outputData = null;
        presenter.errorMessage = null;

        interactor.execute(new GenerateShoppingListInputData("test-1"));

        if (presenter.errorMessage != null
                || presenter.outputData == null
                || !presenter.outputData.getMissingIngredients().isEmpty()) {
            throw new AssertionError("Expected no missing ingredients.");
        }

        System.out.println("PASS: updated pantry");

        presenter.outputData = null;
        presenter.errorMessage = null;

        interactor.execute(new GenerateShoppingListInputData(" "));

        if (presenter.errorMessage == null || presenter.outputData != null) {
            throw new AssertionError("Expected an error for an empty recipe ID.");
        }

        System.out.println("PASS: empty recipe ID");
    }

    private static class TestPresenter
            implements GenerateShoppingListOutputBoundary {

        private GenerateShoppingListOutputData outputData;
        private String errorMessage;

        @Override
        public void presentSuccess(GenerateShoppingListOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
