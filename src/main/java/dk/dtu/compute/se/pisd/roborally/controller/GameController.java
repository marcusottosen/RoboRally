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

import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.*;
import org.jetbrains.annotations.NotNull;


/**
 * Den primære logik af selve spillet findes sted i GameController.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;
    private Wall wall;

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
     * Starter programmeringsfasen, sætter antal spiller=0 og steps=0
     * Gør eventuelle kort visuelle for alle brugere samt giver random kort vha. generateRandomCommandCard().
     */
    public void startProgrammingPhase() {
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
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * Finder random kort som bliver givet vha. StartProgrammingPhase().
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

            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                Command userChoice = board.getUserChoice();
                if (userChoice != null){
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

        /* Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null &&
                board.getPhase() == Phase.PLAYER_INTERACTION &&
                option != null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, option);

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            //Sætter næste spillers tur.
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep() + 1;
                //Tjekker om der er flere spillere i runden og starter ny runde hvis alle har fået sin tur.
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else { //Start ny programmeringsfase når alle runder er færdige.
                    startProgrammingPhase();
                }
            }
            //Fortsætter runden til alle kort er spillet, eller til næste interaction kort.
            if (board.getPhase() == Phase.ACTIVATION && !board.isStepMode()) {
                continuePrograms();
            }
        } else {
            assert false;
        }*/
    }


    /**
     * Overfører kortets navn til kortets funktion og udfører metoden til kortet.
     * @param player Spillerens objekt
     * @param command Objekt af kommandokortet.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

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
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case UTURN:
                    this.uTurn(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Rykker spilleren et felt frem ved at oprette en variable "target", som specificerer hvor spilleren skal rykke sig hen.
     * Der bliver tjekket om der står en person på feltet i forvejen, og evt. kalder pushPlayer() metoden hvis dette er true.
     * @param player Spillerens objekt.
     *//*
    public void forward1(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            //Vi opretter en variable "target", som specificerer hvor spilleren skal rykke sig hen.
            Space target = board.getNeighbour(current, player.getHeading());
            //Vi tjekker om der står en person på feltet i forvejen. Gør der ikke det, så eksekverer vi koden
            if (target != null && target.getPlayer() == null) {
                try {
                    player.setSpace(target);
                    isSpecialSpace(player); //tjekker player's felt for et specielt felt.

            } catch (ImpossibleMoveException e){ //Hvis der står en spiller på feltet i forvejen.
                    //Vi opretter en ny target spiller, som bruges til at finde ud af hvem der står på feltet.
                    Player targetPlayer = target.getPlayer();
                    //pushPlayer bruges til at skubbe den nye spiller
                    pushPlayer(targetPlayer, player);
                    // Rykker til sidst spilleren over på feltet, efter targetPlayer har rykket sig af vejen.
                    player.setSpace(target);

                    //isSpecialSpace(targetPlayer); //tjekker targetplayer's felt for specielt felt.
                }
            }
        }
    }

    *//**
     * Når en spiller bliver skubbet.
     //* @param targetPlayer Spilleren der bliver skubbet til.
     * @param player spiller der skubber
     *//*
    public void pushPlayer(@NotNull Player targetPlayer, @NotNull Player player) {
        Space target = board.getNeighbour(targetPlayer.getSpace(), player.getHeading());
        if (target != null && target.getPlayer() == null) {
            targetPlayer.setSpace(target);
            isSpecialSpace(targetPlayer);
        }else { //Hvis der står en spiller på feltet i forvejen.
            //Vi opretter en ny target spiller, som bruges til at finde ud af hvem der står på feltet.
            Player newTargetPlayer = target.getPlayer();
            //pushPlayer bruges til at skubbe den nye spiller
            pushPlayer(newTargetPlayer, player);
            // Rykker til sidst spilleren over på feltet, efter targetPlayer har rykket sig af vejen.
            targetPlayer.setSpace(target);

            //isSpecialSpace(targetPlayer); //tjekker targetplayer's felt for specielt felt.
        }

    }*/

    public void forward1(@NotNull Player player) {
        wall = new Wall(board);
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();
            spaceActionInit(player.getSpace());


            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
                    // we don't do anything here  for now; we just catch the
                    // exception so that we do no pass it on to the caller
                    // (which would be very bad style).
                }
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

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
