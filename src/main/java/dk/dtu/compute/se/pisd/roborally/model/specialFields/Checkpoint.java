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
 * Til objekt af Checkpoint. Indeholder get og setspace
 *
 * @author Victor Kongsbak & Marcus Ottosen
 */

public class Checkpoint extends Subject {

    final public Board board;
    private Space space;

    //x og y koordinaterne bør vælges tilfældigt indenfor spillepladen og ikke som de er nu.
    private int x;
    private int y;
    //private final Space[][] spaces = new Space[x][y];


    public Checkpoint(@NotNull Board board){
        this.board = board;

        //Bestemmer hvilket felt vi skal arbejde i (altså hvor checkpointet skal være på brættet.
        space = board.getSpace(x,y);
    }


    /**
     * Viser et checkpoint på spillepladen
     * @param board er spillepladen
     * @param mainBoardPane er vores valgte felt
     */
    /*public void showCheckpoint(@NotNull Board board, @NotNull GridPane mainBoardPane){

        SpaceView addCheckpoint = new SpaceView(space);
        addCheckpoint.viewCheckpoint();
        //vælger samme space på board, for at tilføje checkpoint.
        mainBoardPane.add(addCheckpoint,x,y);
    }*/


    /**
     * Returnerer lokationen af checkpointet.
     * @return Lokationen af checkpointet.
     */
    public Space getSpace(){
        //return spaces[x][y];
        return space;
    }

    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setCheckpoint(null);
            }
            if (space != null) {
                space.setCheckpoint(this);
            }
            notifyChange();
        }
    }


    //bør også laves en setSpace()
    /*public Space setSpace(int x, int y){
        this.x = x;
        this.y = y;
        return space;
    }*/



    /**
     * metode til at teste om den nuværende spiller står på et checkpoint
     * @param player objekt af spilleren
     * @return true hvis spilleren befinder sig på feltet.
     */
    public boolean checkCheckpoint(Player player){
        Space current = player.getSpace();
        if (current == space && space == null){
            return true;
        }
        else{
            return false;
        }
    }
}
