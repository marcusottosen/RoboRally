package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gear extends FieldAction {

    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

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
