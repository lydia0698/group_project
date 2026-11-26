package use_case.generate_shopping_list;

public class GenerateShoppingListInputData {

    private final String recipeId;

    public GenerateShoppingListInputData(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }
}
