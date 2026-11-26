package interface_adapter.search_shopping_recipes;

import entity.RecipeSummary;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SearchShoppingRecipesViewModel {

    private List<RecipeSummary> recipes = List.of();
    private String message = "Search for a recipe by name.";
    private String errorMessage = "";
    private boolean loading;

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public List<RecipeSummary> getRecipes() {
        return recipes;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isLoading() {
        return loading;
    }

    public void startLoading() {
        loading = true;
        recipes = List.of();
        message = "Searching recipes...";
        errorMessage = "";

        support.firePropertyChange("state", null, null);
    }

    public void setResults(
            List<RecipeSummary> recipes,
            String message) {

        loading = false;
        this.recipes = List.copyOf(recipes);
        this.message = message;
        errorMessage = "";

        support.firePropertyChange("state", null, null);
    }

    public void setError(String errorMessage) {
        loading = false;
        recipes = List.of();
        message = "";
        this.errorMessage = errorMessage;

        support.firePropertyChange("state", null, null);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
