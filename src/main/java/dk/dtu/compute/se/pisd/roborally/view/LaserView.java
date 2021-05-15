package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Står for at fjerne og tilføje laser til et StackPane indeholdende laseren.
 *
 * @author Victor Kongsbak
 */

public class LaserView {

    public static StackPane laserPane;
    public static ImageView laserImg;
    public static Space space;

    public static List<StackPane> laserPaneList = new ArrayList<>();
    public static List<ImageView> laserImgList = new ArrayList<>();
    public static List<Space> spaces = new ArrayList<>();

    public LaserView(StackPane laserPane, ImageView laserImg, Space space) {
        this.laserPane = laserPane;
        this.laserImg = laserImg;
        this.space = space;

        laserPaneList.add(laserPane);
        laserImgList.add(laserImg);
        spaces.add(space);
    }

    /**
     * For hver space i listen, tilføjes der en rød streg som repræsenterer en laser
     */
    public static void shootLaser() {
        for (int i = 0; i < spaces.size(); i++) {
            laserPaneList.get(i).getChildren().add(laserImgList.get(i));
        }
    }

    /**
     * Det der blev tilføjet i shootLaser, bliver fjernet her.
     */
    public static void stopLaser() {
        for (int i = 0; i < spaces.size(); i++) {
            laserPaneList.get(i).getChildren().remove(laserImgList.get(i));
        }
    }
}
