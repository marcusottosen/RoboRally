package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af en Toolbox.
 *
 * @author Marcus Ottosen
 */
public class Toolbox extends FieldAction {

    /**
     * Toolboxens aktion. Køres når en spiller står på Toolboxens felt.
     * Giver spilleren et liv, hvis spilleren ikke allerede har fuldt liv.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return hvorvidt doAction blev udført.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player.getHealth() < player.availableHealth) {
            player.setHealth(player.getHealth() + 1);
            return true;
        } else
        return false;
    }
}
