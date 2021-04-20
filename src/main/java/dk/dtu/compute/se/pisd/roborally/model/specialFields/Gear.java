package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af et geat
 *
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Gear extends FieldAction {
    private String direction;

    /**
     * Retningen af gear. Enten RIGHT eller LEFT.
     * @return direction som String.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sætter gears retning.
     * @param direction retningen som String. Godtager kun "RIGHT" eller "LEFT".
     */
    public void setDirection(String direction) {
        if (direction.equals("RIGHT") || direction.equals("LEFT"))
            this.direction = direction;
    }

    /**
     * Gears aktion. Køres når en spiller står på samme felt.
     * Drejer spilleren i retningen af pilene.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return false.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        System.out.println("I will rotate you!");

        if (direction.equals("LEFT")){
            gameController.turnLeft(space.getPlayer());
        }else{
            gameController.turnRight(space.getPlayer());
        }
        return false;
    }
}
