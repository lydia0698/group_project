
package app;

import data_access.InMemoryPantryRepository;
import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryPresenter;
import interface_adapter.manage_pantry.ManagePantryViewModel;
import use_case.manage_pantry.ManagePantryInteractor;
import use_case.pantry.PantryRepository;
import view.MainFrame;
import view.ManagePantryView;

public class AppBuilder {

    public static MainFrame build() {

        // 1. Create the shared repository
        PantryRepository repository =
                new InMemoryPantryRepository();

        // 2. Create the ViewModel
        ManagePantryViewModel viewModel =
                new ManagePantryViewModel();

        // 3. Create the Presenter
        ManagePantryPresenter presenter =
                new ManagePantryPresenter(viewModel);

        // 4. Create the Interactor
        ManagePantryInteractor interactor =
                new ManagePantryInteractor(repository, presenter);

        // 5. Create the Controller
        ManagePantryController controller =
                new ManagePantryController(interactor);

        // 6. Create the Pantry UI
        ManagePantryView pantryView =
                new ManagePantryView(controller, viewModel);

        // 7. Create the main window
        return new MainFrame(pantryView);
    }
}
