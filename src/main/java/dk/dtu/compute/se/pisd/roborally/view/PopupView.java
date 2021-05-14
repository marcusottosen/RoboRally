package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import javax.swing.*;

/**
 * Denne klasse bruges til at displaye pop-up vinduerne.
 * @author Marcus Ottosen
 */
public class PopupView {
    private final Alert winningWindow = new Alert(Alert.AlertType.INFORMATION);
    private final JFrame optionWindow = new JFrame();

    /**
     * Når en spiller har vundet, vises dette vindue som også viser spillerens navn.
     * @param player den spiller der har vundet.
     */
    public void winningWindow(Player player){
        if (!winningWindow.isShowing()) {
            winningWindow.setTitle("WINNDER FOUND");
            winningWindow.setHeaderText(null);
            winningWindow.setContentText("Player \"" + player.getName() + "\" has won the game!");
            winningWindow.initStyle(StageStyle.UTILITY);
            winningWindow.showAndWait();
        }
    }

    /**
     * Spørger spilleren om han vil have nye kort og giver nye tilfældige kort hvis der vælges ja.
     */
    public int newCardsWindow(Player player){
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(optionWindow,
                "You have optained a special Energy Cube! \n " + "Would you like to receive a new deck of cards?",
                "Congratulations " + player.getName() + "!", dialogButton);

        return dialogResult;
    }
}
