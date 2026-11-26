package test;

import data_access.InMemoryPantryRepository;
import entity.PantryItem;
import entity.Recipe;
import entity.RecipeIngredient;
import entity.ShoppingListItem;
import use_case.generate_shopping_list.GenerateShoppingListInputData;
import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.generate_shopping_list.GenerateShoppingListOutputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListOutputData;
import use_case.recipe.RecipeGateway;

import java.math.BigDecimal;
import java.util.List;

public class GenerateShoppingListInteractorTest {

    private static int checks;

    public static void main(String[] args) {
        InMemoryPantryRepository pantry =
                new InMemoryPantryRepository();

        save(pantry, "1", "Chicken", "100", "g");
        save(pantry, "2", "Milk", "10", "ml");
        save(pantry, "3", "Salt", "0", "g");

        GenerateShoppingListOutputData result = generate(
                pantry,
                ingredient("Chicken", "500 g"),
                ingredient("Milk", "200 ml"),
                ingredient("Salt", "5 g"));

        expectShortage(result, "Chicken", "400 g");
        expectShortage(result, "Milk", "190 ml");
        expectShortage(result, "Salt", "5 g");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Flour", "400", "g");
        save(pantry, "2", " flour ", "0.3", "kg");

        expectShortage(
                generate(pantry, ingredient("FLOUR", "1 kg")),
                "FLOUR", "300 g");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Soy Sauce", "2", "tbsp");

        result = generate(
                pantry,
                ingredient("Soy Sauce", "2 tbsp"),
                ingredient(" soy sauce ", "1 tablespoon"));

        expect(result.getItems().size() == 1,
                "Duplicate ingredient grouping");
        expectShortage(result, "Soy Sauce", "1 tbsp");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Chicken", "1", "kg");

        expect(generate(pantry, ingredient("Chicken", "500 g"))
                        .getItems().isEmpty(),
                "Sufficient stock");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Milk", "1", "cup");

        expectReview(
                generate(pantry, ingredient("Milk", "200 ml")),
                "Milk", List.of("200 ml"));

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Flour", "100", "g");
        save(pantry, "2", "Flour", "1", "cup");

        expectReview(
                generate(pantry, ingredient("Flour", "200 g")),
                "Flour", List.of("200 g"));

        save(pantry, "1", "Flour", "300", "g");

        expect(generate(pantry, ingredient("Flour", "200 g"))
                        .getItems().isEmpty(),
                "Sufficient comparable stock with extra units");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Salt", "100", "g");

        expectReview(
                generate(pantry, ingredient("Salt", "to taste")),
                "Salt", List.of("to taste"));

        expectReview(
                generate(pantry, ingredient("Rice", "1/3 cup")),
                "Rice", List.of("1/3 cup"));

        expectReview(
                generate(pantry,
                        ingredient("Vegetables", "1 (12 oz.)")),
                "Vegetables", List.of("1 (12 oz.)"));

        expectReview(
                generate(pantry, ingredient("Pepper", null)),
                "Pepper", List.of(""));

        result = generate(
                pantry,
                ingredient("Salt", "200 g"),
                ingredient(" salt ", "to taste"));

        expectReview(
                result, "Salt", List.of("200 g", "to taste"));

        expect(result.getItems().size() == 1,
                "Unknown duplicate handled as one review");

        result = generate(
                pantry,
                ingredient("Sugar", "1 cup"),
                ingredient("Sugar", "20 g"));

        expectReview(
                result, "Sugar", List.of("1 cup", "20 g"));

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Egg", "0", "kg");

        expectShortage(
                generate(pantry, ingredient("Egg", "2")),
                "Egg", "2 pcs");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Rice", "0.5", "cups");

        expectShortage(
                generate(pantry, ingredient("Rice", "1 1/2 cups")),
                "Rice", "1 cup");

        pantry = new InMemoryPantryRepository();
        save(pantry, "1", "Egg", "1", "pcs");
        save(pantry, "2", "Egg", "1", "piece");

        expectShortage(
                generate(
                        pantry,
                        ingredient("Egg", "2"),
                        ingredient("egg", "2 pcs")),
                "Egg", "2 pcs");

        expectShortage(
                generate(
                        new InMemoryPantryRepository(),
                        ingredient("Flour", "500 g")),
                "Flour", "500 g");

        checkPantryUpdate();
        checkFailures();

        System.out.println(
                "PASS: " + checks + " quantity and error checks");
    }

    private static void checkPantryUpdate() {
        InMemoryPantryRepository pantry =
                new InMemoryPantryRepository();

        save(pantry, "1", "Chicken", "100", "g");

        Recipe recipe = new Recipe(
                "id", "Recipe",
                List.of(ingredient("Chicken", "500 g")));

        TestPresenter presenter = new TestPresenter();

        GenerateShoppingListInteractor interactor =
                new GenerateShoppingListInteractor(
                        pantry, id -> recipe, presenter);

        interactor.execute(
                new GenerateShoppingListInputData("id"));

        expectShortage(
                presenter.outputData, "Chicken", "400 g");

        save(pantry, "1", "Chicken", "1", "kg");
        presenter.reset();

        interactor.execute(
                new GenerateShoppingListInputData("id"));

        expect(presenter.outputData.getItems().isEmpty(),
                "Latest UC1 inventory is read on each execution");
    }

