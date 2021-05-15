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


import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
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
 * AppControlleren står hovedsageligt for dialogen mellem spillet og brugeren når der skal vælges nyt spil, load, stop og exit.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<String> BOARD_NAMES = Arrays.asList("Board1", "Board2");

    final private RoboRally roboRally;

    private GameController gameController;
    private Board board = null;

    /**
     * Konstruktøren til AppControlleren.
     *
     * @param roboRally roborally
     */
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Når et nyt spil startes spørger programmet om antal spillere (int) i form at en Choice Dialog.
     * Opretter ydermere en ny gamecontroller med et board.
     * Sætter farverne på spillerne ud fra et array af farverne.
     * Sætter spillernes navne til hhv. player1, player2, player3 osv.
     * Sætter spillerne på boarded 1 height felt og 1 width felt fra den tidligere.
     */
    public void newGame() {
        this.board = null;
        this.gameController = null;
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            ChoiceDialog<String> chooseBoard = new ChoiceDialog<>(BOARD_NAMES.get(0), BOARD_NAMES);
            chooseBoard.setTitle("Board name: ");
            chooseBoard.setHeaderText("Select board");
            Optional<String> boards = chooseBoard.showAndWait();

            if (boards.isPresent()) {
                if (gameController != null) {
                    // The UI should not allow this, but in case this happens anyway.
                    // give the user the option to save the game or abort this operation!
                    if (!stopGame()) {
                        return;
                    }
                }
                Board board = LoadBoard.loadBoard(boards.get());

                gameController = new GameController(board);

                int no = result.get();
                for (int i = 0; i < no; i++) {
                    Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                    board.addPlayer(player);
                    LoadBoard.loadPlayer(player, i);
                }

                gameController.startProgrammingPhase();
                roboRally.createBoardView(gameController);

                //Database
                String chosen_board = boards.get();
                board.setBoardName(chosen_board);
            }
        }
    }

    /**
     * Til at gemme spillet
     */
    public void saveGame() {
        if (gameController.board.getGameId() != null) {
            RepositoryAccess save = new RepositoryAccess();
            save.getRepository().updateGameInDB(gameController.board);
        } else {
            RepositoryAccess update = new RepositoryAccess();
            update.getRepository().createGameInDB(gameController.board);
        }
    }

    /**
     * Til at loade et tidligere gemt spil.
     */
    public BoardView loadGame() {
        RepositoryAccess loadgame = new RepositoryAccess();
        List<GameInDB> savegames = loadgame.getRepository().getGames();

        ChoiceDialog load_dialog = new ChoiceDialog();
        load_dialog.setContentText("Chose savegame to load");
        load_dialog.getItems().addAll(savegames);

        if (load_dialog.showAndWait().isPresent()) {
            board = loadgame.getRepository().loadGameFromDB(((GameInDB) load_dialog.getSelectedItem()).id);
            gameController = new GameController(board);
            roboRally.createBoardView(gameController);
        }
        return null;
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
     *
     * @return boolean.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        //Do nothing
    }
}
