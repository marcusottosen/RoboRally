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
package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Håndterer aktionen af et conveyorbelt. Gælder både for de blå og gule.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;
    private String color;

    /**
     * Returnerer conveyorbeltets farve.
     * @return faven som String.
     */
    public String getColor(){return color; }

    /**
     * Returnerer conveyorbeltets retning.
     * @return heading vha. enum klassen Heading.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sætter conveyorbeltets heading.
     * @param heading heading vha. enum klassen Heading.
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * Conveyorbeltets aktion. Køres når en spiller står på feltet.
     * Rykker spilleren et felt hvis det er et gult transportbånd. 2 ved blå.
     * @param gameController the gameController of the respective game.
     * @param space the space this action should be executed for.
     * @return false.
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        try {
            Player player = space.getPlayer();

            if (color.equals("YELLOW")) {
                Space target = gameController.board.getNeighbour(space, heading);
                gameController.moveToSpace(space.getPlayer(), target, heading);

            } else {
                Space target = gameController.board.getNeighbour(space, heading);
                gameController.moveToSpace(space.getPlayer(), target, heading);
                Space prevTarget = target;

                target = gameController.board.getNeighbour(target, heading);
                gameController.moveToSpace(prevTarget.getPlayer(), target, heading);
            }
        } catch (GameController.ImpossibleMoveException e) {
            // catching exception.
        }
        return false;
    }
}
