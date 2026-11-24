
package view;

import entity.PantryItem;
import interface_adapter.manage_pantry.ManagePantryController;
import interface_adapter.manage_pantry.ManagePantryViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ManagePantryView extends JPanel {

    private final ManagePantryController controller;
    private final ManagePantryViewModel viewModel;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ManagePantryView(
            ManagePantryController controller,
            ManagePantryViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Manage Pantry Inventory");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {
                "ID", "Ingredient", "Quantity", "Expiration Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        // Keep ID in the table model, but hide it from the UI.
        table.removeColumn(table.getColumnModel().getColumn(0));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addItem());
        editButton.addActionListener(e -> editItem());
        deleteButton.addActionListener(e -> deleteItem());
        refreshButton.addActionListener(e -> controller.listItems());

        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "items":
                    refreshTable();
                    break;
                case "message":
                    if (!viewModel.getMessage().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this, viewModel.getMessage());
                    }
                    break;
                case "errorMessage":
                    if (!viewModel.getErrorMessage().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this,
                                viewModel.getErrorMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    break;
            }
        });

        controller.listItems();
    }

    private String formatQuantity(PantryItem item) {

        String quantity = item.getQuantity()
                .stripTrailingZeros()
                .toPlainString();

        String unit = item.getUnit();

        if ("pcs".equalsIgnoreCase(unit)) {
            return quantity;
        }

        return quantity + unit;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (PantryItem item : viewModel.getItems()) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    formatQuantity(item),
                    item.getExpirationDate()
            });
        }
    }

    private void addItem() {
        showItemDialog(null);
    }

    private void editItem() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an item to edit.");
            return;
        }

        String id = tableModel.getValueAt(
                table.convertRowIndexToModel(selectedRow), 0).toString();

        PantryItem selectedItem = null;

        for (PantryItem item : viewModel.getItems()) {
            if (item.getId().equals(id)) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            showItemDialog(selectedItem);
        }
    }

    private void deleteItem() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an item to delete.");
            return;
        }

        String id = tableModel.getValueAt(
                table.convertRowIndexToModel(selectedRow), 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            controller.removeItem(id);
        }
    }

    private void showItemDialog(PantryItem existingItem) {

        boolean editing = existingItem != null;

        JTextField nameField = new JTextField(
                editing ? existingItem.getName() : "");

        JTextField quantityField = new JTextField(
                editing ? formatQuantity(existingItem) : "");

        JTextField expirationField = new JTextField(
                editing && existingItem.getExpirationDate() != null
                        ? existingItem.getExpirationDate().toString()
                        : "");

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Ingredient:"));
        panel.add(nameField);

        panel.add(new JLabel("Quantity (e.g. 500ml, 200g, 3):"));
        panel.add(quantityField);

        panel.add(new JLabel("Expiration (yyyy-MM-dd):"));
        panel.add(expirationField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                editing ? "Edit Item" : "Add Item",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();

            String dateText = expirationField.getText().trim();

            LocalDate expirationDate = dateText.isEmpty()
                    ? null
                    : LocalDate.parse(dateText);

            if (editing) {
                controller.updateItem(
                        existingItem.getId(),
                        name,
                        quantityText,
                        expirationDate
                );
            } else {
                controller.addItem(
                        name,
                        quantityText,
                        expirationDate
                );
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Date must use yyyy-MM-dd format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
