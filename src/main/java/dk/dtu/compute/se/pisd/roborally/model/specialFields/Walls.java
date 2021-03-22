package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

public class Walls{
    private GridPane mainBoardPane;

    public Walls(Board board, GridPane mainBoardPane) {

        //Disse variable bør være tilfældige tal på spilpladen. 5,5 er valgt for at teste at det virker.
        int x = 5;
        int y = 5;

        Space wallSpace = board.getSpace(x,y);
        SpaceView addWall = new SpaceView(wallSpace);
        addWall.viewLine("NORTH");
        mainBoardPane.add(addWall,x,y);

    }


}
