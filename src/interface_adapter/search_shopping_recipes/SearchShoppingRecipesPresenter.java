package interface_adapter.search_shopping_recipes;

import use_case.search_shopping_recipes.SearchShoppingRecipesOutputBoundary;
import use_case.search_shopping_recipes.SearchShoppingRecipesOutputData;

import javax.swing.SwingUtilities;

public class SearchShoppingRecipesPresenter
        implements SearchShoppingRecipesOutputBoundary {

    private final SearchShoppingRecipesViewModel viewModel;

    public SearchShoppingRecipesPresenter(
            SearchShoppingRecipesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(
            SearchShoppingRecipesOutputData outputData) {

        int count = outputData.getRecipes().size();

        String message = count == 0
                ? "No recipes found. Try another recipe name."
                : count + " recipes found. Select one "
                + "to generate a shopping list.";

        updateViewModel(() -> viewModel.setResults(
                outputData.getRecipes(), message));
    }

    @Override
    public void presentError(String errorMessage) {
        updateViewModel(
                () -> viewModel.setError(errorMessage));
    }

    private void updateViewModel(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            SwingUtilities.invokeLater(update);
        }
    }
}
