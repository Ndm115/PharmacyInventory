package pharmacyinventory;

import javax.swing.SwingUtilities;

public class PharmacyInventory {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginFrame login = new LoginFrame();
            login.setVisible(true);

        });
    }
}