    private static void checkFailures() {
        InMemoryPantryRepository pantry =
                new InMemoryPantryRepository();

        TestPresenter presenter = new TestPresenter();

        RecipeGateway gateway = id -> {
            throw new AssertionError(
                    "Invalid input must not call the API");
        };

        GenerateShoppingListInteractor interactor =
                new GenerateShoppingListInteractor(
                        pantry, gateway, presenter);

        GenerateShoppingListInputData[] invalidInputs = {
                null,
                new GenerateShoppingListInputData(null),
                new GenerateShoppingListInputData(" ")
        };

        for (GenerateShoppingListInputData input : invalidInputs) {
            presenter.reset();
            interactor.execute(input);

            expect(
                    presenter.errorMessage != null
                            && presenter.outputData == null,
                    "Invalid input error");
        }

        presenter.reset();

        new GenerateShoppingListInteractor(
                pantry, id -> null, presenter)
                .execute(new GenerateShoppingListInputData("id"));

        expect(
                presenter.errorMessage != null
                        && presenter.outputData == null,
                "Unknown recipe error");

        presenter.reset();

        new GenerateShoppingListInteractor(
                pantry,
                id -> {
                    throw new IllegalStateException(
                            "API unavailable");
                },
                presenter
        ).execute(new GenerateShoppingListInputData("id"));

        expect(
                presenter.errorMessage != null
                        && presenter.outputData == null,
                "API failure error");

        presenter.reset();

        Recipe empty = new Recipe(
                "id", "Empty",
                List.of(
                        ingredient(" ", "1 g"),
                        ingredient(null, "1 g")));

        new GenerateShoppingListInteractor(
                pantry, id -> empty, presenter)
                .execute(new GenerateShoppingListInputData("id"));

        expect(
                presenter.errorMessage != null
                        && presenter.outputData == null,
                "Missing ingredient information error");

        presenter.reset();

        new GenerateShoppingListInteractor(
                pantry,
                id -> {
                    expect(id.equals("id"),
                            "Recipe ID is trimmed");

                    return new Recipe(
                            "id", "Recipe",
                            List.of(ingredient("Salt", "1 g")));
                },
                presenter
        ).execute(new GenerateShoppingListInputData(" id "));

        expect(
                presenter.outputData != null
                        && presenter.errorMessage == null,
                "Valid trimmed recipe ID");
    }

    private static GenerateShoppingListOutputData generate(
            InMemoryPantryRepository pantry,
            RecipeIngredient... ingredients) {

        Recipe recipe = new Recipe(
                "id", "Recipe", List.of(ingredients));

        TestPresenter presenter = new TestPresenter();

        new GenerateShoppingListInteractor(
                pantry, id -> recipe, presenter)
                .execute(new GenerateShoppingListInputData("id"));

        if (presenter.outputData == null
                || presenter.errorMessage != null) {
            throw new AssertionError(
                    "Expected a successful result");
        }

        return presenter.outputData;
    }

    private static RecipeIngredient ingredient(
            String name, String amount) {
        return new RecipeIngredient(name, amount);
    }

    private static void save(
            InMemoryPantryRepository pantry,
            String id,
            String name,
            String amount,
            String unit) {

        pantry.save(new PantryItem(
                id, name, new BigDecimal(amount), unit, null));
    }

    private static void expectShortage(
            GenerateShoppingListOutputData result,
            String name,
            String quantity) {

        ShoppingListItem item = result.getItems().stream()
                .filter(entry -> entry.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Missing entry: " + name));

        expect(
                !item.requiresReview()
                        && item.getMissingQuantity()
                        .orElseThrow()
                        .toString()
                        .equals(quantity),
                name + " should be missing " + quantity);
    }

    private static void expectReview(
            GenerateShoppingListOutputData result,
            String name,
            List<String> recipeAmounts) {

        ShoppingListItem item = result.getItems().stream()
                .filter(entry -> entry.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Missing review: " + name));

        expect(
                item.requiresReview()
                        && item.getMissingQuantity().isEmpty()
                        && item.getRecipeAmounts()
                        .equals(recipeAmounts),
                name + " requires review and must preserve raw amounts");
    }

    private static void expect(
            boolean condition, String message) {

        if (!condition) {
            throw new AssertionError(message);
        }

        checks++;
    }

    private static class TestPresenter
            implements GenerateShoppingListOutputBoundary {

        private GenerateShoppingListOutputData outputData;
        private String errorMessage;

        private void reset() {
            outputData = null;
            errorMessage = null;
        }

        @Override
        public void presentSuccess(
                GenerateShoppingListOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}