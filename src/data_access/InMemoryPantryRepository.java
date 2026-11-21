
package data_access;

import entity.PantryItem;
import use_case.pantry.PantryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPantryRepository implements PantryRepository {

    private final Map<String, PantryItem> items = new HashMap<>();

    @Override
    public List<PantryItem> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public PantryItem findById(String id) {
        return items.get(id);
    }

    @Override
    public void save(PantryItem item) {
        items.put(item.getId(), item);
    }

    @Override
    public void deleteById(String id) {
        items.remove(id);
    }
}
