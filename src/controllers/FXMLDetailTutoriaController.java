/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLDetailTutoriaController implements Initializable {

    private boolean modifiedT = false;
    @FXML
    private ListView<?> listViewStudents;
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
        // TODO
        FXMLLoader customLoader = new FXMLLoader(getClass().
                        getResource("/views/FXMLTutorias.fxml"));
        FXMLTutoriasController controller = customLoader.getController();
        //System.out.println(controller.pruebaInt());
        /*tutoria = controller.getSelectedTutoria();
        
        /*subject.setText(tutoria.getAsignatura().toString());
        initHour.setText(tutoria.getInicio().toString());
        duration.setText(tutoria.getDuracion().toString());
        state.setText(tutoria.getEstado().toString());
        date.setText(tutoria.getFecha().toString());
        anotacionesField.setText(tutoria.getAnotaciones());*/
        
        /*anotacionesField.textProperty().addListener((a, oldV, newV) -> {
            tutoria.setAnotaciones(newV);
            modifiedT = true;
        });*/
        
        
        
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
}
