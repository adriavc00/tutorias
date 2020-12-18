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
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLNewSubjectController implements Initializable {
    private Asignatura subject;
    private boolean pressedOk = false;

    @FXML
    private Button cancelButton;
    @FXML
    private TextField code;
    @FXML
    private TextField description;
    @FXML
    private Button addButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.disableProperty().bind(
            Bindings.or(code.textProperty().isEmpty(), description.textProperty().isEmpty()));
    }

    public boolean pressedOk() {
        return pressedOk;
    }

    public Asignatura getNewSubject() {
        return subject;
    }

    @FXML
    private void addSubject(ActionEvent event) {
        pressedOk = true;
        subject = new Asignatura(code.getText(), description.getText());
        ((Stage) addButton.getScene().getWindow()).close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}
