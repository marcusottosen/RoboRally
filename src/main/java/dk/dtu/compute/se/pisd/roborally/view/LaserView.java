package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LaserView {

    public static StackPane laserPane;
    public static ImageView laserImg;
    public static Space space;
    //public static Heading heading;
    //public static Board board;

    public static List<StackPane> laserPaneList = new ArrayList<>();
    public static List<ImageView> laserImgList = new ArrayList<>();
    public static List<Space> spaces = new ArrayList<>();

    // Tegn en rød linje i den ønskede Heading indtil den rammer en væg, spiller eller kommer ud for banen.
    // Brug en delay metode så laseren vises i fx 1 sekund.

    // Find koordinaterne for laseren et andet sted og få dem herind.
    // LaserView kan måske også bare skrives i SpaceView(?) - helst her though.

    // Thumbs up hvis vi kan få det til at virke med laserCross.png også
    public LaserView(StackPane laserPane, ImageView laserImg, Space space) {
        this.laserPane = laserPane;
        this.laserImg = laserImg;
        this.space = space;
        //this.heading = heading;
        //this.board = board;
        laserPaneList.add(laserPane);
        laserImgList.add(laserImg);
        spaces.add(space);
        /*System.out.println("space x: " + space.x);
        System.out.println("space calc: " + space.x % space.board.width);
        System.out.println("board width: " + space.board.width);
        System.out.println(space.board.getSpace(0,0).x);*/
        /*Space oldspace = space;
        do {
            spaces.add(oldspace);
            oldspace = board.getNeighbour(oldspace,heading);
        }while(board.getNeighbour(oldspace, heading).x < 0 && board.getNeighbour(oldspace, heading).y < 0);*/
    }

    /*public void addLaser(StackPane laserPane, ImageView laserImg, Space space){
        this.laserPane.add(laserPane);
        this.laserImg.add(laserImg);
        this.space.add(space);
    }*/

    public static void shootLaser() {
        //Space oldspace = space;
        /*do{
            laserPane.getChildren().add(laserImg);
            laserPaneList.add(laserPane);
            laserImgList.add(laserImg);
            spaces.add(oldspace);
            oldspace = board.getNeighbour(oldspace, heading);
        }while(board.getNeighbour(oldspace, heading) != null);*/
        for (int i = 0; i < spaces.size(); i++){
            laserPaneList.get(i).getChildren().add(laserImgList.get(i));
        }
        //System.out.println("spaces size: " + spaces.size());

            //oldspace = board.getNeighbour(oldspace, heading);


    }

    public static void stopLaser() {
        for (int i = 0; i < spaces.size(); i++){
            laserPaneList.get(i).getChildren().remove(laserImgList.get(i));
        }
    }
}
