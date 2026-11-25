package entity;

public class RecipeIngredient {

    private final String name;
    private final String recipeAmount;

    public RecipeIngredient(String name, String recipeAmount) {
        this.name = name;
        this.recipeAmount = recipeAmount;
    }

    public String getName() {
        return name;
    }

    public String getRecipeAmount() {
        return recipeAmount;
    }
}