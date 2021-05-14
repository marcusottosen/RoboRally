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

import java.util.List;

/**
 * Her initialises de forskellige kommandokort - deres displayName sættes sammen med kortet.
 * Der gives yderligere et displayName til dem.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public enum Command {

    FORWARD1("Move 1"),
    FORWARD2("Move 2"),
    FORWARD3("Move 3"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    UTURN("U-TURN"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

    final public String displayName;
    final private List<Command> options;

    /**
     * Konstruktøren på Command.
     * Sætter de angivede værdier i parameteren til klassens variable.
     *
     * @param displayName på kortet.
     * @param options     evt. options som følger med.
     */
    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = List.of(options);
    }

    /**
     * Hvorvidt kortet kræver interaktion fra brugeren.
     *
     * @return true hvis kortet kræver interaktion.
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * Hvilket options kortet indeholder.
     *
     * @return listen af options.
     */
    public List<Command> getOptions() {
        return options;
    }
}
