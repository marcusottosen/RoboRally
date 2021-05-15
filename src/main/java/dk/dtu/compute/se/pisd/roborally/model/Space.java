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
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.PlayerSpawns;

import java.util.List;

/**
 * Indeholder spillets felter.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;
    SpaceTemplate spaceTemplate = new SpaceTemplate();
    PlayerTemplate playerTemplate = new PlayerTemplate();

    private Player player;

    /**
     * Bruges til at bestemme et felt på pladen. Oprettelse af objekt.
     *
     * @param board Sætter boarded fra metoden til det public final board i Space.java.
     * @param x     Sætter x-koordinaten til feltet til den public final int x i Space.java.
     * @param y     Sætter y-koordinaten til feltet til den public final int y i Space.java.
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    /**
     * Ved brug af denne metode returneres player.
     *
     * @return returnerer player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Metoden bruges til, at sammenligne 2 spillere, ved at sætte player lig med oldPlayer igennem en håndfuld tjek:
     * Hvis player ikke er lig med oldPlayer og ikke er null, bliver this.player sat til = player.
     *
     * @param player Spillerens objekt.
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // Bør ikke ske
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * Adds a wall on a field
     *
     * @param heading for the wall on the field
     */
    public void addWall(Heading heading) {
        spaceTemplate.walls.add(heading);
        notifyChange();
    }

    /**
     * Deletes a wall on the field
     *
     * @param heading for the wall on the field
     */
    public void deleteWall(Heading heading) {
        spaceTemplate.walls.remove(heading);
        notifyChange();
    }

    /**
     * Returnerer listen over vægge på feltet.
     *
     * @return liste af Walls.
     */
    public List<Heading> getWalls() {
        return spaceTemplate.walls;
    }

    /**
     * Adds a checkpoint on a field
     *
     * @param checkpoint typens fieldAction.
     */
    public void addCheckpoint(FieldAction checkpoint) {
        spaceTemplate.actions.add(checkpoint);
        notifyChange();
    }

    /**
     * Deletes a checkpoint from a field
     *
     * @param checkpoint feltets aktion.
     */
    public void deleteCheckpoint(FieldAction checkpoint) {
        spaceTemplate.actions.remove(checkpoint);
        notifyChange();
    }

    /**
     * Adds a conveyorbelt on a field
     *
     * @param conveyorBelt typens fieldAction.
     */
    public void addConveyorBelt(FieldAction conveyorBelt) {
        spaceTemplate.actions.add(conveyorBelt);
        notifyChange();
    }

    /**
     * Deletes a conveyorbelt from a field
     *
     * @param conveyorBelt typens fieldAction.
     */
    public void deleteConveyorBelt(FieldAction conveyorBelt) {
        spaceTemplate.actions.remove(conveyorBelt);
        notifyChange();
    }

    /**
     * Adds an energycube to a field
     *
     * @param energyCube typens fieldAction.
     */
    public void addEnergyCube(FieldAction energyCube) {
        spaceTemplate.actions.add(energyCube);
        notifyChange();
    }

    /**
     * Removes an energycube from a field
     *
     * @param energyCube typens fieldAction.
     */
    public void deleteEnergyCube(FieldAction energyCube) {
        spaceTemplate.actions.remove(energyCube);
        notifyChange();
    }

    /**
     * @return liste af aktioner på feltet (specialFields).
     */
    public List<FieldAction> getActions() {
        return spaceTemplate.actions;
    }

    /**
     * Returnerer en liste over de spawns der er på feltet.
     *
     * @return liste over de spawns der er på feltet.
     */
    public List<PlayerSpawns> getSpawns() {
        return playerTemplate.spawns;
    }

    /**
     * This is a minor hack; since some views that are registered with the space
     * also need to update when some player attributes change, the player can
     * notify the space of these changes by calling this method.
     */
    void playerChanged() {
        notifyChange();
    }
}
