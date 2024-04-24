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
        speelbord.setOnMouseClicked(this::klik);
        reset.setOnAction(this::reset);
    }

    private void reset(ActionEvent e) {
        model.reset();
        candyCrushView.updateView();
        Score.setText(String.valueOf(model.getScore()));
    }

    private void klik(MouseEvent e){
        int x = (int) e.getX()/ (400/model.getBoard().width());
        int y = (int) e.getY()/ (400/model.getBoard().height());
        int index = y * model.getBoard().width() + x;
        Iterable<Position> sameNeighbors = model.getSameNeighborsPositions(index);
        int sameNeighborsCount = 0;
        for (Position pos : sameNeighbors) {
            model.veranderCandy(pos.toIndex());
            sameNeighborsCount++;
        }
        if (sameNeighborsCount >= 3) {
            model.setScore(model.getScore() + sameNeighborsCount);
            sameNeighborsCount = 0;
        }
        update();
    }
    public void update(){
        Score.setText(String.valueOf(model.getScore()));
        candyCrushView.updateView();
    }
}