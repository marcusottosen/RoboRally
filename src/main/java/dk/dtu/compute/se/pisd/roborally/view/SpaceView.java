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
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

/**
 * Dette dokument sørger for at tegne figurer på spillets felter.
 * Heriblandt spillerens ikon samt vægge og checkpoints.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 75; // 60; // 75;
    final public static int SPACE_WIDTH = 75;  // 60; // 75;

    public final Space space;


    /**
     * Denne metode vise selve felterne, her sort og hvid.
     * @param space placeringen af feltet.
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: #ffffff;");
        } else {
            this.setStyle("-fx-background-color: #000000;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * Viser væggen i form af en linje.
     * @param direction Retningen af linjen som string (NORTH, SOUTH, EAST, WEST)
     */
    public void viewLine(String direction){
        Canvas canvas= new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        GraphicsContext gc =
                canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.ROUND);
        //(startX, startY, endX, endY)
        //NORTH
        if (direction.equals("NORTH")){
            gc.strokeLine(2,0,SPACE_WIDTH-2,0);
            this.getChildren().add(canvas);
        } //grunden til -2, er fordi linjen er 5 tyk, så den bliver derfor justeret.
        //SOUTH
        else if (direction.equals("SOUTH")){
            gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
            this.getChildren().add(canvas);
        }
        //EAST
        else if (direction.equals("EAST")){
            gc.strokeLine(SPACE_WIDTH-2, 0, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
            this.getChildren().add(canvas);
        }
        //WEST
        else if (direction.equals("WEST")){
            gc.strokeLine(2,SPACE_HEIGHT-2,2,-2);
            this.getChildren().add(canvas);
        }
    }

    /**
     * tegner visuelt checkpointet som en cirkel.
     */
    public void viewCheckpoint(){
        Canvas checkpoint= new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        GraphicsContext gc =
                checkpoint.getGraphicsContext2D();
        //Sætter betingelserne for hvad der skal tegnes på canvasset "checkpoint"
        gc.setFill(Color.LIGHTGREEN);

        //Tegner det som er blevet defineret ovenfor på canvasset "checkpoint"
        gc.fillOval(SPACE_WIDTH/4,SPACE_WIDTH/4,40,40);
        this.getChildren().add(checkpoint);
    }


    /**
     * Tegner spillerens ikon, her en trekant. Bruges primært til at opdatere spillerens lokation.
     */
    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
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
