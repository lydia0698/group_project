package interface_adapter.generate_shopping_list;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class GenerateShoppingListViewModel {
    private String recipeName = "";
    private List<String> missingIngredients = List.of();
    private String message = "";
    private String errorMessage = "";
    private boolean loading = false;

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public String getRecipeName() {
        return recipeName;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
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
        recipeName = "";
        missingIngredients = List.of();
        message = "Loading recipe...";
        errorMessage = "";

        support.firePropertyChange("state", null, null);
    }

    public void setResult(
            String recipeName,
            List<String> missingIngredients,
            String message) {
        loading = false;
        this.recipeName = recipeName;
        this.missingIngredients = List.copyOf(missingIngredients);
        this.message = message;
        errorMessage = "";

        support.firePropertyChange("state", null, null);
    }

    public void setError(String errorMessage) {
        loading = false;
        recipeName = "";
        missingIngredients = List.of();
        message = "";
        this.errorMessage = errorMessage;

        support.firePropertyChange("state", null, null);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
