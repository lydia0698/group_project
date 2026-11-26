package data_access;

import entity.Recipe;
import entity.RecipeIngredient;
import entity.RecipeSummary;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.recipe.RecipeGateway;
import use_case.recipe.RecipeSearchGateway;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TheMealDbGateway
        implements RecipeGateway, RecipeSearchGateway {

    private static final String LOOKUP_URL =
            "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

    private static final String SEARCH_URL =
            "https://www.themealdb.com/api/json/v1/1/search.php?s=";

    private final HttpClient client;

    public TheMealDbGateway() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build());
    }

    public TheMealDbGateway(HttpClient client) {
        this.client = client;
    }

    @Override
    public Recipe findById(String recipeId) {
        if (recipeId == null || recipeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe ID is required.");
        }

        String encodedId = URLEncoder.encode(
                recipeId.trim(), StandardCharsets.UTF_8);

        JSONArray meals =
                requestMeals(LOOKUP_URL + encodedId);

        if (meals == null || meals.isEmpty()) {
            return null;
        }

        try {
            JSONObject meal = meals.getJSONObject(0);

            String id = meal.getString("idMeal").trim();
            String name = meal.getString("strMeal").trim();

            if (id.isEmpty() || name.isEmpty()) {
                throw new IllegalStateException(
                        "Recipe details are incomplete.");
            }

            List<RecipeIngredient> ingredients =
                    new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                String ingredientName =
                        meal.optString(
                                "strIngredient" + i, "").trim();

                if (!ingredientName.isEmpty()) {
                    String amount =
                            meal.optString(
                                    "strMeasure" + i, "").trim();

                    ingredients.add(
                            new RecipeIngredient(
                                    ingredientName, amount));
                }
            }

            return new Recipe(id, name, ingredients);

        } catch (JSONException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve recipe information.",
                    exception);
        }
    }

    @Override
    public List<RecipeSummary> searchByName(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe name is required.");
        }

        String encodedQuery = URLEncoder.encode(
                query.trim(), StandardCharsets.UTF_8);

        JSONArray meals =
                requestMeals(SEARCH_URL + encodedQuery);

        if (meals == null || meals.isEmpty()) {
            return List.of();
        }

        List<RecipeSummary> recipes = new ArrayList<>();

        try {
            for (int i = 0; i < meals.length(); i++) {
                JSONObject meal = meals.getJSONObject(i);

                String id = meal.getString("idMeal").trim();
                String name = meal.getString("strMeal").trim();

                if (id.isBlank() || name.isBlank()) {
                    throw new IllegalStateException(
                            "Recipe search results are incomplete.");
                }

                recipes.add(new RecipeSummary(id, name));
            }

        } catch (JSONException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve recipe information.",
                    exception);
        }

        return List.copyOf(recipes);
    }

    private JSONArray requestMeals(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(
                            StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "TheMealDB returned HTTP "
                                + response.statusCode());
            }

            JSONObject root = new JSONObject(response.body());

            if (root.get("meals") == JSONObject.NULL) {
                return null;
            }

            return root.getJSONArray("meals");

        } catch (IOException | JSONException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve recipe information.",
                    exception);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Recipe request was interrupted.",
                    exception);
        }
    }
}
