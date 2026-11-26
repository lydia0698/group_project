package interface_adapter.search_shopping_recipes;

import use_case.search_shopping_recipes.SearchShoppingRecipesInputBoundary;
import use_case.search_shopping_recipes.SearchShoppingRecipesInputData;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;

public class SearchShoppingRecipesController {

    private final SearchShoppingRecipesInputBoundary interactor;
    private final SearchShoppingRecipesViewModel viewModel;

    public SearchShoppingRecipesController(
            SearchShoppingRecipesInputBoundary interactor,
            SearchShoppingRecipesViewModel viewModel) {

        this.interactor = interactor;
        this.viewModel = viewModel;
    }

    public void search(String query) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> search(query));
            return;
        }

        if (viewModel.isLoading()) {
            return;
        }

        viewModel.startLoading();

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                interactor.execute(
                        new SearchShoppingRecipesInputData(query));
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();

                    viewModel.setError(
                            "Recipe search was interrupted.");

                } catch (ExecutionException exception) {
                    viewModel.setError(
                            "Unable to search recipes. "
                                    + "Please try again.");
                }
            }
        }.execute();
    }
}
