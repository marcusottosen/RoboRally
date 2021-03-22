package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;

public class Walls{

    public Walls() {


    }

    /**
     * Laver en mur på spillepladen. I det her tilfælde på den "nordlige" kant i fæltet (5,5)
     * @param board er spillepladen
     * @param mainBoardPane er vores valgte felt
     */
    public void showWalls(Board board, GridPane mainBoardPane) {
        //Disse variable bør være tilfældige tal på spilpladen. 5,5 er valgt for at teste at det virker.
        final int x = 5;
        final int y = 5;

        //vælger et space på board som vi arbejder med
        Space wallSpace = board.getSpace(x,y);

        SpaceView addWall = new SpaceView(wallSpace);
        addWall.viewLine("NORTH");
        //vælger samme space på board, for at tilføje væggen.
        mainBoardPane.add(addWall,x,y);
    }



}
