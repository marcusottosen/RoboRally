package dk.dtu.compute.se.pisd.roborally.model.specialFields;

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

    /**
     * Typen for den energyCube returneres.
     *
     * @return energyCubens type.
     */
    public EnergyCubeTypes getType() {
        return type;
    }

    /**
     * Sætter typen til den i parameteren.
     *
     * @param newType Den type man ønsker energyCuben skal være.
     */
    public void setType(EnergyCubeTypes newType) {
        type = newType;
    }

    /**
     * Finder random kort som bliver efterspurgt fra newCardsWindow().
     *
     * @return nyt random kommandokort
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Hvis der ikke allerede er en type, findes denne tilfældigt.
     * Hvis spilleren på feltet allerede har den type energyCube, findes der en ny.
     * Sørger for at kun én spiller kan have en laser af gangen.
     * Sørger ydermere for evt. at give spilleren et ekstra liv samt nye kort.
     * Samt at tilføje energyCuben til spiller og slette feltet efter brug.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return whether the action was successfully executed
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        try {
            Player player = space.getPlayer();

            if (type == null) {
                type = EnergyCubeTypes.getRandom();
            }

            //Hvis spilleren har alle cubes, gives NEWCARDS, da denne kan fås flere gange.
            //Hvis ikke, gives en random cube. Hvis spilleren allerede har den type cube findes der en ny.
            if (player.getEnergyCubesOptained().size() == EnergyCubeTypes.values().length) {
                type = EnergyCubeTypes.NEWCARDS;
            } else {
                while (player.getEnergyCubesOptained().contains(type)) {
                    type = EnergyCubeTypes.getRandom();
                }
            }

            //Giv laser til spilleren og fjern evt. fra en anden spiller.
            if (type == EnergyCubeTypes.GETLASER) {
                for (Player playerCheck : gameController.board.getPlayers()) {
                    if (playerCheck.getEnergyCubesOptained().contains(type)) {
                        playerCheck.removeOptainedEnergyCube(type);
                    }
                }
            } else if (type == EnergyCubeTypes.EXTRALIFE) { //Giver spilleren mulighed for at få et 4. liv.
                player.availableHealth = 4;
            } else if (type == EnergyCubeTypes.NEWCARDS) { //Spørger spilleren om han vil have alle sine kort skiftet ud.
                PopupView view = new PopupView();
                if (view.newCardsWindow(player) == 0) {
                    for (int j = 0; j < Player.NO_CARDS; j++) {
                        CommandCardField field = player.getCardField(j);
                        field.setCard(generateRandomCommandCard());
                        field.setVisible(true);
                    }
                }
            }

            //NEWCARDS er single-use, så den fjernes med det samme igen.
            if (player.getEnergyCubesOptained().contains(EnergyCubeTypes.NEWCARDS)) {
                player.removeOptainedEnergyCube(EnergyCubeTypes.NEWCARDS);
            }

            //Tilføjer til spillerens liste over opnået energyCubes.
            player.setOptainedEnergyCube(type);

            //Sletter energyCuben så den samme cube ikke kan bruges flere gange.
            space.deleteEnergyCube(this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}