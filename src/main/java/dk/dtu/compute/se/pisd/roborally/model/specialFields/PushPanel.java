package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends FieldAction {

    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        try {
            Space target = gameController.board.getNeighbour(space, heading.next());
            gameController.moveToSpace(space.getPlayer(), target, heading.next());
        } catch (GameController.ImpossibleMoveException e){
            // catching exception.
        }
        return false;
    }
}
