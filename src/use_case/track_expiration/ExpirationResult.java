package use_case.track_expiration;

import entity.PantryItem;
import java.time.LocalDate;

public class ExpirationResult {
    public enum Status {
        EXPIRED,
        EXPIRING_SOON,
        SAFE,
        UNKNOWN
    }

    private final PantryItem item;
    private final LocalDate expirationDate;
    private final boolean estimated;
    private final Status status;

    public ExpirationResult(
            PantryItem item,
            LocalDate expirationDate,
            boolean estimated,
            Status status) {

        this.item = item;
        this.expirationDate = expirationDate;
        this.estimated = estimated;
        this.status = status;
    }

    public PantryItem getItem() {
        return item;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean isEstimated() {
        return estimated;
    }

    public Status getStatus() {
        return status;
    }

}
