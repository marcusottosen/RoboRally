package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

public class Checkpoint {

    private Board board;
    private Space space;

    //x og y koordinaterne bør vælges tilfældigt indenfor spillepladen og ikke som de er nu.
    private final int x = 5;
    private final int y = 3;
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
    public void showCheckpoint(@NotNull Board board, @NotNull GridPane mainBoardPane){

        SpaceView addCheckpoint = new SpaceView(space);
        addCheckpoint.viewCheckpoint();
        //vælger samme space på board, for at tilføje checkpoint.
        mainBoardPane.add(addCheckpoint,x,y);
    }

    /**
     * Returnerer lokationen af checkpointet.
     * @return Lokationen af checkpointet.
     */
    public Space getSpace(){
        //return spaces[x][y];
        return space;
    }

    //bør også laves en setSpace()
    /*public Space setSpace(int x, int y){
        this.x = x;
        this.y = y;
        return space;
    }*/


    //Har lavet en "isSpecialSpace" metode i GameController som tjekker for alle specielle felter.
    //Lavet int score, getScore() og setScore() i Player, så hver spiller har nu en score.
    //Ændret på konstruktøren så den nu indeholder x og y som parametre,
    // så dette kan sættes når objektet bliver oprettet. (ligenu oprettet i BoardView)



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
