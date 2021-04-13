package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import org.jetbrains.annotations.NotNull;

public class Wall extends Subject {
    final public Board board;
    private Space space;
    private Heading direction;
    private Heading otherDirection;
    private Player player;


    public Wall(@NotNull Board board){
        this.board = board;

    }

    public boolean checkForWall(Player player){
        space = player.getSpace();
        direction = player.getHeading();
        otherDirection = direction.next().next(); //finder den omvendte heading, så vi kan sammenligne med nabofeltets evt. wall
        Space space2 = board.getNeighbour(space, direction); //Finder nabofeltet(skal bruges til at finde ud af om der er en væg på nabofeltet.

        for (Heading wall : space.getWalls()) {
            if (!space.getWalls().isEmpty()) {
                if (wall == direction) {
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        for (Heading wall : space2.getWalls()){
            if (!space2.getWalls().isEmpty()) {
                if (wall == otherDirection) {
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
        }



    /**
     * Væggens placering på kortet.
     * @return Væggens placering.
     */
    public Space getSpace(){
        return space;
    }

    /**
     * Returnerer væggens retning
     * @return væggens retning
     */

    public Heading getDirection(){
        return direction;
    }


    /**
     * Sætter væggens retning til retningen i parameteren.
     * @param direction væggens nye retning.
     */

    public void setDirection(Heading direction){
        if (direction != this.direction) {
            this.direction = direction;
        }
    }

}
