package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af et checkpoint.
 * Hvert checkpoint indeholder dette.
 *
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class Checkpoint extends FieldAction {
    private int number = 0;

    /**
     * Returnerer checkpointets nummer.
     * @return checkpointets nummer som int.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sætter checkpointets nummer
     * @param number den int man ønsker checkpointets nummer skal være.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Checkpoinets aktion. Køres når en spiller står på et checkpoint felt.
     * Giver spilleren et point hvis:
     * - Spilleren ikke allerede har været på dette checkpoint
     * - og spilleren har modtaget det tidligere checkpoint.
     * @param gameController the gameController of the respective game.
     * @param space the space this action should be executed for.
     * @return false.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        System.out.println("Here's a point");
        Player player = space.getPlayer();

        int lastCheckpoint = player.checkpointsCompleted.size();

        if (number == lastCheckpoint+1){
            player.addCheckpointsCompleted(number);
            player.setScore(player.getScore()+1);
        }
        return false;
    } //TODO Egentligt ikke nogen grund til at både tilføje til checkpointsComplated array og score. Bare behold arrayet.
    // Bare sørg for at der bruges arrayet alle steder scoren tjekkes (bla. når vinderen findes)
}
