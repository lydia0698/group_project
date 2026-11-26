package use_case.track_expiration;

import entity.PantryItem;
import use_case.pantry.PantryRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrackExpirationInteractor implements TrackExpirationInputBoundary{
    private final PantryRepository repository;
    private final TrackExpirationOutputBoundary presenter;

    public TrackExpirationInteractor(
            PantryRepository repository,
            TrackExpirationOutputBoundary presenter) {

        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void checkExpiration() {

        // Step 1: Get all ingredients from the pantry
        List<PantryItem> items = repository.findAll();

        // Step 2: Create lists for expiration results
        List<PantryItem> expiredItems = new ArrayList<>();
        List<PantryItem> expiringSoonItems = new ArrayList<>();

        // Step 3: Get the current date
        LocalDate today = LocalDate.now();

        // Step 4: Check each ingredient
        for (PantryItem item : items) {

            LocalDate expirationDate =
                    item.getExpirationDate();

            // No expiration date entered
            if (expirationDate == null) {
                // TODO: Use FoodKeeper to estimate the date
                continue;
            }

            // Already expired
            if (expirationDate.isBefore(today)) {
                expiredItems.add(item);
            }

            // Expiring within 3 days, including today
            else if (!expirationDate.isAfter(
                    today.plusDays(3))) {
                expiringSoonItems.add(item);
            }
        }

        // Step 5: Prepare output data
        TrackExpirationOutputData outputData =
                new TrackExpirationOutputData(
                        expiredItems,
                        expiringSoonItems
                );

        // Step 6: Send results to the presenter
        presenter.presentExpirationResults(outputData);
    }
}
