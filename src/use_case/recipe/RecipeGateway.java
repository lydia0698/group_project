package use_case.recipe;
import entity.Recipe;

public interface RecipeGateway {
    Recipe findById(String recipeId);
}
