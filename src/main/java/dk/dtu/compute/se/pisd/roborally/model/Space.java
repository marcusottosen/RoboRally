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
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Indeholder spillets felter.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    private Player player;
    private Checkpoint checkpoint;
    private Wall wall;
    private ConveyorBelt conveyorBelt;

    /**
     * Metode bruges til at bestemme et felt på pladen. Oprettelse af objekt.
     * @param board Sætter boarded fra metoden til det public final board i Space.java.
     * @param x Sætter x-koordinaten til feltet til den public final int x i Space.java.
     * @param y Sætter y-koordinaten til feltet til den public final int y i Space.java.
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    /**
     * Ved brug af denne metode returneres player.
     * @return returnerer player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Metoden bruges til, at sammenligne 2 spillere, ved at sætte player lig med oldPlayer igennem en håndfuld tjek:
     * Hvis player ikke er lig med oldPlayer og ikke er null, bliver this.player sat til = player.
     * @param player Spillerens objekt.
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }


    /**
     *
     * @return walls.
     */
    /*public Wall getWalls() {
        return wall;
    }
*/
    public void setWall(Wall wall){
        Wall oldWall = this.wall;
        if (wall != oldWall && (wall == null || board == wall.board));{
            this.wall = wall;
            if (oldWall != null){
                oldWall.setSpace(null);
            }
            if (wall != null){
                wall.setSpace(this);
            }
            notifyChange();
        }
    }

    SpaceTemplate spaceTemplate = new SpaceTemplate();

    public void addWall(Heading heading){
        spaceTemplate.walls.add(heading);
        notifyChange();
    }
    public void deleteWall(Heading heading){
        spaceTemplate.walls.remove(heading);
        notifyChange();
    }
    public List<Heading> getWalls(){
        return spaceTemplate.walls;
    }



    public Checkpoint getCheckpoint(){
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint){
        Checkpoint oldCheckpoint = this.checkpoint;
        if (checkpoint != oldCheckpoint && (checkpoint == null || board == checkpoint.board));{
            this.checkpoint = checkpoint;
            if (oldCheckpoint != null){
                oldCheckpoint.setSpace(null);
            }
            if (checkpoint != null){
                checkpoint.setSpace(this);
            }
            notifyChange();
        }
    }

    public ConveyorBelt getConveyorBelt(){
        return conveyorBelt;
    }

    public void setConveyorBelt(ConveyorBelt conveyorBelt){
        ConveyorBelt oldConveyorBelt = this.conveyorBelt;
        if (conveyorBelt != oldConveyorBelt && (conveyorBelt == null || board == conveyorBelt.board));{
            this.conveyorBelt = conveyorBelt;
            if (oldConveyorBelt != null){
                oldConveyorBelt.setSpace(null);
            }
            if (conveyorBelt != null){
                conveyorBelt.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     *
     * @return type af aktion.
     */
    public List<FieldAction> getActions() {
        List<FieldAction> actions = new ArrayList<>();

        return actions;
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
