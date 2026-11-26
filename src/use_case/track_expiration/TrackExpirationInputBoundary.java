package use_case.track_expiration;

import entity.StorageCondition;
import java.time.LocalDate;
import java.util.Optional;

public interface TrackExpirationInputBoundary {
    void checkExpiration();

    Optional<ShelfLifeEstimate.DateBasis> getDateBasis(
            String itemId,
            StorageCondition storageCondition
    );

    void updateStorageInfo(
            String itemId,
            StorageCondition storageCondition,
            LocalDate referenceDate,
            ShelfLifeEstimate.DateBasis dateBasis
    );
}
