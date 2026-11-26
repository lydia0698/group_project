package use_case.track_expiration;

public interface TrackExpirationOutputBoundary {
    void presentExpirationResults(TrackExpirationOutputData outputData);
    void presentError(String message);
}
