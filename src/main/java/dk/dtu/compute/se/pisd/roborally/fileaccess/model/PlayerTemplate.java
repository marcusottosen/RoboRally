package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.PlayerSpawns;

import java.util.ArrayList;
import java.util.List;

public class PlayerTemplate {


    public List<PlayerSpawns> spawns = new ArrayList<>();

    public int x;
    public int y;
}
