package be.kuleuven.candycrush.controllers;

import be.kuleuven.candycrush.view.candyCrushView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import be.kuleuven.candycrush.model.model;

import java.util.ArrayList;

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
        speelbord.setOnMouseClicked(this::klik);
        reset.setOnAction(this::reset);
    }

    private void reset(ActionEvent e) {
        model.reset();
        candyCrushView.updateView();
        Score.setText(String.valueOf(model.getScore()));
    }

    private void klik(MouseEvent e){
        int x = (int) e.getX()/ (200/model.getWidth());
        int y = (int) e.getY()/ (200/model.getHeight());
        int index = y * model.getWidth() +x;
        ArrayList<Integer>buren= model.checkBuren(index);
        buren.add(index);
        if (buren.size() >= 3) {
            model.setScore(model.getScore() + buren.size());
        }
        for (int i : buren) {
            model.veranderCandy(i);
        }
        buren.clear();
        update();
    }
    public void update(){

        Score.setText(String.valueOf(model.getScore()));
        candyCrushView.updateView();
    }
}