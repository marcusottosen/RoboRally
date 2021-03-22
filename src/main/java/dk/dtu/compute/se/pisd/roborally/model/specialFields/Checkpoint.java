package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

public class Checkpoint {


    public Checkpoint(){

    }

    public void showCheckpoint(Board board, GridPane mainBoardPane){
        //Tester at tilføje en grøn cirkel(checkpoint)
        Space checkpoint = board.getSpace(3,3);
        SpaceView addCheckpoint = new SpaceView(checkpoint);
        addCheckpoint.viewCheckpoint();
        //vælger samme space på board, for at tilføje væggen.
        mainBoardPane.add(addCheckpoint,3,3);
    }
}
