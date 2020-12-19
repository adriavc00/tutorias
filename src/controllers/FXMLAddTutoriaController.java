/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLAddTutoriaController implements Initializable {
    @FXML
    private Button cancelButton;
    ObservableList<Alumno> students;
    ObservableList<Asignatura> subjects;
    @FXML
    private ListView<Alumno> listViewStudents;
    @FXML
    private ChoiceBox<Alumno> choiceBoxStudents;
    @FXML
    private ChoiceBox<Asignatura> choiceBoxSubjects;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /*
         * FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLMain.fxml"));
         * FXMLMainController controller = customLoader.getController(); Tutorias misTutorias =
         * controller.getTutorias();
         *
         *
         * //DA ERROR DE NULLPOINTEREXCEPTION, ARREGLAR subjects = misTutorias.getAsignaturas();
         * choiceBoxSubjects.setItems(subjects); students = misTutorias.getAlumnosTutorizados();
        choiceBoxStudents.setItems(students);
         */
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}
