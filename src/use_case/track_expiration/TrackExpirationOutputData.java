package use_case.track_expiration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackExpirationOutputData {
    private final List<ExpirationResult> results;

    public TrackExpirationOutputData(
            List<ExpirationResult> results) {

        this.results = Collections.unmodifiableList(
                new ArrayList<>(results));
    }

    public List<ExpirationResult> getResults() {
        return results;
    }
}
