package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoint extends FieldAction {

    private int number = 0;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        System.out.println("Here's a point");
        Player player = space.getPlayer();
        System.out.println("checkpoints completed before: " + player.checkpointsCompleted);

        int lastCheckpoint = player.checkpointsCompleted.size();

        if (number == lastCheckpoint+1){
            player.addCheckpointsCompleted(number);
            player.setScore(player.getScore()+1);
            System.out.println("checkpoints completed after: " + player.checkpointsCompleted);
            System.out.println("score: " + player.getScore());
        }
        return false;
    }
}
