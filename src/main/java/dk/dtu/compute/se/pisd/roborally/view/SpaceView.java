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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.specialFields.Wall;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
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
     */
    public void viewWall() {
        Wall wall = space.getWalls();

        if (wall != null) {

            Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
            GraphicsContext gc =
                    canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.setLineCap(StrokeLineCap.ROUND);
            //(startX, startY, endX, endY)
            switch (wall.getDirection()) {
                case "NORTH" -> {
                    gc.strokeLine(2, 0, SPACE_WIDTH - 2, 0);
                    this.getChildren().add(canvas);
                }
                case "SOUTH" -> {
                    gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    this.getChildren().add(canvas);
                }
                case "EAST" -> {
                    gc.strokeLine(SPACE_WIDTH - 2, 0, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    this.getChildren().add(canvas);
                }
                case "WEST" -> {
                    gc.strokeLine(2, SPACE_HEIGHT - 2, 2, -2);
                    this.getChildren().add(canvas);
                }
            }
        }
    }

    public void viewConveryorBelt() {
        ConveyorBelt conveyorBelt = space.getConveyorBelt();

        if (conveyorBelt != null) {
            int x1=0; int y1=0;
            int x2=0; int y2=0;
            int x3=0; int y3=0;
            switch (conveyorBelt.getDirection()){
                case "NORTH" -> {
                    x1 = SPACE_WIDTH/2; y1 = 0;
                    x2 = 0; y2 = SPACE_HEIGHT;
                    x3 = SPACE_WIDTH; y3 = SPACE_HEIGHT;
                }
                case "SOUTH" -> {
                    x1 = SPACE_WIDTH/2; y1 = SPACE_HEIGHT;
                    x2 = 0; y2 = 0;
                    x3 = SPACE_WIDTH; y3 = 0;
                }
                case "EAST" -> {
                    x1 = SPACE_WIDTH; y1 = SPACE_HEIGHT/2;
                    x2 = 0; y2 = 0;
                    x3 = 0; y3 = SPACE_HEIGHT;
                }
                case "WEST" -> {
                    x1 = 0; y1 = SPACE_HEIGHT/2;
                    x2 = SPACE_WIDTH; y2 = 0;
                    x3 = SPACE_WIDTH; y3 = SPACE_HEIGHT;
                }
                default -> {
                    System.out.println("Error conveyorBelt direction");
                }
            }

            Polygon arrow = new Polygon(x1-5, y1-5,
                    x2-5, y2-5,
                    x3-5, y3-5);
            try {
                arrow.setFill(Color.GRAY);
            } catch (Exception e) {
                arrow.setFill(Color.GREY);
            }
            this.getChildren().add(arrow);
        }

    }


    /**
     * tegner visuelt checkpointet som en cirkel.
     */
    public void viewCheckpoint() {
        //this.getChildren().clear();

        Checkpoint checkpoint = space.getCheckpoint();

        if (checkpoint != null) {
            Canvas circle = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
            GraphicsContext gc =
                    circle.getGraphicsContext2D();
            //Sætter betingelserne for hvad der skal tegnes på canvasset "checkpoint"
            gc.setFill(Color.LIGHTGREEN);
            gc.setGlobalAlpha(0.6); //opacity

            //Tegner det som er blevet defineret ovenfor på canvasset "checkpoint"
            gc.fillOval(SPACE_WIDTH / 4, SPACE_WIDTH / 4, 40, 40);

            this.getChildren().add(circle);
        }
    }


    /**
     * Tegner spillerens ikon, her en trekant. Bruges primært til at opdatere spillerens lokation.
     */
    private void updatePlayer() {
        this.getChildren().clear(); //fjerner den tidligere trekant efter den er rykket.

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
            viewCheckpoint();
            viewWall();
            viewConveryorBelt();

        }
    }

}
