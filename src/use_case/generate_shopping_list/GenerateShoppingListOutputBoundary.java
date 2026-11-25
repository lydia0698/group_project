package use_case.generate_shopping_list;

public interface GenerateShoppingListOutputBoundary {

    void presentSuccess(GenerateShoppingListOutputData outputData);

    void presentError(String errorMessage);
}
