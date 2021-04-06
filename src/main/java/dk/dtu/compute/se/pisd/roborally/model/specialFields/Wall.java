package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Wall extends Subject {
    final public Board board;
    private Space space;
    private int x;
    private int y;
    private String direction;


    public Wall(@NotNull Board board){
        this.board = board;

        space = board.getSpace(x,y);

    }



    public Space getSpace(){
        return space;
    }

    /*public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setWall(null);
            }
            if (space != null) {
                space.setWall(this);
            }
            notifyChange();
        }
    }*/

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






}

/*
    public Walls(@NotNull GameController gameController, int x, int y, String direction) {
        board = gameController.board;
        this.x = x;
        this.y = y;
        this.direction = direction;
        space = board.getSpace(x,y);
    }


    */
/**
 * Laver en mur på spillepladen. I det her tilfælde på den "nordlige" kant i fæltet (5,5)
 * @param board er spillepladen
 * @param mainBoardPane er vores valgte felt
 *//*

    public void showWalls(@NotNull Board board, @NotNull GridPane mainBoardPane) {
        //vælger et space på board som vi arbejder med
        //Space wallSpace = board.getSpace(x,y);
        Space wallSpace = space;

        SpaceView addWall = new SpaceView(wallSpace);
        addWall.viewLine(direction);
        //vælger samme space på board, for at tilføje væggen.
        mainBoardPane.add(addWall,x,y);
    }


    */
/**
 * Returnerer væggens retning
 * @return væggens retning
 *//*

    public String getDirection(){
        return direction;
    }

    */
/**
 * Sætter væggens retning til retningen i parameteren.
 * @param direction væggens nye retning.
 *//*

    public void setDirection(String direction){
        if (direction != this.direction) {
            this.direction = direction;
        }
    }

    */
/**
 * Væggens placering på kortet.
 * @return Væggens placering.
 *//*

    public Space getSpace(){
        return space;
    }

    */
/**
 * Hvis placeringen af væggen ønskes ændret kan denne metode bruges.
 * @param space den nye placering af væggen.
 *//*

    public void setSpace(Space space){
        Space oldSpace = this.space;
        if (space != oldSpace && space == null){
            this.space = space;
        }
    }
}*/
