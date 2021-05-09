package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Random;

public enum EnergyCubeTypes {

    GETLASER, EXTRALIFE, EXTRAMOVE, DEFLECTORSHIELD, MELEEWEAPON, NEWCARDS;



    public static EnergyCubeTypes getRandom(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];

    }
}
