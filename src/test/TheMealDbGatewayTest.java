package test;

import data_access.TheMealDbGateway;
import entity.Recipe;
import entity.RecipeIngredient;
import use_case.recipe.RecipeGateway;

public class TheMealDbGatewayTest {
    public static void main(String[] args) {
        RecipeGateway gateway = new TheMealDbGateway();

        Recipe recipe = gateway.findById("53016");

        if (recipe == null
                || !"53016".equals(recipe.getId())
                || recipe.getIngredients().isEmpty()) {
            throw new AssertionError(
                    "Expected the selected recipe and its ingredients.");
        }

        System.out.println("Recipe: " + recipe.getName());

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            System.out.println(
                    ingredient.getName() + " - " + ingredient.getRecipeAmount());
        }

        System.out.println("PASS: recipe lookup");

        Recipe missingRecipe = gateway.findById("99999999");

        if (missingRecipe != null) {
            throw new AssertionError(
                    "Expected no recipe for an unknown ID.");
        }

        System.out.println("PASS: missing recipe");
    }
}
