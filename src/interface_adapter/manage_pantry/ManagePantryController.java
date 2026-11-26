
package interface_adapter.manage_pantry;

import use_case.manage_pantry.ManagePantryInputBoundary;
import use_case.manage_pantry.ManagePantryInputData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagePantryController {

    private final ManagePantryInputBoundary interactor;

    private static final Pattern QUANTITY_PATTERN =
            Pattern.compile("^(\\d+(?:\\.\\d+)?)\\s*([a-zA-Z]*)$");

    public ManagePantryController(
            ManagePantryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addItem(
            String name,
            String quantityText,
            LocalDate expirationDate) {

        String id = UUID.randomUUID().toString();

        ManagePantryInputData inputData =
                createInputData(id, name, quantityText, expirationDate);

        interactor.addItem(inputData);
    }

    public void updateItem(
            String id,
            String name,
            String quantityText,
            LocalDate expirationDate) {

        ManagePantryInputData inputData =
                createInputData(id, name, quantityText, expirationDate);

        interactor.updateItem(inputData);
    }

    private ManagePantryInputData createInputData(
            String id,
            String name,
            String quantityText,
            LocalDate expirationDate) {

        Matcher matcher =
                QUANTITY_PATTERN.matcher(quantityText.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid quantity. Examples: 500ml, 200g, 1kg, 3"
            );
        }

        BigDecimal quantity = new BigDecimal(matcher.group(1));

        String unit = matcher.group(2).toLowerCase(Locale.ROOT);

        if (unit.isEmpty()) {
            unit = "pcs";
        }

        return new ManagePantryInputData(
                id,
                name,
                quantity,
                unit,
                expirationDate
        );
    }

    public void removeItem(String itemId) {
        interactor.removeItem(itemId);
    }

    public void listItems() {
        interactor.listItems();
    }
}
