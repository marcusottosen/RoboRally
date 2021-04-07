package dk.dtu.compute.se.pisd.roborally.model.specialFields;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

/**
 * Til objekt af conveyorbelt. Indeholder get og setspace samt get og setDirection
 *
 * @author Gruppe 5
 */

public class ConveyorBelt1 extends Subject {

    final public Board board;
    private Space space;
    private String direction;
    private int x;
    private int y;


    public ConveyorBelt1(@NotNull Board board){
        this.board = board;

        //Bestemmer hvilket felt vi skal arbejde i (altså hvor ConveryorBelt skal være på brættet.
        space = board.getSpace(x,y);
    }


    /**
     * Returnerer lokationen af conveyorbelt.
     * @return Lokationen af conveyorbelt.
     */
    public Space getSpace(){
        return space;
    }

    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setConveyorBelt(null);
            }
            if (space != null) {
                space.setConveyorBelt(this);
            }
            notifyChange();
        }
    }

    /**
     * metode til at teste om den nuværende spiller står på et conveyorbelt
     * @param player objekt af spilleren
     * @return true hvis spilleren befinder sig på feltet.
     */
    public boolean checkConveyorBelt(Player player){
        Space current = player.getSpace();
        if (current == space && space == null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Returnerer conveyorbelt retning
     * @return conveyorbelt retning
     */
    public String getDirection(){
        return direction;
    }


    /**
     * Sætter conveyorbelt retning til retningen i parameteren.
     * @param direction conveyorbelt nye retning.
     */

    public void setDirection(String direction){
        if (direction != this.direction) {
            this.direction = direction;
        }
    }
}
