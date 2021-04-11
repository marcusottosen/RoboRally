package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoint extends FieldAction {

    private int number;

    @Override
    public boolean doAction(GameController gameController, Space space) {
        System.out.println("doAction i Checkpoint");


        return false;
    }
}
