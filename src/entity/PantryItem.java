package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PantryItem {

    private final String id;
    private String name;
    private BigDecimal quantity;
    private String unit;
    private LocalDate expirationDate;
    private StorageCondition storageCondition;
    private LocalDate storageStartDate;
    private LocalDate purchaseDate;


    public PantryItem(String id, String name,
                      BigDecimal quantity, String unit,
                      LocalDate expirationDate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        this.id = id;
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
        setExpirationDate(expirationDate);
    }

    public PantryItem(
            String id,
            String name,
            BigDecimal quantity,
            String unit,
            LocalDate expirationDate,
            StorageCondition storageCondition,
            LocalDate storageStartDate) {

        this(id, name, quantity, unit, expirationDate);

        this.storageCondition = storageCondition;
        this.storageStartDate = storageStartDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setQuantity(BigDecimal quantity) {
        if (quantity == null ||
                quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        new IngredientQuantity(BigDecimal.ONE, unit);
        this.unit = unit.trim();
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public StorageCondition getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(StorageCondition storageCondition) {
        this.storageCondition = storageCondition;
    }

    public LocalDate getStorageStartDate() {
        return storageStartDate;
    }

    public void setStorageStartDate(LocalDate storageStartDate) {
        this.storageStartDate = storageStartDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
