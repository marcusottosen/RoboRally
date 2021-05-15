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
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Sørger for at starte spillet op.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH = 600;
    private Stage stage;
    private BorderPane boardRoot;

    /**
     * Initialiserer spillet.
     *
     * @throws Exception hvis en error sker.
     */
    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * starter spillet ved oprettelse af AppController objektet.
     * Starter menubaren, og sætter bl.a. titlen af vinduet til "Roborally".
     * <p>
     * create the primary scene with the a menu bar and a pane for
     * the board view (which initially is empty); it will be filled
     * when the user creates a new game or loads a game
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        AppController appController = new AppController(this);
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();
                });
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Hvis boardRoot findes, fjern den gamle boardview.
     * Opretter et view for det nye board, og sætter et center.
     *
     * @param gameController gamercontroller objekt.
     */
    public void createBoardView(GameController gameController) {
        boardRoot.getChildren().clear();

        if (gameController != null) {
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
        stage.sizeToScene();
    }

    /**
     * Bruges ikke rigtigt da der i stedet bliver brugt exit().
     * Er dog i stand til at stoppe spillet igennem Application
     *
     * @throws Exception hvis error.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    /**
     * Spillets main. Kører launch.
     * Spillet bør kunne køres herfra, men fejler ofte - brug i stedet StartRoboRally dokumentet.
     *
     * @param args main args.
     */
    public static void main(String[] args) {
        launch(args);
    }

}