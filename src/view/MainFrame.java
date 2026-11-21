
package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JTabbedPane tabs;

    public MainFrame(ManagePantryView pantryView) {

        setTitle("Smart Kitchen Assistant");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        // UC1: Manage Pantry
        tabs.addTab("Pantry Inventory", pantryView);

        // Other use cases will be added by teammates later.

        add(tabs, BorderLayout.CENTER);
    }

    public void addRecipesTab(JPanel recipesView) {
        tabs.addTab("Find Recipes", recipesView);
    }

    public void addShoppingListTab(JPanel shoppingListView) {
        tabs.addTab("Shopping List", shoppingListView);
    }

    public void addExpirationTab(JPanel expirationView) {
        tabs.addTab("Expiration Tracking", expirationView);
    }
}
