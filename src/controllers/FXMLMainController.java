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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLMainController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private Button tutoriasButton;
    @FXML
    private Button subjectsButton;
    @FXML
    private Button studentsButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader customLoader = new FXMLLoader(getClass().getResource(
                "/views/FXMLTutorias.fxml"));
            Node tutorias = customLoader.load();
            mainPane.setCenter(tutorias);
            this.tutoriasButton.setDisable(true);
        } catch (IOException e) {
        }
    }

    @FXML
    private void tutoriasPressed(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLTutorias.fxml"));
        Node tutorias = customLoader.load();
        // La linea siguiente da error.
        // ((FXMLListSubjectsController) customLoader.getController()).setMain(this); //Pasar controller a otro
        mainPane.setCenter(tutorias);
        this.tutoriasButton.setDisable(true);
        this.subjectsButton.setDisable(false);
        this.studentsButton.setDisable(false);
    }

    @FXML
    private void subjectsPressed(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource(
            "/views/FXMLListSubjects.fxml"));
        Node tutorias = customLoader.load();
        mainPane.setCenter(tutorias);
        this.tutoriasButton.setDisable(false);
        this.subjectsButton.setDisable(true);
        this.studentsButton.setDisable(false);
    }

    @FXML
    private void studentsPressed(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource(
            "/views/FXMLListStudents.fxml"));
        Node tutorias = customLoader.load();
        mainPane.setCenter(tutorias);
        this.tutoriasButton.setDisable(false);
        this.subjectsButton.setDisable(false);
        this.studentsButton.setDisable(true);
    }
}
