package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
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
    private int x;
    private int y;
    private final Space[][] spaces = new Space[x][y];


    public Checkpoint(@NotNull GameController gameController, int x, int y){
        board = gameController.board;
        this.x = x;
        this.y=y;
        space = board.getSpace(x,y);
    }


    public void showCheckpoint(@NotNull Board board, @NotNull GridPane mainBoardPane){
        //Tester at tilføje en grøn cirkel(checkpoint)
        Space checkpoint = board.getSpace(x,y);
        SpaceView addCheckpoint = new SpaceView(checkpoint);
        addCheckpoint.viewCheckpoint();
        //vælger samme space på board, for at tilføje væggen.
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
