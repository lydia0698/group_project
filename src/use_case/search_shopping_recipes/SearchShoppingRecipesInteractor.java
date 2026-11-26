package use_case.search_shopping_recipes;

import entity.RecipeSummary;
import use_case.recipe.RecipeSearchGateway;

import java.util.List;

public class SearchShoppingRecipesInteractor
        implements SearchShoppingRecipesInputBoundary {

    private final RecipeSearchGateway recipeGateway;
    private final SearchShoppingRecipesOutputBoundary presenter;

    public SearchShoppingRecipesInteractor(
            RecipeSearchGateway recipeGateway,
            SearchShoppingRecipesOutputBoundary presenter) {

        this.recipeGateway = recipeGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchShoppingRecipesInputData inputData) {
        if (inputData == null || inputData.getQuery() == null
                || inputData.getQuery().isBlank()) {

            presenter.presentError("Please enter a recipe name.");
            return;
        }

        List<RecipeSummary> recipes;

        try {
            recipes = recipeGateway.searchByName(
                    inputData.getQuery().trim());
        } catch (IllegalStateException exception) {
            presenter.presentError(
                    "Unable to search recipes. Please try again.");
            return;
        }

        presenter.presentSuccess(
                new SearchShoppingRecipesOutputData(recipes));
    }
}