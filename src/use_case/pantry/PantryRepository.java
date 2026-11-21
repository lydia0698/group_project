
package use_case.pantry;

import entity.PantryItem;
import java.util.List;

public interface PantryRepository {

    List<PantryItem> findAll();

    PantryItem findById(String id);

    void save(PantryItem item);

    void deleteById(String id);
}
