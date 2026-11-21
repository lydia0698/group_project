
package use_case.manage_pantry;

import entity.PantryItem;
import java.util.List;

public class ManagePantryOutputData {

    private final String message;
    private final List<PantryItem> items;

    public ManagePantryOutputData(String message, List<PantryItem> items) {
        this.message = message;
        this.items = List.copyOf(items);
    }

    public String getMessage() {
        return message;
    }

    public List<PantryItem> getItems() {
        return items;
    }
}
