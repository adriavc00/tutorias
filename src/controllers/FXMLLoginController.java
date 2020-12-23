/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLLoginController implements Initializable {
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button startButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void startPressed(ActionEvent event) throws IOException {
        // Do stuff with user and password.
        ((Stage) startButton.getScene().getWindow()).close();
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLMain.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Tutorías");
        stage.setMinHeight(1000);
        stage.setMinWidth(900);
        stage.setScene(scene);
        stage.show();
    }

}
