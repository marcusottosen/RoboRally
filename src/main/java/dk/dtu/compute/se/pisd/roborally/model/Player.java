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
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.EnergyCube;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Laser;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Spillerne af spillet (robotterne).
 * Viser bl.a. farver, navn, placering og retning.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private final CommandCardField[] program;
    private final CommandCardField[] cards;

    private int score = 0;
    public ArrayList<Integer> checkpointsCompleted = new ArrayList<Integer>();
    public int availableHealth = 3;
    private int health = 3;

    // Liste over spillerens energyCubes
    public ArrayList<EnergyCubeTypes> energyCubesOptained = new ArrayList<EnergyCubeTypes>();

    //Spillerens laser
    Laser playerLaser = new Laser();

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
     * @return Navnet som string.
     */
    public String getName() {
        return name;
    }

    /**
     * Hvis navnet ønskes ændret bruges denne metode.
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
     * @return Spillerens farve som String.
     */
    public String getColor() {
        return color;
    }

    /**
     * Hvis farven ønskes ændret bruges denne metode.
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
     * @return Spillerens placering.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Bruges til at ændre spillerens placering, så længe den placering ikke er null.
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
     * @return spillerens retning.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Skifter spillerens heading til det der er indtastet i parameteren.
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

    public ArrayList<Integer> getCheckpointsCompleted() {
        return checkpointsCompleted;
    }

    public void addCheckpointsCompleted(int checkpointNumber){
        checkpointsCompleted.add(checkpointNumber);
    }


    /**
     * returnerer spillerens score.
     * @return spillerens score som int.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sætter spillerens score til det i parameteren
     * @param score den score man ønsker spilleren skal have
     */
    public void setScore(int score) {
        this.score = score;
    }


    /**
     * Bruges til at returnere et programmeringskort
     * @param i Nummeret på kortet i listen
     * @return programmeringskort
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Bruges til at teturnere et kommandokort
     * @param i Nummeret på kortet i listen
     * @return kommandokort
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }


    /**
     * returnerer spillerens health.
     * @return spillerens health som int.
     */
    public int getHealth() {return health;}

    /**
     * Sætter spillerens health til det i parameteren
     * @param health den health man ønsker spilleren skal have
     */
    public void setHealth(int health) {this.health = health;}

    /**
     * Fjerner en specifikt mængde health fra spilleren - går ikke under 0
     * @param amount mængden af liv der skal trækkes fra spilleren.
     */
    public void takeHealth(int amount){
        if (health-amount > 0){
            health = health-amount;
        } else {
            health = 0;
        }
    }


    /**
     * Returnerer listen over hvilke energyCubes spilleren har
     * @return liste af spillerens energyCubes.
     */
    public ArrayList<EnergyCubeTypes> getEnergyCubesOptained() {
        return energyCubesOptained;
    }

    /**
     * Giver spilleren en energyCube hvis spilleren ikke allerede har den.
     * @param newCube er den type cube man ønsker spilleren skal have.
     */
    public void setOptainedEnergyCube(EnergyCubeTypes newCube){
        if (!energyCubesOptained.contains(newCube))
        energyCubesOptained.add(newCube);
    }

    /**
     * Fjerner den energyCube der skrives i attributten fra spilleren.
     * @param removeCube den cube man ønsker skal fjernes fra spilleren.
     */
    public void removeOptainedEnergyCube(EnergyCubeTypes removeCube){
        energyCubesOptained.remove(removeCube);
    }


    public void intiateLaser(){
        System.out.println("initiate");
        if (energyCubesOptained.contains(EnergyCubeTypes.GETLASER)) {
            System.out.println("try shoot");
            playerLaser.setHeading(getHeading());
            getSpace().getActions().add(playerLaser);



            notifyChange();
        }
    }





}
