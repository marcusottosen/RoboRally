package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class infoView extends HBox implements ViewObserver {

    private Label testLabel;

    public infoView() {
    }

    public HBox showBox(HBox testBox) {
        //HBox testBox = new HBox();
        Button playerButton = new Button("hey");
        Button playerButton2 = new Button("hey");
        testBox.setSpacing(20);
        setMargin(playerButton, new Insets(20, 20, 20, 20));
        setMargin(playerButton2, new Insets(20, 20, 20, 20));

        ObservableList list = testBox.getChildren();
        list.addAll(playerButton, playerButton2);
        //Scene scene = new Scene(testBox);
        return testBox;
    }


    @Override
    public void updateView(Subject subject) {

    }

    @Override
    public void update(Subject subject) {

    }
}
