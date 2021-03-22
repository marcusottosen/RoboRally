package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

public class Walls{
    private Board board;
    private Space space;
    private int x;
    private int y;
    private String direction;

    public Walls(@NotNull GameController gameController, int x, int y, String direction) {
        board = gameController.board;
        this.x = x;
        this.y = y;
        this.direction = direction;
        space = board.getSpace(x,y);
    }


    /**
     * Laver en mur på spillepladen. I det her tilfælde på den "nordlige" kant i fæltet (5,5)
     * @param board er spillepladen
     * @param mainBoardPane er vores valgte felt
     */
    public void showWalls(@NotNull Board board, @NotNull GridPane mainBoardPane) {
        //vælger et space på board som vi arbejder med
        //Space wallSpace = board.getSpace(x,y);
        Space wallSpace = space;

        SpaceView addWall = new SpaceView(wallSpace);
        addWall.viewLine(direction);
        //vælger samme space på board, for at tilføje væggen.
        mainBoardPane.add(addWall,x,y);
    }


    /**
     * Returnerer væggens retning
     * @return væggens retning
     */
    public String getDirection(){
        return direction;
    }

    /**
     * Sætter væggens retning til retningen i parameteren.
     * @param direction væggens nye retning.
     */
    public void setDirection(String direction){
        if (direction != this.direction) {
            this.direction = direction;
        }
    }

    /**
     * Væggens placering på kortet.
     * @return Væggens placering.
     */
    public Space getSpace(){
        return space;
    }

    /**
     * Hvis placeringen af væggen ønskes ændret kan denne metode bruges.
     * @param space den nye placering af væggen.
     */
    public void setSpace(Space space){
        Space oldSpace = this.space;
        if (space != oldSpace && space == null){
            this.space = space;
        }
    }
}