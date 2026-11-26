package interface_adapter.generate_shopping_list;

import entity.ShoppingListItem;
import use_case.generate_shopping_list.GenerateShoppingListOutputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListOutputData;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class GenerateShoppingListPresenter implements GenerateShoppingListOutputBoundary {
    private final GenerateShoppingListViewModel viewModel;

    public GenerateShoppingListPresenter(
            GenerateShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(
            GenerateShoppingListOutputData outputData) {

        List<String> rows = new ArrayList<>();
        boolean hasReviewItems = false;

        for (ShoppingListItem item : outputData.getItems()) {
            if (item.requiresReview()) {
                hasReviewItems = true;

                List<String> amounts = item.getRecipeAmounts()
                        .stream()
                        .map(amount -> amount.isBlank()
                                ? "not specified" : amount.trim())
                        .toList();

                rows.add(
                        "Check amount: " + item.getName()
                                + " (recipe: "
                                + String.join(" + ", amounts)
                                + ")");
            } else {
                rows.add(
                        item.getName() + " - buy "
                                + item.getMissingQuantity()
                                .orElseThrow());
            }
        }

        String message;

        if (rows.isEmpty()) {
            message =
                    "Pantry quantities cover all recipe requirements.";
        } else if (hasReviewItems) {
            message =
                    "Check the marked amounts against your pantry "
                            + "before shopping. Some quantities "
                            + "could not be compared.";
        } else {
            message = "Missing quantities are shown above.";
        }

        updateViewModel(() -> viewModel.setResult(
                outputData.getRecipeName(),
                rows,
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
