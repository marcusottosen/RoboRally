package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import com.sun.source.tree.IfTree;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.PopupView;

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
     * Finder random kort som bliver efterspurgt fra newCardsWindow().
     * @return nyt random kommandokort
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
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

        //Hvis spilleren har alle cubes, gives NEWCARDS, da denne kan fås flere gange.
        //Hvis ikke, gives en random cube. Hvis spilleren allerede har den type cube findes der en ny.
        if (player.getEnergyCubesOptained().size() == EnergyCubeTypes.values().length){
            type = EnergyCubeTypes.NEWCARDS;
        }else {
            while (player.getEnergyCubesOptained().contains(type)) {
                type = EnergyCubeTypes.getRandom();
            }
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

        } else if (type == EnergyCubeTypes.DEFLECTORSHIELD){ //Skjold mod laser. Kan kun bruges 1 gang.
            //Skrives hvorend skaden tages

        } else if (type == EnergyCubeTypes.NEWCARDS){ //Spørger spilleren om han vil have alle sine kort skiftet ud.
            PopupView view = new PopupView();
            if(view.newCardsWindow(player) == 0) {
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
         }

        if (player.getEnergyCubesOptained().contains(EnergyCubeTypes.NEWCARDS)) {
            player.removeOptainedEnergyCube(EnergyCubeTypes.NEWCARDS);
        }









        player.setOptainedEnergyCube(type); //Tilføjer til spillerens liste over opnået energyCubes.

        //TODO Hvis energyCuben skal rykkes et andet sted hen efter den er samlet op skriv det her.
        System.out.println(space.getActions());
        //space.deleteEnergyCube(this);
        return false;
    }


}
