package use_case.generate_shopping_list;

import entity.ShoppingListItem;

import java.util.List;

public class GenerateShoppingListOutputData {

    private final String recipeName;
    private final List<ShoppingListItem> items;

    public GenerateShoppingListOutputData(
            String recipeName,
            List<ShoppingListItem> items) {

        this.recipeName = recipeName;
        this.items = List.copyOf(items);
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public List<String> getMissingIngredients() {
        return items.stream()
                .filter(item -> !item.requiresReview())
                .map(ShoppingListItem::getName)
                .toList();
    }
}
