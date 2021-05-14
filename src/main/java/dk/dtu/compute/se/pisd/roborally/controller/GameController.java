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

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.*;
import dk.dtu.compute.se.pisd.roborally.view.InfoView;
import dk.dtu.compute.se.pisd.roborally.view.PopupView;
import dk.dtu.compute.se.pisd.roborally.view.LaserView;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;


/**
 * Den primære logik af selve spillet findes sted i GameController.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 * @author Frederik Nissen
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        Player current = board.getCurrentPlayer();
        if (space.getPlayer() == null && current != null) {
            current.setSpace(space);
            int number = board.getPlayerNumber(current);
            board.setCurrentPlayer(board.getPlayer((number + 1) % board.getPlayersNumber()));
            board.setCount(board.getCount() + 1);
        }
    }

    /**
     * Starter programmeringsfasen, skifter til den første spiller og sætter steps til 0
     * Gør eventuelle kort visuelle for alle brugere samt giver random kort vha. generateRandomCommandCard().
     */
    public void startProgrammingPhase() {
        LaserView.shootLaser();
        Laser.laserDamage();
        playerDeath(board);
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    if (player.getCardField(j).getCard() == null) { //Giver kun kort ved de brugte kort
                        CommandCardField field = player.getCardField(j);
                        field.setCard(generateRandomCommandCard());
                        field.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Finder random kort som bliver efterspurgt fra StartProgrammingPhase().
     * @return nyt random kommandokort
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Når programmeringsfasen er færdig gør den program fields usyndlige vha. makeProgramFieldsVisible()
     * Ænder derudover fasen til ACTIVATION.
     */
    public void finishProgrammingPhase() {
        LaserView.stopLaser();
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    /**
     * Bliver brugt i bl.a. finishProgrammingPhase til at enten vise (1) eller skjule (0) programming fields.
     * @param register int.
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * Bliver brugt i bl.a. finishProgrammingPhase til at skjule programming fields.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Bruges til knappen "execute program" og kører alle programkort igennem automatisk (udover option-kort)
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Bruges til knappen "execute current register" og kører kun det næste programkort.
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * Bruges ved executeProgram() og executeStep() og tjekker om fasen er ACTIVATION og spillet ikke er stepMode.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * Udfører kommandokortene, så længe der er et næste. Alle kortene bliver kørt igennem ved 1 kort pr. person af gangen.
     * Tjekker bl.a. om et kort kræver interaktion fra spilleren.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if ((board.getPhase() == Phase.ACTIVATION ||
                (board.getPhase() == Phase.PLAYER_INTERACTION
                        && board.getUserChoice() != null))
                && currentPlayer != null) {
            currentPlayer.intiateLaser();
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                Command userChoice = board.getUserChoice();
                if (currentPlayer.getHealth()>=1) {
                    if (userChoice != null) {
                        board.setUserChoice(null);
                        board.setPhase(Phase.ACTIVATION);
                        executeCommand(currentPlayer, userChoice);

                    } else {
                        CommandCard card = currentPlayer.getProgramField(step).getCard();
                        if (card != null) {
                            Command command = card.command;
                            //Afbryder eksekveringsløkken og giver spilleren et valg.
                            if (command.isInteractive()) {
                                board.setPhase(Phase.PLAYER_INTERACTION);
                                return;
                            }
                            executeCommand(currentPlayer, command);
                        }
                    }
                }

                isWinnerFound(currentPlayer);

                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }

            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Sætter næste spillers tur.
     * Tjekker om der er flere spillere i runden og starter en ny runde hvis alle har fået sin tur.
     * Runden fortsætter til alle kort er spillet, eller til der kræves en interaktion fra en spiller.
     * Starter en ny programmeringsfase når alle runder er færdige.
     * @param option .
     */
    public void executeCommandOptionAndContinue(@NotNull Command option) {
        assert board.getPhase() == Phase.PLAYER_INTERACTION;
        assert board.getCurrentPlayer() != null;
        board.setUserChoice(option);
        continuePrograms();
    }


    /**
     * Overfører kortets navn til kortets funktion og udfører metoden til kortet.
     * Hvis spilleren har EXTRAMOVE energyCuben, bliver der rykket en ekstra frem.
     * @param player Spillerens objekt
     * @param command Objekt af kommandokortet.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and TODO Forstå hvad ekkart vil her.
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            if (player.energyCubesOptained.contains(EnergyCubeTypes.EXTRAMOVE) &&
                    !((command == Command.RIGHT) || (command == Command.LEFT) || (command == Command.UTURN)
            || (command == Command.OPTION_LEFT_RIGHT))){
                forward1(player);}

            switch (command) {
                case FORWARD1:
                    this.forward1(player);
                    break;
                case FORWARD2:
                    this.forward2(player);
                    break;
                case FORWARD3:
                    this.forward3(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    spaceActionInit(player.getSpace());
                    break;
                case LEFT:
                    this.turnLeft(player);
                    spaceActionInit(player.getSpace());
                    break;
                case UTURN:
                    this.uTurn(player);
                    spaceActionInit(player.getSpace());
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    public void forward1(@NotNull Player player) {
        Wall wall = new Wall(board);
        if (player.board == board) {
            if ((player.getHealth() >= 1)) {
                Space space = player.getSpace();
                Heading heading = player.getHeading();

                Space target = board.getNeighbour(space, heading);
                if (target != null) {
                    try {
                        if (!wall.checkForWall(player)) {
                            if (target.getPlayer() != null) {
                                if (player.energyCubesOptained.contains(EnergyCubeTypes.MELEEWEAPON)){
                                    target.getPlayer().takeHealth(1);}
                                if (!wall.checkForWall(target.getPlayer())) {
                                    moveToSpace(player, target, heading);
                                } else if (wall.checkForWall(target.getPlayer())) {
                                    System.out.println("Der står en spiller i vejen, som ikke kan skubbes");
                                }
                            } else {
                                moveToSpace(player, target, heading);
                            }
                        } else if (wall.checkForWall(player)) {
                            System.out.println("Du kan ikke rykke igennem en væg");
                        }
                    } catch (ImpossibleMoveException e) {
                        // Catching exception
                    }
                }
                spaceActionInit(player.getSpace());
            }
        }
    }

    public void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        if (other != null){
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                // XXX Note that there might be additional problems with
                //     infinite recursion here (in some special cases)!
                //     We will come back to that!
                moveToSpace(other, target, heading);

                // Note that we do NOT embed the above statement in a try catch block, since
                // the thrown exception is supposed to be passed on to the caller
                assert target.getPlayer() == null : target; // make sure target is free now
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }

    public static class ImpossibleMoveException extends Exception {
        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Kan sku ikke rykke mig!");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    /**
     * Flytter spilleren frem 2 felter ved at kalde forward1 metoden 2 gange.
     * @param player Spillerens objekt.
     */
    public void forward2(@NotNull Player player) {
        forward1(player);
        forward1(player);
    }

    /**
     * Flytter spilleren frem 3 felter ved at kalde forward1 metoden 3 gange.
     * @param player Spillerens objekt.
     */
    public void forward3(@NotNull Player player) {
        forward1(player);
        forward1(player);
        forward1(player);
    }

    /**
     * Skifter spillerens heading til højre.
     * @param player Spillerens objekt.
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }


    /**
     * Skifter spilleren heading til venstre.
     * @param player Spillerens objekt.
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * Skifter spillerens heading to gange til højre og altså vender spilleren i den modsatte retning.
     * @param player Spillerens objekt.
     */
    public void uTurn(@NotNull Player player){
        player.setHeading(player.getHeading().next());
        player.setHeading(player.getHeading().next());
    }

    /**
     * Tjekker om kortet er et move card eller ej.
     * @param source CommandCardField
     * @param target CommandCardField
     * @return returnerer true/false
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tjekker hvorvidt en spillet har vundet.
     * Hvis en spiller har vundet, vises en ny boks som fortæller spillerne hvem der har vundet.
     * Derudover ændres fasen til FINISH som skjuler knapper og kort.
     * @param player spilleren der tjekkes.
     */
    public void isWinnerFound(Player player){
        if (player.getScore() >= 3 ){ //TODO 3-tallet bør ændres til antallet af checkpoints (ikke hard-coded)
            board.setPhase(Phase.FINISH);
            PopupView view = new PopupView();
            view.winningWindow(player);
        }
    }

    /**
     * tjekker hvorvidt feltet er specielt og initialiserer feltet.
     * @param space object af feltet
     */
    public void spaceActionInit(@NotNull Space space) {

        if (space.getActions().size() != 0) {
            FieldAction actionType = space.getActions().get(0);
            System.out.println(actionType);
            actionType.doAction(this, space);
        }
    }


    public void playerDeath(Board board){
        for (int i = 0; i < board.getPlayersNumber(); i++){
            int health = board.getPlayer(i).getHealth();
            Player player = board.getPlayer(i);
            if (health <= 0){
                int x = LoadBoard.template.spawns.get(i).x;
                int y = LoadBoard.template.spawns.get(i).y;
                player.setSpace(board.getSpace(x, y));
                player.setHealth(3);
                player.setHeading(Heading.SOUTH);
                player.getEnergyCubesOptained().clear();
                player.getCheckpointsCompleted().clear();
                player.setScore(0);
            }
        }
    }
}

