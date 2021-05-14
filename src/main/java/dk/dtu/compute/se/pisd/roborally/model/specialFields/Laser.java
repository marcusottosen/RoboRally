package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.EnergyCubeTypes;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;

import java.util.List;

/**
 * Controllerer laserens funktionalitet
 *
 * @author Victor Kongsbak
 * @author Marcus Ottosen
 */
public class Laser extends FieldAction {
    private Heading heading;
    final public static List<Space> laserSpaces = BoardView.laserSpaces;


    /**
     * returnerer laserens heading.
     *
     * @return laserens heading som Heading.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sætter laserens Heading til den i parameteren.
     *
     * @param heading den Heading man ønsker laseren skal have.
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * Laser som fjerner et liv fra spilleren.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return hvorvidt doAction blev udført.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return true;
    }

    /**
     * Fjerner et liv fra spilleren som laseren rammer, såvidt spilleren ikke har et deflector shield.
     */
    public static void laserDamage() {
        for (Space laserSpace : laserSpaces) {
            Player player = laserSpace.getPlayer();
            if (player != null) {
                if (player.getEnergyCubesOptained().contains(EnergyCubeTypes.DEFLECTORSHIELD)) {
                    player.removeOptainedEnergyCube(EnergyCubeTypes.DEFLECTORSHIELD);
                } else
                    player.takeHealth(1);
            }
        }
    }
}
