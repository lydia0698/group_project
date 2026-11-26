package view;

import entity.RecipeSummary;
import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListViewModel;
import interface_adapter.search_shopping_recipes.SearchShoppingRecipesController;
import interface_adapter.search_shopping_recipes.SearchShoppingRecipesViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShoppingListView extends JPanel {

    private final GenerateShoppingListViewModel viewModel;
    private final SearchShoppingRecipesViewModel searchViewModel;

    private final JTextField queryField;
    private final JButton searchButton;
    private final JComboBox<RecipeOption> recipeBox;
    private final JButton generateButton;

    private final DefaultListModel<String> listModel;
    private final JLabel resultLabel;
    private final JLabel searchStatusLabel;
    private final JTextArea statusArea;

    public ShoppingListView(
            GenerateShoppingListController controller,
            GenerateShoppingListViewModel viewModel,
            SearchShoppingRecipesController searchController,
            SearchShoppingRecipesViewModel searchViewModel) {

        this.viewModel = viewModel;
        this.searchViewModel = searchViewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Shopping List");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        queryField = new JTextField(30);
        searchButton = new JButton("Search Recipes");

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 0));

        searchPanel.add(
                new JLabel("Recipe name"), BorderLayout.WEST);
        searchPanel.add(queryField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchStatusLabel = new JLabel();
        recipeBox = new JComboBox<>();
        generateButton =
                new JButton("Generate Shopping List");

        JPanel selectionPanel =
                new JPanel(new BorderLayout(10, 0));

        selectionPanel.add(
                new JLabel("Recipe"), BorderLayout.WEST);
        selectionPanel.add(recipeBox, BorderLayout.CENTER);
        selectionPanel.add(generateButton, BorderLayout.EAST);

        JPanel searchControls =
                new JPanel(new BorderLayout(0, 6));

        searchControls.add(searchPanel, BorderLayout.NORTH);
        searchControls.add(
                searchStatusLabel, BorderLayout.CENTER);
        searchControls.add(
                selectionPanel, BorderLayout.SOUTH);

        JPanel headerPanel =
                new JPanel(new BorderLayout(0, 15));

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(searchControls, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> ingredientList = new JList<>(listModel);
        resultLabel = new JLabel("Shopping list");

        JPanel resultPanel =
                new JPanel(new BorderLayout(0, 8));

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(
                new JScrollPane(ingredientList),
                BorderLayout.CENTER);

        add(resultPanel, BorderLayout.CENTER);

        statusArea = new JTextArea(3, 40);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setOpaque(false);
        statusArea.setFont(
                UIManager.getFont("Label.font"));

        JScrollPane statusPane =
                new JScrollPane(statusArea);

        statusPane.setBorder(
                BorderFactory.createEmptyBorder());
        statusPane.setOpaque(false);
        statusPane.getViewport().setOpaque(false);

        add(statusPane, BorderLayout.SOUTH);

        recipeBox.addActionListener(event -> {
            viewModel.setResult("", List.of(), "");
            refreshControls();
        });

        searchButton.addActionListener(event -> {
            if (!isBusy()) {
                viewModel.setResult("", List.of(), "");
                searchController.search(queryField.getText());
            }
        });

        queryField.addActionListener(event -> {
            if (searchButton.isEnabled()) {
                searchButton.doClick();
            }
        });

        generateButton.addActionListener(event -> {
            RecipeOption selected =
                    (RecipeOption) recipeBox.getSelectedItem();

            if (selected != null && !isBusy()) {
                controller.generate(selected.id);
            }
        });

        viewModel.addPropertyChangeListener(
                event -> refreshFromViewModel());

        searchViewModel.addPropertyChangeListener(
                event -> refreshFromSearchViewModel());

        refreshFromSearchViewModel();
        refreshFromViewModel();
    }

    public boolean selectRecipe(
            String recipeId, String recipeName) {

        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException(
                    "Select recipes from Swing's event dispatch thread.");
        }

        if (isBusy()) {
            return false;
        }

        if (recipeId == null || recipeId.isBlank()
                || recipeName == null || recipeName.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe ID and name are required.");
        }

        String id = recipeId.trim();
        String name = recipeName.trim();

        for (int i = 0; i < recipeBox.getItemCount(); i++) {
            RecipeOption existing = recipeBox.getItemAt(i);

            if (existing.id.equals(id)) {
                if (!existing.name.equals(name)) {
                    recipeBox.removeItemAt(i);
                    recipeBox.insertItemAt(
                            new RecipeOption(id, name), i);
                }

                recipeBox.setSelectedIndex(i);
                return true;
            }
        }

        RecipeOption option = new RecipeOption(id, name);
        recipeBox.addItem(option);
        recipeBox.setSelectedItem(option);

        return true;
    }

    private boolean isBusy() {
        return viewModel.isLoading()
                || searchViewModel.isLoading();
    }

    private void refreshControls() {
        boolean busy = isBusy();

        queryField.setEnabled(!busy);
        searchButton.setEnabled(!busy);

        recipeBox.setEnabled(
                !busy && recipeBox.getItemCount() > 0);

        generateButton.setEnabled(
                !busy && recipeBox.getSelectedItem() != null);
    }

    private void refreshFromSearchViewModel() {
        recipeBox.removeAllItems();

        for (RecipeSummary recipe
                : searchViewModel.getRecipes()) {

            recipeBox.addItem(
                    new RecipeOption(
                            recipe.getId(), recipe.getName()));
        }

        recipeBox.setSelectedIndex(-1);

        if (searchViewModel.getErrorMessage().isEmpty()) {
            searchStatusLabel.setForeground(
                    UIManager.getColor("Label.foreground"));

            searchStatusLabel.setText(
                    searchViewModel.getMessage());
        } else {
            searchStatusLabel.setForeground(Color.RED);

            searchStatusLabel.setText(
                    searchViewModel.getErrorMessage());
        }

        refreshControls();
    }

    private void refreshFromViewModel() {
        listModel.clear();

        for (String ingredient
                : viewModel.getMissingIngredients()) {
            listModel.addElement(ingredient);
        }

        String recipeName = viewModel.getRecipeName();

        resultLabel.setText(
                recipeName.isEmpty()
                        ? "Shopping list"
                        : "Shopping list for " + recipeName);

        if (viewModel.getErrorMessage().isEmpty()) {
            statusArea.setForeground(
                    UIManager.getColor("Label.foreground"));

            statusArea.setText(viewModel.getMessage());
        } else {
            statusArea.setForeground(Color.RED);
            statusArea.setText(viewModel.getErrorMessage());
        }

        refreshControls();
    }

    private static class RecipeOption {

        private final String id;
        private final String name;

        private RecipeOption(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
