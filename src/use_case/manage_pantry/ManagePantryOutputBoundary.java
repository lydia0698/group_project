
package use_case.manage_pantry;

public interface ManagePantryOutputBoundary {

    void presentSuccess(ManagePantryOutputData outputData);

    void presentError(String errorMessage);
}

