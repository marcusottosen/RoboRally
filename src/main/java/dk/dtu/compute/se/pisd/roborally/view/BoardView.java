/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Laser;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * BoardView implementere GameController til spillepladen.
 * Giver spillerene mulighed for at rykke rundt på spillepladen.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class BoardView extends VBox implements ViewObserver {

    private final Board board;

    public static GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private PlayersView playersView;

    private final Label statusLabel;

    private final HBox infoView;
    private final InfoView view;



    /***
     * Konstruktøren.
     * Tilføjer inforView, mainBoardPane, playersView & statusLabel til boarded, så de alle bliver vist.
     * @param gameController The GameController of the given game
     */
    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");

        infoView = new HBox();
        view = new InfoView(infoView, board);

        this.getChildren().add(infoView);
        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];
        SpaceEventHandler spaceEventHandler = new SpaceEventHandler(gameController);

        //initiate the lasers for the given board. This is done before the SpaceView is created, to be able to prep the lasers range.
        Laser laser = new Laser();
        laser.laserRange(board);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space, board.height);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }
        board.attach(this);
        update(board);
    }

    /**
     * Range of laser
     *
     * @param board objektet
     */
    /*public void laserRange(Board board){
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                Space oldspace;
                Space newspace = space;
                Wall wall = new Wall(board);
                if(space.getActions().size() != 0) {
                    for (int i = 0; i < space.getActions().size(); i++) {
                        FieldAction actionType = space.getActions().get(i);
                        if (actionType instanceof Laser){
                            System.out.println("start");
                            if (((Laser) actionType).getHeading() == Heading.WEST || ((Laser) actionType).getHeading() == Heading.NORTH){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    newspace = board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.x+1 != board.width && oldspace.y+1 != board.height && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }else if (((Laser) actionType).getHeading() == Heading.EAST){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    newspace = board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.x != 0 && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }else if (((Laser) actionType).getHeading() == Heading.SOUTH){
                                Heading heading = ((Laser) actionType).getHeading();
                                do {
                                    oldspace = newspace;
                                    laserHeading.add(heading);
                                    laserSpaces.add(newspace);
                                    newspace = oldspace.board.getNeighbour(oldspace, ((Laser) actionType).getHeading().next().next());
                                }while(oldspace.y != 0 && wall.isWall(heading, newspace) && wall.isWall(heading.next().next(), oldspace));
                            }
                        }
                    }
                }
            }
        }
    }*/

    /**
     * Checks to see if subject is equal to board phase
     *
     * @param subject subject objektet
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            statusLabel.setText(board.getStatusMessage());
            view.updateBox(infoView, board.getCurrentPlayer()); //Opdaterer infobaren i toppen
        }
    }

        private class SpaceEventHandler implements EventHandler<MouseEvent> {
        final public GameController gameController;

        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

        /**
         * the "event" that happens when the game is initialized
         *
         * @param event objekt af MouseEvent
         */
        @Override
        public void handle(MouseEvent event) { //TODO Fjern denne metode
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board) {
                    gameController.moveCurrentPlayerToSpace(space);
                    event.consume();
                }
            }
        }
    }
}
