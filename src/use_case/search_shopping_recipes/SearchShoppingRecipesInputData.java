package use_case.search_shopping_recipes;

public class SearchShoppingRecipesInputData {

    private final String query;

    public SearchShoppingRecipesInputData(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}