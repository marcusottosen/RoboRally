package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af et pit.
 *
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Pit extends FieldAction {

    /**
     * Sætter spillerens liv til 0.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return false.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        System.out.println("You fell into the abyss");
        space.getPlayer().setHealth(0);
        return false;
    }
}
