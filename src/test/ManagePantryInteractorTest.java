
package test;

import data_access.InMemoryPantryRepository;
import entity.PantryItem;
import use_case.manage_pantry.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ManagePantryInteractorTest {

    public static void main(String[] args) {

        InMemoryPantryRepository repository =
                new InMemoryPantryRepository();

        ManagePantryOutputBoundary presenter =
                new ManagePantryOutputBoundary() {

                    @Override
                    public void presentSuccess(
                            ManagePantryOutputData data) {
                        System.out.println(
                                "SUCCESS: " + data.getMessage());
                        System.out.println(
                                "Inventory size: " + data.getItems().size());
                    }

                    @Override
                    public void presentError(String message) {
                        System.out.println("ERROR: " + message);
                    }
                };

        ManagePantryInputBoundary interactor =
                new ManagePantryInteractor(repository, presenter);

        // Test 1: Add
        ManagePantryInputData milk =
                new ManagePantryInputData(
                        "001", "Milk",
                        new BigDecimal("500"),
                        "mL",
                        LocalDate.of(2026, 11, 25)
                );

        interactor.addItem(milk);

        // Test 2: Duplicate ID
        interactor.addItem(milk);

        // Test 3: Update
        ManagePantryInputData updatedMilk =
                new ManagePantryInputData(
                        "001", "Milk",
                        new BigDecimal("1000"),
                        "mL",
                        LocalDate.of(2026, 11, 25)
                );

        interactor.updateItem(updatedMilk);

        // Test 4: Invalid quantity
        ManagePantryInputData invalid =
                new ManagePantryInputData(
                        "002", "Eggs",
                        new BigDecimal("-5"),
                        "pcs",
                        null
                );

        interactor.addItem(invalid);

        // Test 5: List
        interactor.listItems();

        // Test 6: Remove
        interactor.removeItem("001");

        // Test 7: Remove nonexistent item
        interactor.removeItem("999");
    }
}
