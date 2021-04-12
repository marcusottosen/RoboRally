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

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Spillerne af spillet (robotterne).
 * Viser bl.a. farver, navn, placering og retning.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;

    private int score = 0;
    private int health = 0;



    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * Returnerer navnet af spilleren i dette objekt.
     *
     * @return Navnet som string.
     */
    public String getName() {
        return name;
    }

    /**
     * Hvis navnet ønskes ændret bruges denne metode.
     *
     * @param name String af det navn man ønsker spillerens navn ændret til.
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Bruges til at finde spillerens farve.
     *
     * @return Spillerens farve som String.
     */
    public String getColor() {
        return color;
    }

    /**
     * Hvis farven ønskes ændret bruges denne metode.
     *
     * @param color String af den farve man ønsker spillerens farve ændret til.
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Spillerens placering på kortet.
     *
     * @return Spillerens placering.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Bruges til at ændre spillerens placering, så længe den placering ikke er null.
     *
     * @param space Det felt man ønsker spilleren rykket til.
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * Returnerer retningen som spilleren vender som enten: NORTH, SOUTH, EAST eller WEST.
     *
     * @return spillerens retning.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Skifter spillerens heading til det der er indtastet i parameteren.
     *
     * @param heading Den ønskede heading.
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * returnerer spillerens score.
     *
     * @return spillerens score som int.
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public CommandCardField getCardField(int i) {
        return cards[i];
    }




    public int getHealth() {return health;}
    public void setHealth(int health) {this.health = health;}


    }
