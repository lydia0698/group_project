package use_case.track_expiration;

public class ShelfLifeEstimate {

    public enum DateBasis {
        PURCHASE_DATE,
        STORAGE_START_DATE
    }

    private final int days;
    private final DateBasis dateBasis;

    public ShelfLifeEstimate(int days, DateBasis dateBasis) {
        if (days <= 0 || dateBasis == null) {
            throw new IllegalArgumentException(
                    "Invalid shelf-life estimate");
        }

        this.days = days;
        this.dateBasis = dateBasis;
    }

    public int getDays() {
        return days;
    }

    public DateBasis getDateBasis() {
        return dateBasis;
    }
}
