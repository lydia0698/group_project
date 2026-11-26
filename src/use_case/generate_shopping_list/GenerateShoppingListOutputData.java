package use_case.generate_shopping_list;

import java.util.List;

public class GenerateShoppingListOutputData {

    private final String recipeName;
    private final List<String> missingIngredients;

    public GenerateShoppingListOutputData(
            String recipeName,
            List<String> missingIngredients) {
        this.recipeName = recipeName;
        this.missingIngredients = List.copyOf(missingIngredients);
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }
}
