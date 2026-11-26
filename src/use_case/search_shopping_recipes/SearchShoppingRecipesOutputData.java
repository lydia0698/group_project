package use_case.search_shopping_recipes;

import entity.RecipeSummary;

import java.util.List;

public class SearchShoppingRecipesOutputData {

    private final List<RecipeSummary> recipes;

    public SearchShoppingRecipesOutputData(
            List<RecipeSummary> recipes) {
        this.recipes = List.copyOf(recipes);
    }

    public List<RecipeSummary> getRecipes() {
        return recipes;
    }
}