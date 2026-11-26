package app;

import data_access.InMemoryPantryRepository;
import data_access.TheMealDbGateway;

import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListPresenter;
import interface_adapter.generate_shopping_list.GenerateShoppingListViewModel;

import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryPresenter;
import interface_adapter.manage_pantry.ManagePantryViewModel;

import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationPresenter;
import interface_adapter.track_expiration.TrackExpirationViewModel;

import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.manage_pantry.ManagePantryInteractor;
import use_case.pantry.PantryRepository;
import use_case.recipe.RecipeGateway;
import use_case.track_expiration.TrackExpirationInteractor;

import view.MainFrame;
import view.ManagePantryView;
import view.ShoppingListView;
import view.TrackExpirationView;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppBuilder {

    public static MainFrame build() {

        // Create shared pantry repository
        PantryRepository repository = new InMemoryPantryRepository();

        // UC1: Manage Pantry
        ManagePantryViewModel viewModel = new ManagePantryViewModel();
        ManagePantryPresenter presenter = new ManagePantryPresenter(viewModel);

        ManagePantryInteractor interactor =
                new ManagePantryInteractor(repository, presenter);

        ManagePantryController controller =
                new ManagePantryController(interactor);

        ManagePantryView pantryView =
                new ManagePantryView(controller, viewModel);

        // UC3: Generate Shopping List
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

        // Example recipes while UC2 integration is pending
        Map<String, String> recipeOptions = new LinkedHashMap<>();
        recipeOptions.put("53016", "Chick-Fil-A Sandwich");
        recipeOptions.put("52772", "Teriyaki Chicken Casserole");

        ShoppingListView shoppingView =
                new ShoppingListView(
                        shoppingController, shoppingViewModel, recipeOptions);

        // UC4: Track Expiration Dates
        TrackExpirationViewModel expirationViewModel =
                new TrackExpirationViewModel();

        TrackExpirationPresenter expirationPresenter =
                new TrackExpirationPresenter(expirationViewModel);

        TrackExpirationInteractor expirationInteractor =
                new TrackExpirationInteractor(
                        repository, expirationPresenter);

        TrackExpirationController expirationController =
                new TrackExpirationController(expirationInteractor);

        TrackExpirationView expirationView =
                new TrackExpirationView(
                        expirationController, expirationViewModel);

        // Create MainFrame
        MainFrame mainFrame = new MainFrame(pantryView);

        // Add both tabs
        mainFrame.addShoppingListTab(shoppingView);
        mainFrame.addExpirationTab(expirationView);

        return mainFrame;
    }
}
