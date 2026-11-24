package interface_adapter.generate_shopping_list;

import use_case.generate_shopping_list.GenerateShoppingListOutputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListOutputData;

import javax.swing.SwingUtilities;

public class GenerateShoppingListPresenter implements GenerateShoppingListOutputBoundary {
    private final GenerateShoppingListViewModel viewModel;

    public GenerateShoppingListPresenter(
            GenerateShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(GenerateShoppingListOutputData outputData) {
        String message;

        if (outputData.getMissingIngredients().isEmpty()) {
            message = "All ingredients are recorded in your pantry. "
                    + "Check their quantities before cooking.";
        } else {
            message = "Check your pantry quantities before shopping.";
        }

        updateViewModel(() -> viewModel.setResult(
                outputData.getRecipeName(),
                outputData.getMissingIngredients(),
                message));
    }

    @Override
    public void presentError(String errorMessage) {
        updateViewModel(() -> viewModel.setError(errorMessage));
    }

    private void updateViewModel(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            SwingUtilities.invokeLater(update);
        }
    }
}
