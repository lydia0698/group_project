package use_case.track_expiration;

import entity.PantryItem;
import entity.StorageCondition;
import use_case.pantry.PantryRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackExpirationInteractor
        implements TrackExpirationInputBoundary {

    private final PantryRepository repository;
    private final TrackExpirationOutputBoundary presenter;
    private final ShelfLifeDataAccessInterface shelfLifeDataAccess;
    private final Clock clock;

    public TrackExpirationInteractor(
            PantryRepository repository,
            TrackExpirationOutputBoundary presenter,
            ShelfLifeDataAccessInterface shelfLifeDataAccess) {

        this(repository, presenter, shelfLifeDataAccess,
                Clock.systemDefaultZone());
    }

    public TrackExpirationInteractor(
            PantryRepository repository,
            TrackExpirationOutputBoundary presenter,
            ShelfLifeDataAccessInterface shelfLifeDataAccess,
            Clock clock) {

        this.repository = repository;
        this.presenter = presenter;
        this.shelfLifeDataAccess = shelfLifeDataAccess;
        this.clock = clock;
    }

    // Find out whether the date should be the purchase
    // date or the storage start date.
    @Override
    public Optional<ShelfLifeEstimate.DateBasis> getDateBasis(
            String itemId,
            StorageCondition storageCondition) {

        PantryItem item = repository.findById(itemId);

        if (item == null || storageCondition == null) {
            return Optional.empty();
        }

        Optional<ShelfLifeEstimate> estimate =
                shelfLifeDataAccess.getShelfLifeEstimate(
                        item.getName(),
                        storageCondition
                );

        return estimate.map(ShelfLifeEstimate::getDateBasis);
    }

    // Save the date in the correct field.
    @Override
    public void updateStorageInfo(
            String itemId,
            StorageCondition storageCondition,
            LocalDate referenceDate,
            ShelfLifeEstimate.DateBasis dateBasis) {

        PantryItem item = repository.findById(itemId);

        if (item == null) {
            presenter.presentError("Ingredient not found.");
            return;
        }

        if (storageCondition == null
                || referenceDate == null
                || dateBasis == null) {

            presenter.presentError(
                    "Please provide all storage information.");
            return;
        }

        if (referenceDate.isAfter(LocalDate.now(clock))) {
            presenter.presentError(
                    "The date cannot be in the future.");
            return;
        }

        // Check that the chosen date basis still matches
        // the FoodKeeper guidance.
        Optional<ShelfLifeEstimate.DateBasis> requiredBasis =
                getDateBasis(itemId, storageCondition);

        if (!requiredBasis.isPresent()) {
            presenter.presentError(
                    "No FoodKeeper estimate is available "
                            + "for this ingredient and storage condition.");
            return;
        }

        if (requiredBasis.get() != dateBasis) {
            presenter.presentError(
                    "The date type does not match "
                            + "the FoodKeeper guidance.");
            return;
        }

        item.setStorageCondition(storageCondition);

        if (dateBasis == ShelfLifeEstimate.DateBasis.PURCHASE_DATE) {
            item.setPurchaseDate(referenceDate);
            item.setStorageStartDate(null);
        } else {
            item.setStorageStartDate(referenceDate);
            item.setPurchaseDate(null);
        }

        repository.save(item);
        checkExpiration();
    }

    @Override
    public void checkExpiration() {

        List<ExpirationResult> results = new ArrayList<>();
        LocalDate today = LocalDate.now(clock);

        try {
            for (PantryItem item : repository.findAll()) {

                LocalDate expirationDate =
                        item.getExpirationDate();

                boolean estimated = false;

                // Only estimate when the entered date is blank.
                if (expirationDate == null
                        && item.getStorageCondition() != null) {

                    Optional<ShelfLifeEstimate> estimate =
                            shelfLifeDataAccess.getShelfLifeEstimate(
                                    item.getName(),
                                    item.getStorageCondition()
                            );

                    if (estimate.isPresent()) {

                        ShelfLifeEstimate shelfLife =
                                estimate.get();

                        LocalDate referenceDate;

                        if (shelfLife.getDateBasis()
                                == ShelfLifeEstimate.DateBasis.PURCHASE_DATE) {

                            referenceDate = item.getPurchaseDate();

                        } else {

                            referenceDate = item.getStorageStartDate();
                        }

                        if (referenceDate != null) {

                            expirationDate =
                                    referenceDate.plusDays(
                                            shelfLife.getDays());

                            estimated = true;
                        }
                    }
                }

                ExpirationResult.Status status;

                if (expirationDate == null) {
                    status = ExpirationResult.Status.UNKNOWN;
                }
                else if (expirationDate.isBefore(today)) {
                    status = ExpirationResult.Status.EXPIRED;
                }
                else if (!expirationDate.isAfter(today.plusDays(3))) {
                    status = ExpirationResult.Status.EXPIRING_SOON;
                }
                else {
                    status = ExpirationResult.Status.SAFE;
                }

                results.add(new ExpirationResult(
                        item,
                        expirationDate,
                        estimated,
                        status
                ));
            }

            presenter.presentExpirationResults(
                    new TrackExpirationOutputData(results)
            );

        } catch (RuntimeException exception) {

            presenter.presentError(
                    "Unable to check expiration dates: "
                            + exception.getMessage()
            );
        }
    }
}
