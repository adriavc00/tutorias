/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Alumno;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLNewStudentController implements Initializable {
    private boolean pressedOk = false;
    private Alumno student;

    @FXML
    private Button cancelButton;
    @FXML
    private TextField name;
    @FXML
    private TextField surnames;
    @FXML
    private TextField email;
    @FXML
    private Button addButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.disableProperty().bind(
            Bindings.or(name.textProperty().isEmpty(), Bindings.or(surnames.textProperty().isEmpty(),
                email.textProperty().isEmpty())));
    }

    public boolean pressedOk() {
        return pressedOk;
    }

    public Alumno getNewStudent() {
        return student;
    }

    @FXML
    private void addStudent(ActionEvent event) {
        pressedOk = true;
        student = new Alumno(name.getText(), surnames.getText(), email.getText());
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}
