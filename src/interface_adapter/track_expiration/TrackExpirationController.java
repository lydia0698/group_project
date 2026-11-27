package interface_adapter.track_expiration;

import entity.StorageCondition;
import use_case.track_expiration.ShelfLifeEstimate;
import use_case.track_expiration.TrackExpirationInputBoundary;

import java.time.LocalDate;
import java.util.Optional;

public class TrackExpirationController {

    private final TrackExpirationInputBoundary interactor;

    public TrackExpirationController(TrackExpirationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void checkExpiration() {
        interactor.checkExpiration();
    }

    public Optional<ShelfLifeEstimate.DateBasis> getDateBasis(
            String itemId,
            StorageCondition storageCondition) {

        return interactor.getDateBasis(
                itemId,
                storageCondition
        );
    }

    public void updateStorageInfo(
            String itemId,
            StorageCondition storageCondition,
            LocalDate referenceDate,
            ShelfLifeEstimate.DateBasis dateBasis) {

        interactor.updateStorageInfo(
                itemId,
                storageCondition,
                referenceDate,
                dateBasis
        );
    }
}
