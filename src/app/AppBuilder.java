package app;

import data_access.InMemoryPantryRepository;
import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryPresenter;
import interface_adapter.manage_pantry.ManagePantryViewModel;
import use_case.manage_pantry.ManagePantryInteractor;
import use_case.pantry.PantryRepository;
import view.MainFrame;
import view.ManagePantryView;
import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationPresenter;
import interface_adapter.track_expiration.TrackExpirationViewModel;
import use_case.track_expiration.TrackExpirationInteractor;
import view.TrackExpirationView;

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

        // Create expiration ViewModel
        TrackExpirationViewModel expirationViewModel =
                new TrackExpirationViewModel();

// Create expiration Presenter
        TrackExpirationPresenter expirationPresenter =
                new TrackExpirationPresenter(expirationViewModel);

// Create expiration Interactor
        TrackExpirationInteractor expirationInteractor =
                new TrackExpirationInteractor(
                        repository,
                        expirationPresenter
                );

// Create expiration Controller
        TrackExpirationController expirationController =
                new TrackExpirationController(expirationInteractor);

// Create expiration View
        TrackExpirationView expirationView =
                new TrackExpirationView(
                        expirationController,
                        expirationViewModel
                );

// Add expiration tab to the main window
        MainFrame mainFrame = new MainFrame(pantryView);
        mainFrame.addExpirationTab(expirationView);

        return mainFrame;
    }
}
