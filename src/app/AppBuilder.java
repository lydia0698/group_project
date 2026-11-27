package app;

import data_access.InMemoryPantryRepository;
import data_access.TheMealDbGateway;
import data_access.FoodKeeperDataAccessObject;
import use_case.track_expiration.ShelfLifeDataAccessInterface;

import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListPresenter;
import interface_adapter.generate_shopping_list.GenerateShoppingListViewModel;

import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryPresenter;
import interface_adapter.manage_pantry.ManagePantryViewModel;

import interface_adapter.search_shopping_recipes.SearchShoppingRecipesController;
import interface_adapter.search_shopping_recipes.SearchShoppingRecipesPresenter;
import interface_adapter.search_shopping_recipes.SearchShoppingRecipesViewModel;

import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationPresenter;
import interface_adapter.track_expiration.TrackExpirationViewModel;

import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.manage_pantry.ManagePantryInteractor;
import use_case.pantry.PantryRepository;
import use_case.search_shopping_recipes.SearchShoppingRecipesInteractor;
import use_case.track_expiration.TrackExpirationInteractor;

import view.MainFrame;
import view.ManagePantryView;
import view.ShoppingListView;
import view.TrackExpirationView;

public class AppBuilder {

    public static MainFrame build() {

        // Shared pantry repository
        PantryRepository repository = new InMemoryPantryRepository();

        // UC1: Manage Pantry
        ManagePantryViewModel pantryViewModel =
                new ManagePantryViewModel();

        ManagePantryPresenter pantryPresenter =
                new ManagePantryPresenter(pantryViewModel);

        ManagePantryInteractor pantryInteractor =
                new ManagePantryInteractor(repository, pantryPresenter);

        ManagePantryController pantryController =
                new ManagePantryController(pantryInteractor);

        ManagePantryView pantryView =
                new ManagePantryView(pantryController, pantryViewModel);

        // UC3: Generate Shopping List
        GenerateShoppingListViewModel shoppingViewModel =
                new GenerateShoppingListViewModel();

        GenerateShoppingListPresenter shoppingPresenter =
                new GenerateShoppingListPresenter(shoppingViewModel);

        TheMealDbGateway recipeGateway = new TheMealDbGateway();

        GenerateShoppingListInteractor shoppingInteractor =
                new GenerateShoppingListInteractor(
                        repository,
                        recipeGateway,
                        shoppingPresenter
                );

        GenerateShoppingListController shoppingController =
                new GenerateShoppingListController(
                        shoppingInteractor,
                        shoppingViewModel
                );

        // UC3: Search Recipes by Name
        SearchShoppingRecipesViewModel searchViewModel =
                new SearchShoppingRecipesViewModel();

        SearchShoppingRecipesPresenter searchPresenter =
                new SearchShoppingRecipesPresenter(
                        searchViewModel);

        SearchShoppingRecipesInteractor searchInteractor =
                new SearchShoppingRecipesInteractor(
                        recipeGateway,
                        searchPresenter);

        SearchShoppingRecipesController searchController =
                new SearchShoppingRecipesController(
                        searchInteractor,
                        searchViewModel);

        ShoppingListView shoppingView =
                new ShoppingListView(
                        shoppingController,
                        shoppingViewModel,
                        searchController,
                        searchViewModel);

        // UC4: Track Expiration Dates
        TrackExpirationViewModel expirationViewModel =
                new TrackExpirationViewModel();

        TrackExpirationPresenter expirationPresenter =
                new TrackExpirationPresenter(expirationViewModel);

        // Create FoodKeeper data access
        ShelfLifeDataAccessInterface shelfLifeDataAccess =
                new FoodKeeperDataAccessObject();

        // Create interactor with three arguments
        TrackExpirationInteractor expirationInteractor =
                new TrackExpirationInteractor(
                        repository,
                        expirationPresenter,
                        shelfLifeDataAccess
                );

        TrackExpirationController expirationController =
                new TrackExpirationController(expirationInteractor);

        TrackExpirationView expirationView =
                new TrackExpirationView(
                        expirationController,
                        expirationViewModel
                );

        // Build main window
        MainFrame mainFrame = new MainFrame(pantryView);

        mainFrame.addShoppingListTab(shoppingView);
        mainFrame.addExpirationTab(expirationView);

        return mainFrame;
    }
}
