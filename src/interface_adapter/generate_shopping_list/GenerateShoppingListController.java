package interface_adapter.generate_shopping_list;

import use_case.generate_shopping_list.GenerateShoppingListInputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListInputData;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;

public class GenerateShoppingListController {
    private final GenerateShoppingListInputBoundary interactor;
    private final GenerateShoppingListViewModel viewModel;

    public GenerateShoppingListController(
            GenerateShoppingListInputBoundary interactor,
            GenerateShoppingListViewModel viewModel) {
        this.interactor = interactor;
        this.viewModel = viewModel;
    }

    public void generate(String recipeId) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> generate(recipeId));
            return;
        }

        if (viewModel.isLoading()) {
            return;
        }

        viewModel.startLoading();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                interactor.execute(
                        new GenerateShoppingListInputData(recipeId));
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    viewModel.setError(
                            "Generating the shopping list was interrupted.");
                } catch (ExecutionException exception) {
                    viewModel.setError(
                            "Unable to generate the shopping list. Please try again.");
                }
            }
        };

        worker.execute();
    }
}
