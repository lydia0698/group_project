package use_case.track_expiration;

import entity.StorageCondition;
import java.util.OptionalInt;
import java.util.Optional;

public interface ShelfLifeDataAccessInterface {

    Optional<ShelfLifeEstimate> getShelfLifeEstimate(
            String ingredientName,
            StorageCondition storageCondition
    );

    OptionalInt getShelfLifeDays(
            String ingredientName,
            StorageCondition storageCondition
    );
}
