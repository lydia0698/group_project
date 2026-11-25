package entity;

import java.util.List;

public class Recipe {

    private final String id;
    private final String name;
    private final List<RecipeIngredient> ingredients;

    public Recipe(
            String id,
            String name,
            List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }
}
