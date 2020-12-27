/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLDetailTutoriaController implements Initializable {

    private boolean modifiedA = false;
    private boolean modifiedR = false;
    private FXMLTutoriasController tutoriasController;
    private TextArea anotacionesF = new TextArea();

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

    private Tutoria tutoria;
    @FXML
    private Label anotacionesField;
    @FXML
    private Button exitButton;
    @FXML
    private Button doneButton;

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
        String anotaciones = tutoria.getAnotaciones();
        if (anotaciones == null || anotaciones.isEmpty()) {
            anotacionesField.setText("No hay anotaciones en la tutoria.");
            anotacionesField.setFont(Font.font("System", FontPosture.ITALIC, 11));
            anotacionesField.setTextFill(Color.GREY);
        } else {
            anotacionesField.setText(tutoria.getAnotaciones());
        }

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
        if (!tutoria.getEstado().equals(Tutoria.EstadoTutoria.PEDIDA)) {
            cancelButton.setDisable(true);
            doneButton.setDisable(true);
        }
    }

    @FXML
    private void cancelTutoria(ActionEvent event) {
        dialogo("Anulada");
    }

    @FXML
    private void doneTutoria(ActionEvent event) {
        dialogo("Realizada");
    }

    @FXML
    private void exit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    private void dialogo(String state) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Nuevo estado: " + state);
        if (state.equals("Realizada")) {
            Label textAlertState = new Label();
            textAlertState.setText("Ha marcado la asignatura como " + state);
            Label textAlertAnotaciones = new Label();
            textAlertAnotaciones.setText("Anotaciones: ");
            textAlertAnotaciones.setPadding(new Insets(5, 0, 5, 0));
            anotacionesF.setPrefHeight(100);
            anotacionesF.setMinHeight(100);
            anotacionesF.wrapTextProperty().setValue(true);
            Node vBoxAlert = new VBox(textAlertState, textAlertAnotaciones, anotacionesF);
            alerta.getDialogPane().setContent(vBoxAlert);
        } else {
            alerta.setContentText("Ha marcado la asignatura como " + state);
        }
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (state.equals("Realizada")) {
                modifiedR = true;
            } else if (state.equals("Anulada")) {
                modifiedA = true;
            }
            exit(null);

        }
    }

    public boolean modifiedA() {
        return modifiedA;
    }

    public boolean modifiedR() {
        return modifiedR;
    }

    public String getNotesTutoria() {
        return anotacionesF.getText();
    }

    public void setController(FXMLTutoriasController controller) {
        this.tutoriasController = controller;
    }

}
