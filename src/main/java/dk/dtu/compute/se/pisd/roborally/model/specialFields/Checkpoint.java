package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;

public class Checkpoint {

    private Board board;
    private Space space;

    //x og y koordinaterne bør vælges tilfældigt indenfor spillepladen og ikke som de er nu.
    final int x = 3;
    final int y = 3;
    private final Space[][] spaces = new Space[x][y];


    public Checkpoint(GameController gameController){
        board = gameController.board;

    }


    public void showCheckpoint(Board board, GridPane mainBoardPane){
        //Tester at tilføje en grøn cirkel(checkpoint)
        Space checkpoint = board.getSpace(x,y);
        SpaceView addCheckpoint = new SpaceView(checkpoint);
        addCheckpoint.viewCheckpoint();
        //vælger samme space på board, for at tilføje væggen.
        mainBoardPane.add(addCheckpoint,x,y);

    }

    /**
     *
     * @param x x koordinaten af hvor checkpointet er.
     * @param y y koordinaten af hvor checkpointet er.
     * @return Lokationen af checkpointet.
     */
    public Space getSpace(int x, int y){
        return spaces[x][y];
    }

    //metode til at teste om den nuværende spiller står på et checkpoint
    public static void checkCheckpoint(Player player){
        Space current = player.getSpace();
    }
}
