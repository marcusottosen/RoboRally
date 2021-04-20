package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * InfoView viser spillets top-bar som indeholder alle spillers robot, navn, score og liv.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Ottosen
 */
public class InfoView{

    private final Board board;
    private Player player;

    final private static String PLAYER_IMAGE_PATH = "images/robots/player";
    final private static String ALIVE = "/alive.png";
    final private static String HEALTH_ALIVE = "images/heart.png";
    final private static String HEALTH_DEAD = "images/heartDead.png";

    HBox playerView;
    ImageView playerImageView;
    VBox playerInfoBox;
    VBox playerHealthBox;

    /**
     * Konstruktøren af klassen.
     * Opretter mængden af HBoxe som der vil blive brugt.
     * @param allInfoBox HBoxen som viser hele top-baren.
     * @param board spillets board.
     */
    public InfoView(HBox allInfoBox, Board board) {
        this.board = board;
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            playerView = new HBox();
            allInfoBox.getChildren().add(playerView);
        }
    }

    /**
     * Opdaterer og viser alle informationerne i top-baren.
     * Der laves et playerView HBox til hver spiller som indeholder spillerens robot, navn, score og liv.
     * Hver playerView HBox bliver tilføjet til containeren som så viser dem alle.
     * @param container HBoxen som viser hele top-baren.
     * @param player Den nuværende spiller.
     */
    public void updateBox(HBox container, Player player){
        if (player!=null) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                playerView = new HBox();

                this.player = board.getPlayer(i);

                //Sætter farven på baggrunden til spillerens farve
                Color color = getColor(board.getPlayer(i).getColor());
                playerView.setBackground(new Background(new BackgroundFill(color, null, null)));
                playerView.setStyle("-fx-border-width:2;"+"-fx-border-color: black;");

                if (board.getPlayerNumber(player) == i && !board.getPhase().name().equals("PROGRAMMING")) {
                    playerView.setStyle("-fx-border-width:2;"+"-fx-border-color: MEDIUMSPRINGGREEN;");
                }

                showPlayer();
                showPlayerInfo();
                showPlayerHealth();
                playerView.getChildren().setAll(playerImageView, playerInfoBox, playerHealthBox);

                container.getChildren().set(i, playerView);
            }
        }
    }

    /**
     *
     * @param playerColor Spillerens farve som string
     * @return spillerens farve som rette Color object.
     */
    private Color getColor(String playerColor){
        Color color = Color.web(playerColor);

        color = Color.rgb(
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255),
                0.25
        );
        return color;
    }

    /**
     * Viser spillerens robot
     */
    private void showPlayer(){
        Image playerImg = new Image(PLAYER_IMAGE_PATH + (player.board.getPlayerNumber(player) + 1) + ALIVE);
        playerImageView = new ImageView();

        playerImageView.setImage(playerImg);
        playerImageView.setFitWidth(50);
        playerImageView.setFitHeight(50);
        playerImageView.setSmooth(true);
        playerImageView.setCache(true); //Loader hurtigere
    }

    /**
     * Viser spillerens navn og score inde i en VBox
     */
    private void showPlayerInfo(){
        playerInfoBox = new VBox(5);

        Label playerName = new Label(player.getName());
        Label playerScore = new Label("score: " + player.getScore());

        playerInfoBox.getChildren().setAll(playerName, playerScore);
    }

    /**
     * Viser spillerens liv som hjerter. Der vises et sort hjerte for hvert liv der er mistet.
     * Sætter først de "døde" hjerter, og derefter i normale hjerter
     */
    private void showPlayerHealth(){
        playerHealthBox = new VBox();
        Image aliveImg = new Image(HEALTH_ALIVE);
        Image deadImg = new Image(HEALTH_DEAD);

        for (int i = player.getHealth(); i < 3; i++) {
            ImageView deadHeart = new ImageView(deadImg);
            deadHeart.setFitHeight(20);
            deadHeart.setFitWidth(20);

            playerHealthBox.getChildren().add(deadHeart);
        }

        for (int i = 0; i < player.getHealth(); i++) {
            ImageView heart = new ImageView(aliveImg);
            heart.setFitHeight(20);
            heart.setFitWidth(20);

            playerHealthBox.getChildren().add(heart);
        }
    }
}
