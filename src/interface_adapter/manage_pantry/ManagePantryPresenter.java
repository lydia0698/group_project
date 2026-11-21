
package interface_adapter.manage_pantry;

import use_case.manage_pantry.ManagePantryOutputBoundary;
import use_case.manage_pantry.ManagePantryOutputData;

public class ManagePantryPresenter
        implements ManagePantryOutputBoundary {

    private final ManagePantryViewModel viewModel;

    public ManagePantryPresenter(ManagePantryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(ManagePantryOutputData data) {
        viewModel.setItems(data.getItems());
        viewModel.setErrorMessage("");
        viewModel.setMessage(data.getMessage());
    }

    @Override
    public void presentError(String message) {
        viewModel.setMessage("");
        viewModel.setErrorMessage(message);
    }
}
