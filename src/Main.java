import app.AppBuilder;
import view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame = AppBuilder.build();

            mainFrame.setVisible(true);

        });
    }
}

