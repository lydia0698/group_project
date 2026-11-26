package use_case.track_expiration;

import entity.PantryItem;
import java.util.List;

public class TrackExpirationOutputData {
    private final List<PantryItem> expiredItems;
    private final List<PantryItem> expiringSoonItems;

    public TrackExpirationOutputData(
            List<PantryItem> expiredItems,
            List<PantryItem> expiringSoonItems) {

        this.expiredItems = expiredItems;
        this.expiringSoonItems = expiringSoonItems;
    }

    public List<PantryItem> getExpiredItems() {
        return expiredItems;
    }

    public List<PantryItem> getExpiringSoonItems() {
        return expiringSoonItems;
    }
}
