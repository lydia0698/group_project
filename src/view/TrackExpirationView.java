package view;

import entity.PantryItem;
import entity.StorageCondition;
import interface_adapter.track_expiration.TrackExpirationController;
import interface_adapter.track_expiration.TrackExpirationViewModel;
import use_case.track_expiration.ExpirationResult;
import use_case.track_expiration.ShelfLifeEstimate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class TrackExpirationView extends JPanel {

    private final TrackExpirationController controller;
    private final TrackExpirationViewModel viewModel;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public TrackExpirationView(
            TrackExpirationController controller,
            TrackExpirationViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(
                15, 15, 15, 15));

        JLabel title = new JLabel(
                "Track Ingredient Expiration Dates");

        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {
                "Ingredient",
                "Quantity",
                "Unit",
                "Expiration Date",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton checkButton =
                new JButton("Check Expiration");

        JButton storageButton =
                new JButton("Set Storage Information");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(checkButton);
        buttonPanel.add(storageButton);
        add(buttonPanel, BorderLayout.SOUTH);

        checkButton.addActionListener(
                e -> controller.checkExpiration());

        storageButton.addActionListener(
                e -> showStorageDialog());

        viewModel.addPropertyChangeListener(evt -> {

            switch (evt.getPropertyName()) {
                case "results":
                    refreshTable();
                    break;

                case "errorMessage":
                    String message =
                            viewModel.getErrorMessage();

                    if (message != null
                            && !message.isEmpty()) {

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

        controller.checkExpiration();
    }

    private void showStorageDialog() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an ingredient first.");
            return;
        }

        int modelRow =
                table.convertRowIndexToModel(selectedRow);

        List<ExpirationResult> results =
                viewModel.getResults();

        if (modelRow < 0 || modelRow >= results.size()) {
            return;
        }

        PantryItem item =
                results.get(modelRow).getItem();

        if (item.getExpirationDate() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "This ingredient already has "
                            + "an entered expiration date.");
            return;
        }

        JComboBox<StorageCondition> conditionBox =
                new JComboBox<>(StorageCondition.values());

        if (item.getStorageCondition() != null) {
            conditionBox.setSelectedItem(
                    item.getStorageCondition());
        }

        JLabel dateLabel = new JLabel("Reference Date:");

        JTextField dateField = new JTextField(12);

        JLabel formatLabel =
                new JLabel("Format: yyyy-MM-dd");

        // Update the date label and existing value when
        // the user changes refrigeration/freezing.
        Runnable updateDateField = () -> {

            StorageCondition condition =
                    (StorageCondition)
                            conditionBox.getSelectedItem();

            Optional<ShelfLifeEstimate.DateBasis> basis =
                    controller.getDateBasis(
                            item.getId(), condition);

            if (!basis.isPresent()) {
                dateLabel.setText("No estimate available:");
                dateField.setText("");
                dateField.setEnabled(false);
                return;
            }

            dateField.setEnabled(true);

            LocalDate existingDate;

            if (basis.get()
                    == ShelfLifeEstimate.DateBasis.PURCHASE_DATE) {

                dateLabel.setText("Purchase Date:");
                existingDate = item.getPurchaseDate();

            } else {

                dateLabel.setText("Storage Start Date:");
                existingDate = item.getStorageStartDate();
            }

            dateField.setText(
                    existingDate == null
                            ? ""
                            : existingDate.toString());
        };

        conditionBox.addActionListener(
                e -> updateDateField.run());

        updateDateField.run();

        JPanel panel = new JPanel(
                new GridLayout(4, 2, 8, 8));

        panel.add(new JLabel("Ingredient:"));
        panel.add(new JLabel(item.getName()));

        panel.add(new JLabel("Storage Condition:"));
        panel.add(conditionBox);

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(new JLabel("Date Format:"));
        panel.add(formatLabel);

        while (true) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Set Storage Information",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            StorageCondition condition =
                    (StorageCondition)
                            conditionBox.getSelectedItem();

            Optional<ShelfLifeEstimate.DateBasis> basis =
                    controller.getDateBasis(
                            item.getId(), condition);

            if (!basis.isPresent()) {
                JOptionPane.showMessageDialog(
                        this,
                        "FoodKeeper has no numeric estimate "
                                + "for this storage condition.");
                continue;
            }

            try {
                String dateText =
                        dateField.getText().trim();

                if (dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter the required date.");
                    continue;
                }

                LocalDate referenceDate =
                        LocalDate.parse(dateText);

                if (referenceDate.isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(
                            this,
                            "The date cannot be in the future.");
                    continue;
                }

                controller.updateStorageInfo(
                        item.getId(),
                        condition,
                        referenceDate,
                        basis.get()
                );

                return;

            } catch (DateTimeParseException exception) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid date. Use yyyy-MM-dd.",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (ExpirationResult result
                : viewModel.getResults()) {

            PantryItem item = result.getItem();

            String status;

            switch (result.getStatus()) {
                case EXPIRED:
                    status = "Expired";
                    break;

                case EXPIRING_SOON:
                    status = "Expiring Soon";
                    break;

                case SAFE:
                    status = "No Warning";
                    break;

                default:
                    status = "Unknown";
            }

            String dateText =
                    result.getExpirationDate() == null
                            ? "Unknown"
                            : result.getExpirationDate().toString();

            tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit(),
                    dateText,
                    status
            });
        }
    }
}
