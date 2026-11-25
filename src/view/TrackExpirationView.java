package view;

import entity.PantryItem;
import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrackExpirationView extends JPanel{

    private final TrackExpirationController controller;
    private final TrackExpirationViewModel viewModel;
    private final DefaultTableModel tableModel;

    public TrackExpirationView(
            TrackExpirationController controller,
            TrackExpirationViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(
                15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Track Ingredient Expiration Dates");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "Ingredient", "Quantity", "Unit",
                "Expiration Date", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Check expiration button
        JButton checkButton = new JButton("Check Expiration");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(checkButton);
        add(buttonPanel, BorderLayout.SOUTH);

        checkButton.addActionListener(
                e -> controller.checkExpiration());

        // Listen for ViewModel updates
        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "expiredItems":
                case "expiringSoonItems":
                    refreshTable();
                    break;

                case "errorMessage":
                    String message = viewModel.getErrorMessage();

                    if (message != null && !message.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this,
                                message,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                default:
                    break;
            }
        });
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        // Display expired ingredients
        for (PantryItem item : viewModel.getExpiredItems()) {
            addItemToTable(item, "Expired");
        }

        // Display ingredients expiring soon
        for (PantryItem item : viewModel.getExpiringSoonItems()) {
            addItemToTable(item, "Expiring Soon");
        }
    }

    private void addItemToTable(
            PantryItem item, String status) {

        tableModel.addRow(new Object[]{
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getExpirationDate(),
                status
        });
    }

}
