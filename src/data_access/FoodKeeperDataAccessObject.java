package data_access;

import entity.StorageCondition;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.track_expiration.ShelfLifeDataAccessInterface;
import use_case.track_expiration.ShelfLifeEstimate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalInt;

public class FoodKeeperDataAccessObject
        implements ShelfLifeDataAccessInterface {

    private final JSONArray products;

    public FoodKeeperDataAccessObject() {

        // Keep the file-loading method that worked for you.
        try (InputStream input = Files.newInputStream(
                Path.of("resources", "foodkeeper.json"))) {

            String json = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            JSONObject root = new JSONObject(json);
            JSONArray sheets = root.getJSONArray("sheets");

            JSONArray productData = null;

            for (int i = 0; i < sheets.length(); i++) {
                JSONObject sheet = sheets.getJSONObject(i);

                if ("Product".equals(sheet.getString("name"))) {
                    productData = sheet.getJSONArray("data");
                    break;
                }
            }

            if (productData == null) {
                throw new IllegalStateException(
                        "Product sheet not found");
            }

            this.products = productData;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to read FoodKeeper JSON", e);
        }
    }

    // New method: returns days AND date basis.
    @Override
    public Optional<ShelfLifeEstimate> getShelfLifeEstimate(
            String ingredientName,
            StorageCondition storageCondition) {

        if (ingredientName == null
                || ingredientName.isBlank()
                || storageCondition == null) {
            return Optional.empty();
        }

        String target = normalize(ingredientName);

        JSONObject keywordMatch = null;
        int keywordMatches = 0;

        for (int i = 0; i < products.length(); i++) {

            JSONObject product = flattenProduct(
                    products.getJSONArray(i));

            String name = product.optString("Name", "");

            if (normalize(name).equals(target)) {
                return readShelfLife(
                        product, storageCondition);
            }

            String keywords =
                    product.optString("Keywords", "");

            for (String keyword : keywords.split(",")) {
                if (normalize(keyword).equals(target)) {
                    keywordMatch = product;
                    keywordMatches++;
                    break;
                }
            }
        }

        if (keywordMatches == 1) {
            return readShelfLife(
                    keywordMatch, storageCondition);
        }

        return Optional.empty();
    }

    // Old method: keep it for existing code and tests.
    @Override
    public OptionalInt getShelfLifeDays(
            String ingredientName,
            StorageCondition storageCondition) {

        Optional<ShelfLifeEstimate> estimate =
                getShelfLifeEstimate(
                        ingredientName,
                        storageCondition);

        if (estimate.isPresent()) {
            return OptionalInt.of(
                    estimate.get().getDays());
        }

        return OptionalInt.empty();
    }

    private JSONObject flattenProduct(JSONArray fields) {

        JSONObject product = new JSONObject();

        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);

            for (String key : field.keySet()) {
                product.put(key, field.get(key));
            }
        }

        return product;
    }

    private Optional<ShelfLifeEstimate> readShelfLife(
            JSONObject product,
            StorageCondition condition) {

        String prefix = condition == StorageCondition.REFRIGERATION
                ? "Refrigerate"
                : "Freeze";

        // First try general storage guidance.
        OptionalInt general = readDuration(
                product,
                prefix + "_Min",
                prefix + "_Metric");

        if (general.isPresent()) {
            return Optional.of(new ShelfLifeEstimate(
                    general.getAsInt(),
                    ShelfLifeEstimate.DateBasis.STORAGE_START_DATE
            ));
        }

        // Otherwise try date-of-purchase guidance.
        OptionalInt purchase = readDuration(
                product,
                "DOP_" + prefix + "_Min",
                "DOP_" + prefix + "_Metric");

        if (purchase.isPresent()) {
            return Optional.of(new ShelfLifeEstimate(
                    purchase.getAsInt(),
                    ShelfLifeEstimate.DateBasis.PURCHASE_DATE
            ));
        }

        return Optional.empty();
    }

    private OptionalInt readDuration(
            JSONObject product,
            String amountKey,
            String metricKey) {

        Object amount = product.opt(amountKey);

        if (!(amount instanceof Number)) {
            return OptionalInt.empty();
        }

        double value = ((Number) amount).doubleValue();

        if (value <= 0) {
            return OptionalInt.empty();
        }

        String metric = product.optString(
                metricKey, "").toLowerCase();

        double days;

        switch (metric) {
            case "day":
            case "days":
                days = value;
                break;

            case "week":
            case "weeks":
                days = value * 7;
                break;

            case "month":
            case "months":
                days = value * 30;
                break;

            case "year":
            case "years":
                days = value * 365;
                break;

            default:
                return OptionalInt.empty();
        }

        if (days > Integer.MAX_VALUE) {
            return OptionalInt.empty();
        }

        return OptionalInt.of((int) Math.round(days));
    }

    private String normalize(String name) {

        String normalized = name.trim().toLowerCase();

        if (normalized.endsWith("oes")) {
            return normalized.substring(
                    0, normalized.length() - 2);
        }

        if (normalized.endsWith("s")
                && !normalized.endsWith("ss")) {
            return normalized.substring(
                    0, normalized.length() - 1);
        }

        return normalized;
    }
}
