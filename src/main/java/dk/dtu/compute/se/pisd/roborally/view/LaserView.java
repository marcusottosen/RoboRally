package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.concurrent.TimeUnit;

public class LaserView extends Subject {
    public static StackPane laserPane;
    public static ImageView laserImg;
    public static Space space;

    // Tegn en rød linje i den ønskede Heading indtil den rammer en væg, spiller eller kommer ud for banen.
    // Brug en delay metode så laseren vises i fx 1 sekund.

    // Find koordinaterne for laseren et andet sted og få dem herind.
    // LaserView kan måske også bare skrives i SpaceView(?) - helst her though.

    // Thumbs up hvis vi kan få det til at virke med laserCross.png også
    public LaserView(StackPane laserPane, ImageView laserImg, Space space) {
        this.laserPane = laserPane;
        this.laserImg = laserImg;
        this.space = space;
    }

    public static void shootLaser(){
        laserPane.getChildren().add(laserImg);
    }
    public static void stopLaser(){
        laserPane.getChildren().removeAll(laserImg);
        laserPane.
    }
}
