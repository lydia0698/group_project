
package test;

import data_access.InMemoryPantryRepository;
import entity.PantryItem;
import use_case.pantry.PantryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PantryRepositoryTest {

    public static void main(String[] args) {

        PantryRepository repository =
                new InMemoryPantryRepository();

        // 1. Add an item
        PantryItem milk = new PantryItem(
                "001",
                "Milk",
                new BigDecimal("500"),
                "mL",
                LocalDate.of(2026, 11, 25)
        );

        repository.save(milk);
        System.out.println("After adding: "
                + repository.findAll().size());

        // 2. Find an item
        PantryItem found = repository.findById("001");
        System.out.println("Found: " + found.getName());

        // 3. Update an item
        PantryItem updatedMilk = new PantryItem(
                "001",
                "Milk",
                new BigDecimal("1000"),
                "mL",
                LocalDate.of(2026, 11, 25)
        );

        repository.save(updatedMilk);
        System.out.println("Updated quantity: "
                + repository.findById("001").getQuantity());

        // 4. Delete an item
        repository.deleteById("001");
        System.out.println("After deleting: "
                + repository.findAll().size());
    }
}

