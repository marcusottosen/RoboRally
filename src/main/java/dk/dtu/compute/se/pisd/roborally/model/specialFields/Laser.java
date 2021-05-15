package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controllerer laserens funktionalitet
 *
 * @author Victor Kongsbak
 * @author Marcus Ottosen
 */
public class Laser extends FieldAction {
    private Heading heading;

    final static public List<Heading> laserHeading = new ArrayList<>();

    //laserSpaces indeholder koordinaterne på alle de felter en laser skal vises på.
    final static public List<Space> laserSpaces = new ArrayList<>();


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





    /**
     * Range of laser
     * @param board objektet
     */
    public void laserRange(Board board){
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                Space oldspace;
                Space newspace = space;
                Wall wall = new Wall(board);
                if(space.getActions().size() != 0) {
                    for (int i = 0; i < space.getActions().size(); i++) {
                        FieldAction actionType = space.getActions().get(i);
                        if (actionType instanceof Laser){

                            //System.out.println(space.getActions().toString() + " " + i);
                            System.out.println("laser " + i + ": " + space.x + ", " + space.y);

                            if (((Laser) actionType).getHeading() == Heading.WEST || ((Laser) actionType).getHeading() == Heading.NORTH){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    //System.out.println(newspace.x + ", " + newspace.y);
                                    newspace = board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.x+1 != board.width && oldspace.y+1 != board.height && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }else if (((Laser) actionType).getHeading() == Heading.EAST){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    newspace = board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.x != 0 && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }else if (((Laser) actionType).getHeading() == Heading.SOUTH){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    newspace = oldspace.board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.y != 0 && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Heading> getLaserHeading(){
        return laserHeading;
    }

    public List<Space> getLaserSpaces(){
        return laserSpaces;
    }
}
