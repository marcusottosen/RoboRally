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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    //private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();
    //private final List<ConveyorBelt1> conveyorBelt1s = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * Sætter højden og bredden af pladen.
     * @param width angivet i antal felter.
     * @param height angivet i antal felter.
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * returnerer gameID.
     * @return i form af interger.
     */
    public Integer getGameId() {
        return gameId;
    }

    public void setBoardName(String boardName){
        this.boardName = boardName;
    }
    public String getBoardName(){
        return boardName;
    }
    /**
     * Sætter spillets id til det der bliver skrevet i parameteren, hvis den ikke er null.
     * Bruges til at gemme og loade spillet. gameID kan blive set some primary key.
     * @param gameId spillets ID som int.
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * bruges til at returnere et felts x og y.
     * @param x int x-koordinat.
     * @param y int y-koordinat.
     * @return feltets x og y koordinater.
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * Bruges til at finde antallet at spillere ved at tjekke størrelsen på arraylisten Players.
     * @return størrelsen som int.
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * bruges til at tilføje en spiller
     * @param player player objektet.
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }



    /**
     * Væggen i parameteren bliver tilføjet til arraylisten over walls.
     * @param wall checkpoint.
     */
    public void addWall(@NotNull Wall wall){
        walls.add(wall);
        notifyChange();
    }

    /**
     * Bruges til at få fat i en bestemt spiller på.
     * @param i nummeret på spilleren man vil have fat på.
     * @return player object.
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Bruges til at finde den nuværende spiller.
     * @return player objekt.
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sætter en spiller som den nuværende. Bruges når der skal skiftes mellem spillere.
     * @param player player objekt.
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Bruges til at få fat i den nuværende fase af spillet.
     * @return spillets fase som objekt.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sætter spillets fase til en af de 4 faser.
     * @param phase objekt af Phase. Inder kun de 4 faser som enum.
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * Finder spillets nuværende step. Et step stiger for hvert kort der bliver brugt.
     * @return nuværende step som int.
     */
    public int getStep() {
        return step;
    }

    /**
     * Sætter spillets step til det i parameteren.
     * @param step den int man ønsker spillets step skal være lig.
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Returnerer hvorvidt om spillet er i step mode eller ej.
     * @return boolean. TRUE hvis stepMode.
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Sætter spillets stepmode svarende til den boolean der bliver angivet i parameteren.
     * @param stepMode boolean af det ønskede stepmode.
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Returnerer spillerens nummer som int.
     * @param player objekt af den spiller man ønsker nummeret på.
     * @return Spillerens nummer som int.
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    //PiSU P08.2
    private Command userChoice = null;

    public Command getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Command userChoice) {
        if (this.userChoice != userChoice) {
            this.userChoice = userChoice;
            notifyChange();
        }
    }

    /**
     * En kort tekst i bunden af vinduet som angiver: spillets fase, den nuværende spillers navn, steps samt spillerens score.
     * @return spillets fase, den nuværende spillers navn, steps samt spillerens score som en string.
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() + ", Step: " + getCount();
    }

    /**
     * Counts the number of moves in the game
     */
    private int count;

    /**
     * Returnerer spillets nuværende count.
     * @return count som int.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sætter spillets count til den i parameteren.
     * @param count den int man ønsker count skal være lig.
     */
    public void setCount(int count) {
        if (this.count != count) {
            this.count = count;
            notifyChange();
        }
    }




}
