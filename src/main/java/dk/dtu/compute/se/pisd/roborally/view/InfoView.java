package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.EnergyCubeTypes;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * InfoView viser spillets top-bar som indeholder alle spillers robot, navn, score og liv.
 *
 * @author Marcus Ottosen
 */
public class InfoView {

    private final Board board;
    private Player player;

    final private static String PLAYER_IMAGE_PATH = "images/robots/player";
    final private static String ALIVE = "/alive.png";
    final private static String HEALTH_ALIVE = "images/heart.png";
    final private static String HEALTH_DEAD = "images/heartDead.png";
    final private static String EXTRAMOVE_IMAGE_PATH = "images/energyCubeIcons/extraMove.png";
    final private static String KNIFE_IMAGE_PATH = "images/energyCubeIcons/knife.png";
    final private static String LASER_IMAGE_PATH = "images/energyCubeIcons/laser.png";
    final private static String SHIELD_IMAGE_PATH = "images/energyCubeIcons/shield.png";

    ImageView playerImageView;
    HBox playerView;
    VBox playerInfoBox;
    VBox playerHealthBox;
    HBox cubeBoxtop;
    HBox cubeBoxBottom;

    int smallIconSize = 20;

    /**
     * Konstruktøren af klassen.
     * Opretter mængden af HBoxe som der vil blive brugt.
     * Hvis boxene ikke bliver oprettet her, resulterer det i index out of bounds når de blive ændret længere nede.
     *
     * @param allInfoBox HBoxen som viser hele top-baren.
     * @param board      spillets board.
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
     *
     * @param allInfoBox HBoxen som viser hele top-baren.
     * @param player     Den nuværende spiller.
     */
    public void updateBox(HBox allInfoBox, Player player) {
        if (player != null) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                playerView = new HBox();

                this.player = board.getPlayer(i);

                //Sætter farven på baggrunden til spillerens farve
                Color color = getColor(board.getPlayer(i).getColor());
                playerView.setBackground(new Background(new BackgroundFill(color, null, null)));
                playerView.setStyle("-fx-border-width:2;" + "-fx-border-color: black;");

                if (board.getPlayerNumber(player) == i && !board.getPhase().name().equals("PROGRAMMING")) {
                    playerView.setStyle("-fx-border-width:2;" + "-fx-border-color: MEDIUMSPRINGGREEN;");
                }

                showPlayer();
                showPlayerInfo();
                showPlayerHealth();
                playerView.getChildren().setAll(playerImageView, playerInfoBox, playerHealthBox);

                allInfoBox.setMinHeight(smallIconSize * 4 + 5);
                allInfoBox.getChildren().set(i, playerView);
            }
        }
    }

    /**
     * @param playerColor Spillerens farve som string
     * @return spillerens farve som rette Color object.
     */
    private Color getColor(String playerColor) {
        Color color = Color.web(playerColor);

        color = Color.rgb(
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                0.25
        );
        return color;
    }

    /**
     * Viser spillerens robot
     */
    private void showPlayer() {
        Image playerImg = new Image(PLAYER_IMAGE_PATH + (player.board.getPlayerNumber(player) + 1) + ALIVE);
        playerImageView = new ImageView();

        playerImageView.setImage(playerImg);
        setElementSize(playerImageView, 50);
    }

    /**
     * Viser spillerens navn og score inde i en VBox
     * Derudover tilføjer den også cubeBoxTop og cubeBoxBottom til playerInfoBox.
     * Dette er for at alt ovenstående information er i samme box i HBoxen "allInfoBox".
     */
    private void showPlayerInfo() {
        cubeBoxtop = new HBox();
        cubeBoxBottom = new HBox();

        playerInfoBox = new VBox();
        playerInfoBox.getChildren().clear();

        Label playerName = new Label(player.getName());
        Label playerScore = new Label("score: " + player.getScore());

        cubeBoxtop.setMinHeight(20);
        cubeBoxBottom.setMinHeight(20);
        cubeBoxes();
        playerInfoBox.getChildren().setAll(playerName, playerScore, cubeBoxtop, cubeBoxBottom);
    }

    /**
     * Tilføjer billeder til hhv. cubeBoxTop og cubeBoxBottom.
     */
    public void cubeBoxes() {
        ImageView energyCubeTop = null;
        ImageView energyCubeBottom = null;
        for (EnergyCubeTypes type : player.energyCubesOptained) {
            switch (type.name()) {
                case "GETLASER" -> {
                    Image laserImg = new Image(LASER_IMAGE_PATH);
                    energyCubeTop = new ImageView(laserImg);
                    energyCubeBottom = null;
                }
                case "EXTRAMOVE" -> {
                    Image extraMoveImg = new Image(EXTRAMOVE_IMAGE_PATH);
                    energyCubeTop = new ImageView(extraMoveImg);
                    energyCubeBottom = null;
                }
                case "DEFLECTORSHIELD" -> {
                    Image deflectorShieldImg = new Image(SHIELD_IMAGE_PATH);
                    energyCubeBottom = new ImageView(deflectorShieldImg);
                    energyCubeTop = null;
                }
                case "MELEEWEAPON" -> {
                    Image knifeImg = new Image(KNIFE_IMAGE_PATH);
                    energyCubeBottom = new ImageView(knifeImg);
                    energyCubeTop = null;
                }
                default -> {
                    energyCubeTop = null;
                    energyCubeBottom = null;
                }
            }

            if (energyCubeTop != null) {
                setElementSize(energyCubeTop, smallIconSize);
                cubeBoxtop.getChildren().add(energyCubeTop);
            }
            if (energyCubeBottom != null) {
                setElementSize(energyCubeBottom, smallIconSize);
                cubeBoxBottom.getChildren().add(energyCubeBottom);
            }
        }
    }

    /**
     * Lille metode til at sætte størrrelsen af et billede.
     *
     * @param imageView billedet der skal ændre størrelse.
     */
    public void setElementSize(ImageView imageView, int size) {
        imageView.setFitWidth(size); //Holder billedet samme størrelse som en tile
        imageView.setFitHeight(size);
        imageView.setSmooth(true);
        imageView.setCache(true); //Loader hurtigere
    }

    /**
     * Viser spillerens liv som hjerter. Der vises et sort hjerte for hvert liv der er mistet.
     * Sætter først de "døde" hjerter, og derefter de normale hjerter
     */
    private void showPlayerHealth() {

        playerHealthBox = new VBox();
        Image aliveImg = new Image(HEALTH_ALIVE);
        Image deadImg = new Image(HEALTH_DEAD);

        for (int i = player.getHealth(); i < player.availableHealth; i++) {
            ImageView deadHeart = new ImageView(deadImg);
            setElementSize(deadHeart, smallIconSize);

            playerHealthBox.getChildren().add(deadHeart);
        }

        for (int i = 0; i < player.getHealth(); i++) {
            ImageView heart = new ImageView(aliveImg);
            setElementSize(heart, smallIconSize);

            playerHealthBox.getChildren().add(heart);
        }
    }
}