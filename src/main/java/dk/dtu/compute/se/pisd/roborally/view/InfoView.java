package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoView extends HBox implements ViewObserver {

    private Board board;
    private HBox testBox = new HBox();


    private Player player;
    private int playerNumber;
    Button playerButton = new Button("hey");
    Button playerButton2 = new Button("hey");
    Label testLabel = new Label("nothing");
    Label testLabel2 = new Label("nothing");
    ObservableList list;

    public InfoView() {
    }

    public HBox showBox(HBox testBox) {
        System.out.println("1");
        this.testBox=testBox;

        if (player!=null) {
            testLabel.setText(player.getName());
            testLabel2.setText(Integer.toString(playerNumber));
        }
        testBox.setSpacing(20);
        setMargin(playerButton, new Insets(20, 20, 20, 20));
        setMargin(playerButton2, new Insets(20, 20, 20, 20));

        list = testBox.getChildren();
        list.addAll(playerButton, playerButton2, testLabel, testLabel2);
        //Scene scene = new Scene(testBox);
        return testBox;
    }

    public void updateBox(HBox testBox, Player player){
        System.out.println("2");
        testLabel.setText("hi");
        if (player!=null) {
            testLabel.setText(player.getName());
            testLabel2.setText(Integer.toString(playerNumber));
            testBox.setSpacing(20);
            setMargin(playerButton, new Insets(20, 20, 20, 20));
            setMargin(playerButton2, new Insets(20, 20, 20, 20));
            //list.removeAll(playerButton, playerButton2, testLabel, testLabel2);
            list = testBox.getChildren();
            list.setAll(playerButton, playerButton2, testLabel, testLabel2);
        }
    }



    @Override
    public void updateView(Subject subject) {
        System.out.println("info");
        if (subject == board){
            Player player = board.getCurrentPlayer();
            this.player=player;
            playerNumber = this.board.getPlayersNumber();
        }
    }
}
