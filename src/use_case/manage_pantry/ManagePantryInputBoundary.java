
package use_case.manage_pantry;

public interface ManagePantryInputBoundary {

    void addItem(ManagePantryInputData inputData);

    void updateItem(ManagePantryInputData inputData);

    void removeItem(String itemId);

    void listItems();
}

