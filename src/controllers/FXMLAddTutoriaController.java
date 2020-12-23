/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;
import modelo.util.DurationAdapter;

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
    private Asignatura subjectSelected;
    @FXML
    private ListView<Alumno> listViewStudents;
    @FXML
    private Button addStudent;
    @FXML
    private ComboBox<Asignatura> comboBoxSubjects;
    @FXML
    private ComboBox<Alumno> comboBoxStudents;
    @FXML
    private Button addTutoriaButton;
    @FXML
    private Slider sliderTime;
    @FXML
    private TextField AnotacionesField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addStudent.disableProperty().bind(Bindings.lessThan(comboBoxStudents.getSelectionModel().
            selectedIndexProperty(), 0));
        BDaccess = AccesoBD.getInstance();
        subjects = BDaccess.getTutorias().getAsignaturas();
        students = BDaccess.getTutorias().getAlumnosTutorizados();
        //subjectList.setItems(subjects);
        comboBoxSubjects.setItems(subjects);
        comboBoxSubjects.setCellFactory((cell) -> new ListCell<Asignatura>() {
            @Override
            protected void updateItem(Asignatura item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getCodigo());
                }
            }
            
        });
        comboBoxStudents.setItems(students);
        comboBoxStudents.setCellFactory((cell) -> new ListCell<Alumno>() {
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
        Alumno selected = comboBoxStudents.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println(selected);
            studentsSelected.add(selected);
        }
    }

    @FXML
    private void addTutoria(ActionEvent event) {
        Duration duration = null;
        subjectSelected = comboBoxSubjects.getSelectionModel().getSelectedItem();
        Tutoria tutoriaNew = new Tutoria();
        
        //ANOTACIONES
        tutoriaNew.setAnotaciones(AnotacionesField.getText());
        
        //ASIGNATURA
        tutoriaNew.setAsignatura(subjectSelected);
        
        //DURATION
        int durationInt = (int)sliderTime.getValue();
        String durationString = String.valueOf(durationInt);
        DurationAdapter durationA = new DurationAdapter();
        /*try {
            duration = durationA.unmarshal(durationString);
        } catch (Exception ex) {
            Logger.getLogger(FXMLAddTutoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tutoriaNew.setDuracion(duration);
*/

        
        //ESTADO
        tutoriaNew.setEstado(Tutoria.EstadoTutoria.PEDIDA);
        
        //FECHA
        tutoriaNew.setFecha(LocalDate.MAX);
        
        //INICIO
        tutoriaNew.setInicio(LocalTime.MAX);
        
        //ALUMNOS
        ObservableList<Alumno> studentsInTutoria = tutoriaNew.getAlumnos();
        studentsInTutoria.addAll(studentsSelected);
        
        System.out.println(tutoriaNew.getAlumnos());
        System.out.println(tutoriaNew.getAnotaciones());
        System.out.println(tutoriaNew.getAsignatura());
        System.out.println(durationA);
        System.out.println(tutoriaNew.getDuracion());
        System.out.println(tutoriaNew.getEstado());
        System.out.println(tutoriaNew.getFecha());
        System.out.println(tutoriaNew.getInicio());
        
    }

}
