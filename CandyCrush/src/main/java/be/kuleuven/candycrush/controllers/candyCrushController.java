package be.kuleuven.candycrush.controllers;

import be.kuleuven.candycrush.recordsAndGenerics.Position;
import be.kuleuven.candycrush.view.candyCrushView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import be.kuleuven.candycrush.model.model;

public class candyCrushController {
    @FXML
    private AnchorPane speelbord;
    @FXML
    private be.kuleuven.candycrush.model.model model;
    @FXML
    private be.kuleuven.candycrush.view.candyCrushView candyCrushView;
    @FXML
    private Label Score;
    @FXML
    private Button reset;

    @FXML
    public void initialize(){
        model = new model("Default Player");
        candyCrushView = new candyCrushView(model);
        speelbord.getChildren().add(candyCrushView);
        reset.setOnAction(e->reset());
        Score.setText(String.valueOf(model.getScore()));
        update();
    }

    private void reset() {
        model.reset();
        update();
    }

    public void update(){
        model.updateBoard();
        candyCrushView.updateView();
    }
}