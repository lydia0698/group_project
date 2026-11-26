package view;

import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ShoppingListView extends JPanel{
    private final GenerateShoppingListViewModel viewModel;
    private final JComboBox<RecipeOption> recipeBox;
    private final JButton generateButton;
    private final DefaultListModel<String> listModel;
    private final JLabel resultLabel;
    private final JTextArea statusArea;

    public ShoppingListView(
            GenerateShoppingListController controller,
            GenerateShoppingListViewModel viewModel,
            Map<String, String> recipeOptions) {
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Shopping List");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        recipeBox = new JComboBox<>();
        for (Map.Entry<String, String> entry : recipeOptions.entrySet()) {
            recipeBox.addItem(
                    new RecipeOption(entry.getKey(), entry.getValue()));
        }
        recipeBox.setSelectedIndex(-1);

        generateButton = new JButton("Generate Shopping List");

        JPanel selectionPanel = new JPanel(new BorderLayout(10, 0));
        selectionPanel.add(new JLabel("Recipe"), BorderLayout.WEST);
        selectionPanel.add(recipeBox, BorderLayout.CENTER);
        selectionPanel.add(generateButton, BorderLayout.EAST);

        JPanel headerPanel = new JPanel(new BorderLayout(0, 15));
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(selectionPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> ingredientList = new JList<>(listModel);

        resultLabel = new JLabel("Shopping list");

        JPanel resultPanel = new JPanel(new BorderLayout(0, 8));
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(
                new JScrollPane(ingredientList), BorderLayout.CENTER);
        add(resultPanel, BorderLayout.CENTER);

        statusArea = new JTextArea(3, 40);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setOpaque(false);
        statusArea.setFont(UIManager.getFont("Label.font"));

        JScrollPane statusPane = new JScrollPane(statusArea);
        statusPane.setBorder(BorderFactory.createEmptyBorder());
        statusPane.setOpaque(false);
        statusPane.getViewport().setOpaque(false);
        add(statusPane, BorderLayout.SOUTH);

        recipeBox.addActionListener(event ->
                viewModel.setResult("", List.of(), ""));

        generateButton.addActionListener(event -> {
            RecipeOption selected =
                    (RecipeOption) recipeBox.getSelectedItem();

            if (selected != null) {
                controller.generate(selected.id);
            }
        });

        viewModel.addPropertyChangeListener(
                event -> refreshFromViewModel());

        refreshFromViewModel();
    }

    public boolean selectRecipe(String recipeId, String recipeName) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException(
                    "Select recipes from Swing's event dispatch thread.");
        }

        if (viewModel.isLoading()) {
            return false;
        }

        if (recipeId == null || recipeId.isBlank()
                || recipeName == null || recipeName.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe ID and name are required.");
        }

        String id = recipeId.trim();

        for (int i = 0; i < recipeBox.getItemCount(); i++) {
            if (recipeBox.getItemAt(i).id.equals(id)) {
                recipeBox.setSelectedIndex(i);
                return true;
            }
        }

        RecipeOption option =
                new RecipeOption(id, recipeName.trim());

        recipeBox.addItem(option);
        recipeBox.setSelectedItem(option);
        return true;
    }

    private void refreshFromViewModel() {
        boolean loading = viewModel.isLoading();

        recipeBox.setEnabled(!loading);
        generateButton.setEnabled(
                !loading && recipeBox.getSelectedItem() != null);

        listModel.clear();
        for (String ingredient : viewModel.getMissingIngredients()) {
            listModel.addElement(ingredient);
        }

        String recipeName = viewModel.getRecipeName();

        if (recipeName.isEmpty()) {
            resultLabel.setText("Shopping list");
        } else {
            resultLabel.setText(
                    "Shopping list for " + recipeName);
        }

        if (viewModel.getErrorMessage().isEmpty()) {
            statusArea.setForeground(
                    UIManager.getColor("Label.foreground"));
            statusArea.setText(viewModel.getMessage());
        } else {
            statusArea.setForeground(Color.RED);
            statusArea.setText(viewModel.getErrorMessage());
        }
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
