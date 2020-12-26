/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLAddTutoriaController implements Initializable {
    private ObservableList<Alumno> students;
    private ObservableList<Asignatura> subjects;
    private AccesoBD BDaccess;
    private ObservableList<Alumno> studentsSelected;
    private Asignatura subjectSelected;
    private Tutoria tutoria;
    private boolean pressedOk = false;

    @FXML
    private Button cancelButton;
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
    private TextArea AnotacionesField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> hours;
    @FXML
    private Spinner<Integer> minutes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BDaccess = AccesoBD.getInstance();
        subjects = BDaccess.getTutorias().getAsignaturas();
        students = BDaccess.getTutorias().getAlumnosTutorizados();
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
        comboBoxSubjects.setConverter(new StringConverter<Asignatura>() {
            @Override
            public String toString(Asignatura object) {
                return object.getCodigo();
            }

            @Override
            public Asignatura fromString(String string) {
                return null;
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
        comboBoxStudents.setConverter(new StringConverter<Alumno>() {
            @Override
            public String toString(Alumno object) {
                String res = object.getNombre() + " " + object.getApellidos();
                return res;
            }

            @Override
            public Alumno fromString(String string) {
                return null;
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
        //TODO: Cambiar el factory del Spinner para poner dos dígitos

        // BINDINGS
        addStudent.disableProperty().bind(Bindings.lessThan(comboBoxStudents.getSelectionModel().
            selectedIndexProperty(), 0));

        addTutoriaButton.disableProperty().bind(
            Bindings.or(Bindings.isEmpty(studentsSelected),
                        Bindings.or(Bindings.lessThan(comboBoxSubjects.getSelectionModel().
                            selectedIndexProperty(), 0), Bindings.isNull(datePicker.valueProperty())))
        );
    }

    public boolean pressedOk() {
        return pressedOk;
    }

    public Tutoria getNewTutoria() {
        return tutoria;
    }

    public void setTimeParameters(LocalDateTime start) {
        datePicker.setValue(start.toLocalDate()); // XXX The change is not displayed
        // hours.setValue(start.getHour());
        // minutes.setValue(start.getMinute());
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void pressedAddStudent(ActionEvent event) {
        Alumno selected = comboBoxStudents.getSelectionModel().getSelectedItem();
        if (selected != null) {
            studentsSelected.add(selected);
        }
    }

    @FXML
    private void addTutoria(ActionEvent event) {
        subjectSelected = comboBoxSubjects.getSelectionModel().getSelectedItem();
        tutoria = new Tutoria();

        //ANOTACIONES
        tutoria.setAnotaciones(AnotacionesField.getText());

        //ASIGNATURA
        tutoria.setAsignatura(subjectSelected);

        //DURATION
        int durationInt = (int) sliderTime.getValue();
        Duration duration = Duration.ofMinutes(durationInt);
        tutoria.setDuracion(duration);

        //ESTADO
        tutoria.setEstado(Tutoria.EstadoTutoria.PEDIDA);

        //FECHA
        tutoria.setFecha(datePicker.getValue());

        //INICIO
        int h = Integer.parseInt(hours.getValue().toString());
        int m = Integer.parseInt(minutes.getValue().toString());
        LocalTime initTime = LocalTime.of(h, m);
        tutoria.setInicio(initTime);

        //ALUMNOS
        ObservableList<Alumno> studentsInTutoria = tutoria.getAlumnos();
        studentsInTutoria.addAll(studentsSelected);

        pressedOk = true;

        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void adjustMaxValue(DragEvent event) {
        LocalTime maxTime = LocalTime.of(20, 0);
        int h = Integer.parseInt(hours.getValue().toString());
        int m = Integer.parseInt(minutes.getValue().toString());
        LocalTime initTime = LocalTime.of(h, m);
        if (initTime.plusMinutes((long) sliderTime.getValue()).isAfter(maxTime)) {
            double max = ChronoUnit.MINUTES.between(maxTime, initTime);
            sliderTime.setValue(max);
        }
    }

}
