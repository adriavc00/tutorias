/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLDetailTutoriaController implements Initializable {

    private boolean modifiedT = false;
    private FXMLTutoriasController tutoriasController;

    @FXML
    private ListView<Alumno> listViewStudents;
    @FXML
    private Label subject;
    @FXML
    private Label initHour;
    @FXML
    private Label duration;
    @FXML
    private Label state;
    @FXML
    private Label date;
    @FXML
    private Button cancelButton;
    @FXML
    private Button notDoneButton;

    private Tutoria tutoria;
    @FXML
    private TextArea anotacionesField;
    @FXML
    private Button exitButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initialize() {
        tutoria = tutoriasController.getSelectedTutoria();

        subject.setText(tutoria.getAsignatura().getCodigo());
        initHour.setText(tutoria.getInicio().toString());
        duration.setText(tutoria.getDuracion().toMinutes() + " mins");
        state.setText(tutoria.getEstado().toString());
        date.setText(tutoria.getFecha().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")));
        anotacionesField.setText(tutoria.getAnotaciones());

        listViewStudents.setItems(tutoria.getAlumnos());
        listViewStudents.setCellFactory((cell) -> new ListCell<Alumno>() {
            @Override
            protected void updateItem(Alumno item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getNombre() + " " + item.getApellidos());
                }
            }
        });
    }

    @FXML
    private void notDoneTutoria(ActionEvent event) {
        //tutoria.setEstado(Tutoria.EstadoTutoria.NO_ASISTIDA);
        modifiedT = true;
        dialogo();
    }

    @FXML
    private void cancelTutoria(ActionEvent event) {
        //tutoria.setEstado(Tutoria.EstadoTutoria.ANULADA);
        modifiedT = true;
        dialogo();
    }

    @FXML
    private void exit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    private void dialogo() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("No asistida");
        alerta.setContentText("Ha marcado la asignatura como " + "no asistida");
        alerta.showAndWait();
    }

    public boolean modifiedT() {
        return modifiedT;
    }

    public void setController(FXMLTutoriasController controller) {
        this.tutoriasController = controller;
    }
}
