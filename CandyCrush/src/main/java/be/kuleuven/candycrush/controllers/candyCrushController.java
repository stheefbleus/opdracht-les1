package be.kuleuven.candycrush.controllers;

import be.kuleuven.candycrush.recordsAndGenerics.Position;
import be.kuleuven.candycrush.view.candyCrushView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        speelbord.setOnMouseClicked(this::klik);
    }

    private void reset() {
        model.reset();
        candyCrushView.updateView();
        Score.setText(String.valueOf(model.getScore()));
    }

    private Position lastClickedPosition = null;

    public void klik(MouseEvent e){
        int row = (int) e.getX()/ candyCrushView.getWidthCandy();
        int col = (int) e.getY() / candyCrushView.getHeightCandy();
        Position clickedPosition = new Position(col, row, model.getBoardsize());

        if (lastClickedPosition == null) {
            lastClickedPosition = clickedPosition;
        } else {
            int rowDiff = Math.abs(lastClickedPosition.rijNummer() - clickedPosition.rijNummer());
            int colDiff = Math.abs(lastClickedPosition.kolomNummer() - clickedPosition.kolomNummer());

            if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
                model.swapPositions(lastClickedPosition, clickedPosition);
                model.updateBoard();
                candyCrushView.updateView();
                Score.setText(String.valueOf(model.getScore()));
            }
            lastClickedPosition = null;
        }
    }
}