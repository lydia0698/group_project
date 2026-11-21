
package interface_adapter.manage_pantry;

import use_case.manage_pantry.ManagePantryInputBoundary;
import use_case.manage_pantry.ManagePantryInputData;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ManagePantryController {

    private final ManagePantryInputBoundary interactor;

    public ManagePantryController(
            ManagePantryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addItem(
            String id,
            String name,
            BigDecimal quantity,
            String unit,
            LocalDate expirationDate) {

        ManagePantryInputData inputData =
                new ManagePantryInputData(
                        id,
                        name,
                        quantity,
                        unit,
                        expirationDate
                );

        interactor.addItem(inputData);
    }

    public void updateItem(
            String id,
            String name,
            BigDecimal quantity,
            String unit,
            LocalDate expirationDate) {

        ManagePantryInputData inputData =
                new ManagePantryInputData(
                        id,
                        name,
                        quantity,
                        unit,
                        expirationDate
                );

        interactor.updateItem(inputData);
    }

    public void removeItem(String itemId) {
        interactor.removeItem(itemId);
    }

    public void listItems() {
        interactor.listItems();
    }
}
