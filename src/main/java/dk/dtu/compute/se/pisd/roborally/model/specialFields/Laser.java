package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;

import java.util.List;

/**
 * Håndterer aktionen af hver laser.
 * Fungerer endnu ikke.
 */
public class Laser extends FieldAction{

    private Heading heading;
    final public static List<Space> laserSpaces = BoardView.laserSpaces;


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

    public static void laserDamage(){
        //Space playerLocation = player.getSpace();
        /*for (Space playerLocation : laserSpaces){
            playerLocation = player.getSpace();
            if (playerLocation == laserSpaces){
                player.takeHealth(1);
            }
        }*/
        for (int i = 0; i < laserSpaces.size(); i++){
            Player player = laserSpaces.get(i).getPlayer();
            if (player != null){
                player.takeHealth(1);
            }

        }
    }

}
