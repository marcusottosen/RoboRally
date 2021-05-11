package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;

/**
 * Håndterer aktionen af hver laser.
 * Fungerer endnu ikke.
 */
public class Laser extends FieldAction{

    private Heading heading;


    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }

    /*public void shootLaser(Space space, Heading heading) {
        if (space.getActions().size() != 0){
            for (int i = 0; i < space.getActions().size(); i++){
                FieldAction laser = space.getActions().get(i);
                if (laser instanceof Laser){
                    Space target = space.board.getNeighbour(space, heading);
                    //SpaceView view = new SpaceView(space, gameController.board.height);
                    //view.viewLaser(target, heading);
                }
            }
        }
    }*/
}
