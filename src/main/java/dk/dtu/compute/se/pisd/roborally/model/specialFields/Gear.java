package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af et gear
 *
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Gear extends FieldAction {
    private String direction;

    /**
     * Retningen af gear. Enten RIGHT eller LEFT.
     *
     * @return direction som String.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sætter retningen på et gear.
     *
     * @param direction retningen som String. Godtager kun "RIGHT" eller "LEFT".
     */
    public void setDirection(String direction) {
        if (direction.equals("RIGHT") || direction.equals("LEFT"))
            this.direction = direction;
    }

    /**
     * Gears aktion. Køres når en spiller står på samme felt.
     * Drejer spilleren i retningen af pilene.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return hvorvidt aktionen blev udført.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        try {
            if (direction.equals("LEFT")) {
                gameController.turnLeft(space.getPlayer());
            } else {
                gameController.turnRight(space.getPlayer());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
