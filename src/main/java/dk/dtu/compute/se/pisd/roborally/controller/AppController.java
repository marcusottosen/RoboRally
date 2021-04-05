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
package dk.dtu.compute.se.pisd.roborally.controller;

//import dk.dtu.compute.se.pisd.roborally.dal.*;

import com.sun.jdi.connect.Connector;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import dk.dtu.compute.se.pisd.roborally.model.specialFields.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;


    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Når et nyt spil startes spørger programmet om antal spillere (int) i form at en Choice Dialog.
     * Opretter ydermere boarded på fx 8x8.
     * Sætter farverne på spillerne ud fra et array af farverne.
     * Sætter spillernes navne til hhv. player1, player2, player3 osv.
     * Sætter spillerne på boarded 1 height felt og 1 width felt fra den tidligere.
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board = new Board(8,8);
            gameController = new GameController(board);

            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));

            }

            Checkpoint checkpoint1 = new Checkpoint(board);
            board.addCheckpoint(checkpoint1);
            //checkpoint1.showCheckpoint(board);
            checkpoint1.setSpace(board.getSpace(3,5));

            Checkpoint checkpoint2 = new Checkpoint(board);
            board.addCheckpoint(checkpoint2);
            //checkpoint1.showCheckpoint(board);
            checkpoint2.setSpace(board.getSpace(1,0));


            //FieldAction fieldAction = new ConveyorBelt();

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
        }
    }

    /**
     * Til at gemme spillet
     */
    public void saveGame() {
        RepositoryAccess save = new RepositoryAccess();
        save.getRepository().createGameInDB(gameController.board);
        System.out.println("save test");
    }

    /**
     * Til at loade et tidligere gemt spil.
     */
    public void loadGame() {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        /*if (gameController == null) {
            newGame();
        }*/

        RepositoryAccess load = new RepositoryAccess();
        load.getRepository().loadGameFromDB(6);
        gameController = new GameController(load.getRepository().loadGameFromDB(6));
        roboRally.createBoardView(gameController);

    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Når en bruger ønsker at forlade spillet bliver brugeren spurgt om personen er helt sikker på dette.
     * Hvis der ikke bliver svaret cancel, vil applikationen exit.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * Hvis gamecontroller kører, er spillet igang.
     * @return boolean.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
