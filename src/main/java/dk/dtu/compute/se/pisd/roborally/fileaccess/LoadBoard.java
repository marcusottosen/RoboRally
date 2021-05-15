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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.EnergyCube;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * LoadBoard bruges til hhv. at loade og save boarded samt at loade spilleren fra en JSON fil.
 * Derudover sørges der for at tilføje energyCubes på tilfældige frie tiles ved at tilføje dem til space.getActions.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */

public class LoadBoard {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "board1";
    private static final String JSON_EXT = "json";
    public static int energyCubesAmount = 0;
    public static BoardTemplate template;
    public static Space space;
    public static Board result;

    /**
     * loader boarded samt sætter energyCubes
     *
     * @param boardname navnet på boarded der skal loades.
     * @return result hvilket er boarded.
     */
    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);

        Adapter adapter = new Adapter<FieldAction>();
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, adapter);
        Gson gson = simpleBuilder.create();

        JsonReader reader = null;
        try {
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height);
            for (SpaceTemplate spaceTemplate : template.spaces) {
                space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }

            //Finder en random tom placering til energyCube og tilføjer hver især.
            energyCubesAmount = template.energyCubesAmount;
            Random random = new Random();
            for (int i = 0; i < energyCubesAmount; i++) {
                int randomX;
                int randomY;
                Space energySpace;
                do {
                    randomX = random.nextInt(template.width);
                    randomY = random.nextInt(template.height);
                    energySpace = result.getSpace(randomX, randomY);
                } while (!energySpace.getActions().isEmpty()); //Selvølgelig kræves det, at der er frie pladser tilbage.
                EnergyCube energyCube = new EnergyCube();
                energySpace.getActions().add(energyCube);
            }

            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException ignored) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    /**
     * Loader spilleren.
     *
     * @param player the player to add to the board
     * @param i      the index of the loop required to spawn all players on the board. (see AppController)
     */
    public static void loadPlayer(Player player, int i) {
        // i er indexet af den loop der bruges til at kalde metoden
        PlayerTemplate playerTemplate = template.spawns.get(i);
        space = result.getSpace(playerTemplate.x, playerTemplate.y);
        if (space != null) {
            space.getSpawns().addAll(playerTemplate.spawns);
        }
        player.setSpace(space);
    }

    /**
     * Gemmer boarded til en JSON fil.
     *
     * @param board det board der skal gemmes.
     * @param name  navnet boarded skal gemmes under.
     */
    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate();
        template.width = board.width;
        template.height = board.height;

        ClassLoader classLoader = LoadBoard.class.getClassLoader();

        String filename =
                classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException ignored) {
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
