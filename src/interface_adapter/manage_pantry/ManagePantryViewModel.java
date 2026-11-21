
package interface_adapter.manage_pantry;

import entity.PantryItem;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ManagePantryViewModel {

    private List<PantryItem> items = new ArrayList<>();
    private String message = "";
    private String errorMessage = "";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public List<PantryItem> getItems() {
        return List.copyOf(items);
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setItems(List<PantryItem> items) {
        this.items = new ArrayList<>(items);
        support.firePropertyChange("items", null, this.items);
    }

    public void setMessage(String message) {
        this.message = message;
        support.firePropertyChange("message", null, message);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        support.firePropertyChange(
                "errorMessage", null, errorMessage);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
