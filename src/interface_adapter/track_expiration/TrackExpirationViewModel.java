package interface_adapter.track_expiration;

import use_case.track_expiration.ExpirationResult;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class TrackExpirationViewModel {
    private List<ExpirationResult> results =
            new ArrayList<>();

    private String errorMessage = "";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public List<ExpirationResult> getResults() {
        return new ArrayList<>(results);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setResults(List<ExpirationResult> results) {
        this.results = new ArrayList<>(results);

        support.firePropertyChange(
                "results",
                null,
                this.results
        );
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;

        support.firePropertyChange(
                "errorMessage",
                null,
                message
        );
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {

        support.addPropertyChangeListener(listener);
    }
}
