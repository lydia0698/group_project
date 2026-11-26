package use_case.search_shopping_recipes;

public interface SearchShoppingRecipesOutputBoundary {

    void presentSuccess(
            SearchShoppingRecipesOutputData outputData);

    void presentError(String errorMessage);
}
