import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlateauConfigurator app = new PlateauConfigurator();
            app.setVisible(true);
        });
    }
}
