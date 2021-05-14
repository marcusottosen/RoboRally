package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Random;

/**
 * ENUM over de forskellige typer af energyCubes.
 *
 * @author Marcus Ottosen
 */
public enum EnergyCubeTypes {
    GETLASER, EXTRALIFE, EXTRAMOVE, DEFLECTORSHIELD, MELEEWEAPON, NEWCARDS;

    /**
     * Bruges til at finde en tilfældig energyCube.
     *
     * @return en random energyCube.
     */
    public static EnergyCubeTypes getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];

    }
}
