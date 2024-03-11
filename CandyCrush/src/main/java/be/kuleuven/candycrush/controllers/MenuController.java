package be.kuleuven.candycrush.controllers;

import be.kuleuven.candycrush.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane menu;
    @FXML
    private Button btn;
    @FXML
    private TextField speler;

    @FXML
    public void initialize(){
        btn.setText("Start");
        if(speler.getText() != ""){
            btn.setOnAction(this::inDruk);
        }
    }

    public void inDruk(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("candyCrush-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
