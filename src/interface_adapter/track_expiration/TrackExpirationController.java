package interface_adapter.track_expiration;

import use_case.track_expiration.TrackExpirationInputBoundary;

public class TrackExpirationController {

    private final TrackExpirationInputBoundary interactor;

    public TrackExpirationController(TrackExpirationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void checkExpiration() {
        interactor.checkExpiration();
    }

}
