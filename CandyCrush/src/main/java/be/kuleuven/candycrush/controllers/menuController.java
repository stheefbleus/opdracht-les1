package be.kuleuven.candycrush.controllers;

import be.kuleuven.candycrush.HelloApplication;
import be.kuleuven.candycrush.model.model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class menuController {
    @FXML
    private be.kuleuven.candycrush.model.model model;
    @FXML
    private AnchorPane menu;
    @FXML
    private Button btn;
    @FXML
    private TextField speler;

    @FXML
    public void initialize(){
        model model = new model("Default Player");
        btn.setText("Start");
        btn.setOnAction(this::inDruk);
        speler.setPromptText(model.getSpeler());
    }

    public void inDruk(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("candyCrush-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setTitle("current player: "+ speler.getText());
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
