package use_case.recipe;

import entity.RecipeSummary;

import java.util.List;

public interface RecipeSearchGateway {

    List<RecipeSummary> searchByName(String query);
}
