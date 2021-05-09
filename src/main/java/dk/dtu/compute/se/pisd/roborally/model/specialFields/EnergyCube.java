package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.EnergyCubeTypes;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * En EnergyCube kan være af 6 forskellige værdier: se evt. EnergyCubeTypes (ENUM).
 *
 * @author Marcus Ottosen
 */
public class EnergyCube extends FieldAction {

    private EnergyCubeTypes type;



    public EnergyCubeTypes getType() {
        return type;}

    public void setType(EnergyCubeTypes newType){
        type = newType;
    }

    /**
     * Executes the field action for a given space. In order to be able to do.
     * that the GameController associated with the game is passed to this method.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return whether the action was successfully executed
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();

        if (type == null){
            type = EnergyCubeTypes.getRandom();
        }

        //Hvis spilleren allerede har den type cube findes der en ny.
        //Hvis spilleren har alle cubes, brydes loopet aldrig!
        while (player.energyCubesOptained.contains(type)){
            type = EnergyCubeTypes.getRandom();
        }


        //Giv laser til spilleren og fjern evt. fra en anden spiller.
        if (type == EnergyCubeTypes.GETLASER){
            for (Player playerCheck : gameController.board.getPlayers()){
                if (playerCheck.getEnergyCubesOptained().contains(type)){
                    playerCheck.removeOptainedEnergyCube(type);
                }
            }

        } else if (type == EnergyCubeTypes.EXTRALIFE){ //Giver spilleren mulighed for at få et 4. liv.
            player.availableHealth=4;
        } else if (type == EnergyCubeTypes.EXTRAMOVE){ //Rykker spilleren 1 ekstra frem ved hvert move kort.
            //Logik til denne skrives evt. inde i gamecontroller når man rykker sig.
        } else if (type == EnergyCubeTypes.DEFLECTORSHIELD){ //Skjold mod laser. Kan kun bruges 1 gang.
            //Skrives hvorend skaden tages
        } else if (type == EnergyCubeTypes.MELEEWEAPON){ //Skader alle man skubber til.
            //Logik til denne skrives evt. inde i Gamecontroller når der skubbes
        } else if (type == EnergyCubeTypes.NEWCARDS){ //Spørger spilleren om han vil have alle sine kort skiftet ud.
            //Åben enten et nye vindue eller ændre på knapperne så spilleren kan vælge om han vil have nye kort.
        }

        //System.out.println(type);
        player.setOptainedEnergyCube(type); //Tilføjer til spillerens liste over opnået energyCubes.

        //TODO Hvis energyCuben skal rykkes et andet sted hen efter den er samlet op skriv det her.
        System.out.println(space.getActions());
        //space.deleteEnergyCube(this);
        return false;
    }
}
