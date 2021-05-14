package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Laser;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;

import java.util.ArrayList;
import java.util.List;

public class LaserRange {

    final public static List<Heading> laserHeading = new ArrayList<>();
    final public static List<Space> laserSpaces = new ArrayList<>();


    /**
     * Range of laser
     * @param board objektet
     */
    public LaserRange(Board board){
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
                            System.out.println("start");
                            if (((Laser) actionType).getHeading() == Heading.WEST || ((Laser) actionType).getHeading() == Heading.NORTH){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
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

    public static List<Heading> getLaserHeading(){
        return laserHeading;
    }

    public static List<Space> getLaserSpaces(){
        return laserSpaces;
    }

}
