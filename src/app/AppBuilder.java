
package app;

import data_access.InMemoryPantryRepository;
import data_access.TheMealDbGateway;
import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListPresenter;
import interface_adapter.generate_shopping_list.GenerateShoppingListViewModel;
import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryPresenter;
import interface_adapter.manage_pantry.ManagePantryViewModel;
import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.manage_pantry.ManagePantryInteractor;
import use_case.pantry.PantryRepository;
import use_case.recipe.RecipeGateway;
import view.MainFrame;
import view.ManagePantryView;
import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationPresenter;
import interface_adapter.track_expiration.TrackExpirationViewModel;
import use_case.track_expiration.TrackExpirationInteractor;
import view.TrackExpirationView;
import view.ShoppingListView;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppBuilder {

    public static MainFrame build() {
        PantryRepository repository = new InMemoryPantryRepository();

        ManagePantryViewModel viewModel = new ManagePantryViewModel();
        ManagePantryPresenter presenter = new ManagePantryPresenter(viewModel);
        ManagePantryInteractor interactor =
                new ManagePantryInteractor(repository, presenter);
        ManagePantryController controller =
                new ManagePantryController(interactor);
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

        GenerateShoppingListViewModel shoppingViewModel =
                new GenerateShoppingListViewModel();
        GenerateShoppingListPresenter shoppingPresenter =
                new GenerateShoppingListPresenter(shoppingViewModel);

        RecipeGateway recipeGateway = new TheMealDbGateway();

        GenerateShoppingListInteractor shoppingInteractor =
                new GenerateShoppingListInteractor(
                        repository, recipeGateway, shoppingPresenter);
        GenerateShoppingListController shoppingController =
                new GenerateShoppingListController(
                        shoppingInteractor, shoppingViewModel);

        // Example recipes for testing while UC2 integration is pending.
        Map<String, String> recipeOptions = new LinkedHashMap<>();
        recipeOptions.put("53016", "Chick-Fil-A Sandwich");
        recipeOptions.put("52772", "Teriyaki Chicken Casserole");

        ShoppingListView shoppingView = new ShoppingListView(
                shoppingController, shoppingViewModel, recipeOptions);

        MainFrame mainFrame = new MainFrame(pantryView);
        mainFrame.addShoppingListTab(shoppingView);
        return mainFrame;
    }
}
