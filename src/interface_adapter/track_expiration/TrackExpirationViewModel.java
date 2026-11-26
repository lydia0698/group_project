package interface_adapter.track_expiration;

import entity.PantryItem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class TrackExpirationViewModel {

    private List<PantryItem> expiredItems = new ArrayList<>();
    private List<PantryItem> expiringSoonItems = new ArrayList<>();
    private String errorMessage = "";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public List<PantryItem> getExpiredItems() {
        return List.copyOf(expiredItems);
    }

    public List<PantryItem> getExpiringSoonItems() {
        return List.copyOf(expiringSoonItems);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setExpiredItems(List<PantryItem> items) {
        this.expiredItems = new ArrayList<>(items);
        support.firePropertyChange(
                "expiredItems", null, this.expiredItems);
    }

    public void setExpiringSoonItems(List<PantryItem> items) {
        this.expiringSoonItems = new ArrayList<>(items);
        support.firePropertyChange(
                "expiringSoonItems", null, this.expiringSoonItems);
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
        support.firePropertyChange(
                "errorMessage", null, message);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

}
