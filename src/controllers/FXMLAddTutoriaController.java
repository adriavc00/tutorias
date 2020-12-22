/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
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
    private ObservableList<Alumno> students;
    private ObservableList<Asignatura> subjects;
    private AccesoBD BDaccess;
    private ObservableList<Alumno> studentsSelected;
    @FXML
    private ListView<Alumno> listViewStudents;
    @FXML
    private ChoiceBox<Alumno> choiceBoxStudents;
    @FXML
    private ChoiceBox<Asignatura> choiceBoxSubjects;
    @FXML
    private Button addStudent;

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
         * choiceBoxStudents.setItems(students);
         */
        BDaccess = AccesoBD.getInstance();
        subjects = BDaccess.getTutorias().getAsignaturas();
        students = BDaccess.getTutorias().getAlumnosTutorizados();
        //subjectList.setItems(subjects);
        choiceBoxSubjects.setItems(subjects);
        choiceBoxStudents.setItems(students);
        studentsSelected = FXCollections.observableArrayList();
        listViewStudents.setItems(studentsSelected);
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
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void pressedAddStudent(ActionEvent event) {
        Alumno selected = choiceBoxStudents.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println(selected);
            studentsSelected.add(selected);
        }
    }

}
