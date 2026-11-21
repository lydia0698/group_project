
package use_case.manage_pantry;

import entity.PantryItem;
import use_case.pantry.PantryRepository;

import java.util.List;

public class ManagePantryInteractor implements ManagePantryInputBoundary {

    private final PantryRepository repository;
    private final ManagePantryOutputBoundary presenter;

    public ManagePantryInteractor(
            PantryRepository repository,
            ManagePantryOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void addItem(ManagePantryInputData inputData) {
        try {
            if (repository.findById(inputData.getId()) != null) {
                presenter.presentError("Item ID already exists");
                return;
            }

            PantryItem item = new PantryItem(
                    inputData.getId(),
                    inputData.getName(),
                    inputData.getQuantity(),
                    inputData.getUnit(),
                    inputData.getExpirationDate()
            );

            repository.save(item);
            presentInventory("Item added successfully");

        } catch (IllegalArgumentException e) {
            presenter.presentError(e.getMessage());
        }
    }

    @Override
    public void updateItem(ManagePantryInputData inputData) {
        try {
            PantryItem existing =
                    repository.findById(inputData.getId());

            if (existing == null) {
                presenter.presentError("Item not found");
                return;
            }

            PantryItem updated = new PantryItem(
                    existing.getId(),
                    inputData.getName(),
                    inputData.getQuantity(),
                    inputData.getUnit(),
                    inputData.getExpirationDate()
            );

            repository.save(updated);
            presentInventory("Item updated successfully");

        } catch (IllegalArgumentException e) {
            presenter.presentError(e.getMessage());
        }
    }

    @Override
    public void removeItem(String itemId) {
        if (repository.findById(itemId) == null) {
            presenter.presentError("Item not found");
            return;
        }

        repository.deleteById(itemId);
        presentInventory("Item removed successfully");
    }

    @Override
    public void listItems() {
        presentInventory("Current pantry inventory");
    }

    private void presentInventory(String message) {
        List<PantryItem> items = repository.findAll();

        ManagePantryOutputData outputData =
                new ManagePantryOutputData(message, items);

        presenter.presentSuccess(outputData);
    }
}
