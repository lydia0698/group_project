package entity;

public class RecipeSummary {
    private final String id;
    private final String name;

    public RecipeSummary(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe ID is required.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe name is required.");
        }

        this.id = id.trim();
        this.name = name.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
