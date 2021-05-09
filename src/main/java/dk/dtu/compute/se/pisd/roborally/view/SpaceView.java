/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.*;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

/**
 * Dette dokument sørger for at tegne figurer/billeder på spillets felter.
 * Heriblandt spillerens ikon samt vægge, checkpoints conveyorbelts osv..
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class SpaceView extends StackPane implements ViewObserver {

    public static int SPACE_HEIGHT;
    public static int SPACE_WIDTH;

    public final Space space;

    final private static String TILE_IMAGE_PATH = "images/tiles/tile.png";
    final private static String WALL_IMAGE_PATH = "images/tiles/wall.png";
    final private static String BLUECONVEYORBELT_IMAGE_PATH = "images/tiles/conveyorbeltBlue.png";
    final private static String YELLOWCONVEYORBELT_IMAGE_PATH = "images/tiles/conveyorbeltYellow.png";
    final private static String PIT_IMAGE_PATH = "images/tiles/pit.png";
    final private static String LEFT_GEAR_IMAGE_PATH = "images/tiles/gearLeft.png";
    final private static String RIGHT_GEAR_IMAGE_PATH = "images/tiles/gearRight.png";
    final private static String LASER_EMITTER_IMAGE_PATH = "images/tiles/laserEmitter.png";
    final private static String PUSHPANEL_IMAGE_PATH = "images/tiles/pushPanel.png";
    final private static String TOOLBOX_IMAGE_PATH = "images/tiles/toolbox.png";
    final private static String CHECKPOINT_IMAGE_PATH = "images/tiles/checkpoint";
    final private static String ENERGYCUBE_IMAGE_PATH = "images/tiles/energyCube.png";

    final private static String PLAYER_IMAGE_PATH = "images/robots/player";
    final private static String ALIVE = "/alive.png";
    final private static String POWERUP = "/powerUp.png";
    final private static String DEAD = "/dead.png";

    private final StackPane laserPane;
    private final StackPane playerPane;
    Random random = new Random();

    /**
     * Denne metode vise selve felterne, her sort og hvid.
     * @param space placeringen af feltet.
     */
    public SpaceView(@NotNull Space space, int height) {
        this.space = space;
        SPACE_HEIGHT = 85-(height*2);
        SPACE_WIDTH = 85-(height*2);

        Image image = new Image(TILE_IMAGE_PATH);

        ImageView tile = new ImageView();
        laserPane = new StackPane(); // Laver et nyt pane(lag) til laser emitters, så de kan stå ovenpå walls
        playerPane = new StackPane(); // laver et pane til robotten ovenpå alt andet.

        tile.setImage(image);
        setElementSize(tile);

        tile.setRotate(random.nextInt(4)*90);


        space.attach(this);
        update(space);
        this.getChildren().add(tile);
        viewBoardElements();

        this.getChildren().add(laserPane);
        this.getChildren().add(playerPane);
        updatePlayer();
    }

    /**
     * Finder felttypens instans og tjekker om den matcher en instans af en af de spicielle felter.
     * Hvis det er en instans, kaldes der til den rette felttype som så vises.
     * Denne metode er kun til de felter som ikke ændrer, flytter eller fjerner sig.
     */
    public void viewBoardElements(){
        if(space.getActions().size() != 0) {
            for (int i = 0; i < space.getActions().size(); i++) {
                FieldAction actionType = space.getActions().get(i);
                //System.out.println(actionType);
                if (actionType instanceof ConveyorBelt) {
                    viewConveyorbelt(((ConveyorBelt) actionType).getHeading(), ((ConveyorBelt) actionType).getColor());
                }else if (actionType instanceof Checkpoint){
                    viewCheckpoint(((Checkpoint) actionType).getNumber());
                }else if (actionType instanceof Pit){
                    viewPit();
                }else if (actionType instanceof Gear){
                    viewGear(((Gear) actionType).getDirection());
                }else if (actionType instanceof Toolbox){
                    viewToolbox();
                }else if(actionType instanceof Laser){
                    viewLaserEmitter(((Laser) actionType).getHeading());
                }else if(actionType instanceof PushPanel){
                    viewPushPanel(((PushPanel) actionType).getHeading());
                }else if (actionType instanceof EnergyCube){
                    viewEnergyCube();
                }
            }
        }
        viewWall();
    }

    /**
     * Lille metode til at sætte størrrelsen af et billede til størrelsen af et felt.
     * @param imageView billedet der skal ændre størrelse.
     */
    public void setElementSize(ImageView imageView){
        imageView.setFitWidth(SPACE_WIDTH); //Holder billedet samme størrelse som en tile
        imageView.setFitHeight(SPACE_HEIGHT);
        imageView.setSmooth(true);
        imageView.setCache(true); //Loader hurtigere
    }

    /**
     * Viser væggen.
     */
    public void viewWall() {
        for(Heading wall : space.getWalls()) {
            if (wall != null) {
                try {
                    Image image = new Image(WALL_IMAGE_PATH);

                    ImageView wallImg = new ImageView();
                    wallImg.setImage(image);
                    setElementSize(wallImg);

                    wallImg.setRotate(((90*wall.ordinal())%360)-180);
                    this.getChildren().add(wallImg);
                }catch (Exception e){
                    System.out.println("Error loading wall");
                }
            }
        }
    }

    /**
     * Viser et conveyorbelt i den rette retning og farve.
     * @param heading conveyorbeltets heading som Heading
     * @param color conveyorbeltets farve som String.
     */
    public void viewConveyorbelt(Heading heading, String color) {
        for (FieldAction conveyorBelt : space.getActions()){
            if (conveyorBelt != null) {
                try {
                    Image image;
                    if (color.equals("BLUE")) {
                        image = new Image(BLUECONVEYORBELT_IMAGE_PATH);
                    } else {
                        image = new Image(YELLOWCONVEYORBELT_IMAGE_PATH);
                    }

                    ImageView conveyorBeltImg = new ImageView();

                    conveyorBeltImg.setImage(image);
                    setElementSize(conveyorBeltImg);

                    conveyorBeltImg.setRotate(((90*heading.ordinal())%360)-180);
                    this.getChildren().add(conveyorBeltImg);
                }catch (Exception e){
                    System.out.println("Error loading conveyorbelt");
                }
            }
        }
    }

    /**
     * tegner visuelt checkpointet.
     */
    public void viewCheckpoint(int number) {
        for(FieldAction checkpoints : space.getActions()){
            if (checkpoints != null) {
                try {
                    String PATH=CHECKPOINT_IMAGE_PATH+number+".png";
                    Image image = new Image(PATH);

                    ImageView checkpointImg = new ImageView();

                    checkpointImg.setImage(image);
                    setElementSize(checkpointImg);
                    this.getChildren().add(checkpointImg);
                }catch (Exception e){
                    System.out.println("Error loading checkpoint");
                }
            }
        }
    }

    /**
     * Viser et pit på det rette felt.
     */
    public void viewPit() {
        for (FieldAction pit : space.getActions()){
            if(pit != null){
                try {
                    Image image = new Image(PIT_IMAGE_PATH);
                    ImageView pitImg = new ImageView();

                    pitImg.setImage(image);
                    setElementSize(pitImg);
                    this.getChildren().add(pitImg);
                } catch (Exception e){
                    System.out.println("Error loading pit");
                }
            }
        }
    }

    /**
     * Viser et gear på det rette felt i retningen af det der er angivet i parameteren.
     * @param direction retningen på gear. Skal være enten "LEFT" eller "RIGHT".
     */
    public void viewGear(String direction){
        for (FieldAction gear : space.getActions()){
            if(gear != null){
                String PATH = "";

                switch (direction){
                    case "LEFT" -> PATH=LEFT_GEAR_IMAGE_PATH;
                    case "RIGHT" -> PATH=RIGHT_GEAR_IMAGE_PATH;
                }
                try {
                    Image image = new Image(PATH);
                    ImageView gearImg = new ImageView();

                    gearImg.setImage(image);
                    setElementSize(gearImg);
                    this.getChildren().add(gearImg);
                } catch (Exception e){
                    System.out.println("Error loading gear");
                }
            }
        }
    }

    /**
     * Viser en laser emitter. Skal sættes ovenpå en væg.
     * @param heading laser emitterens retning som Heading.
     */
    public void viewLaserEmitter(Heading heading) {
        for(FieldAction laserEmitter : space.getActions()) {
            if (laserEmitter != null) {
                try {
                    Image image = new Image(LASER_EMITTER_IMAGE_PATH);
                    ImageView laserEmitterImg = new ImageView();
                    laserEmitterImg.setImage(image);
                    setElementSize(laserEmitterImg);

                    laserEmitterImg.setRotate(((90*heading.ordinal())%360)-180);
                    laserPane.getChildren().add(laserEmitterImg);
                } catch (Exception e){
                    System.out.println("Error loading Laser Emitter");
                }
            }
        }
    }

    /**
     * Viser en toolbox på feltet.
     */
    public void viewToolbox() {
        for (FieldAction toolbox : space.getActions()){
            if (toolbox != null) {
                try {
                    Image image = new Image(TOOLBOX_IMAGE_PATH);
                    ImageView tollboxImg = new ImageView();

                    tollboxImg.setImage(image);
                    setElementSize(tollboxImg);
                    this.getChildren().add(tollboxImg);
                }catch (Exception e){
                    System.out.println("Error loading Toolbox");
                }
            }
        }
    }

    /**
     * Viser en energyCube på feltet.
     */
    public void viewEnergyCube() {
        laserPane.getChildren().clear();
        for (FieldAction energyCube : space.getActions()){
            if (energyCube != null) {
                try {
                    Image image = new Image(ENERGYCUBE_IMAGE_PATH);
                    ImageView energyCubeImg = new ImageView();

                    energyCubeImg.setImage(image);

                    energyCubeImg.setFitWidth(SPACE_WIDTH/1.5);
                    energyCubeImg.setFitHeight(SPACE_HEIGHT/1.5);
                    energyCubeImg.setSmooth(true);
                    energyCubeImg.setCache(true); //Loader hurtigere

                    laserPane.getChildren().add(energyCubeImg);
                }catch (Exception e){
                    System.out.println("Error loading energyCube");
                }
            }
        }
    }

    /**
     * Viser et push panel på feltet i den retning som angivet i parameteren.
     * @param heading retningen som Heading.
     */
    public void viewPushPanel(Heading heading) {
        for (FieldAction pushPanel : space.getActions()){
            if (pushPanel != null) {
                try {
                    Image image = new Image(PUSHPANEL_IMAGE_PATH);
                    ImageView pushPanelImg = new ImageView();
                    pushPanelImg.setImage(image);
                    setElementSize(pushPanelImg);

                    pushPanelImg.setRotate(((90*heading.ordinal())%360)-180);
                    laserPane.getChildren().add(pushPanelImg);
                } catch (Exception e){
                    System.out.println("Error loading push panel");
                }
            }
        }
    }

    /**
     * Tegner spillerens ikon, her en trekant. Bruges primært til at opdatere spillerens lokation.
     * playerPane er spillernes helt eget pane, som er sat ovenpå alle de andre felter.
     */
    private void updatePlayer() {
        playerPane.getChildren().clear();
        Player player = space.getPlayer();
        //player.board.getPlayerNumber(player)
        if (player != null) {
            try {
                Image image = new Image(PLAYER_IMAGE_PATH + (player.board.getPlayerNumber(player) + 1) + ALIVE);
                ImageView playerImg = new ImageView();

                playerImg.setImage(image);
                playerImg.setFitWidth(SPACE_WIDTH-15); //Holder billedet samme størrelse som en tile
                playerImg.setFitHeight(SPACE_HEIGHT-15);
                playerImg.setSmooth(true);
                playerImg.setCache(true); //Loader hurtigere

                playerImg.setRotate((90*player.getHeading().ordinal())%360);
                playerPane.getChildren().add(playerImg);
            } catch (Exception e){
                System.out.println("Error loading player image");
            }

        }
    }

    /**
     * motoden sikre at der befinder sig et subject på feltet før den eksekverer updatePlayer()
     * @param subject objekt af subject.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }
}
