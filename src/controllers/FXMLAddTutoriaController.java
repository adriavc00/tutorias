/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;
import modelo.Tutorias;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLAddTutoriaController implements Initializable {
    private ObservableList<Alumno> students;
    private ObservableList<Asignatura> subjects;
    private ObservableList<Tutoria> tutorias;
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
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> hours;
    @FXML
    private Spinner<Integer> minutes;
    @FXML
    private TextArea anotacionesField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BDaccess = AccesoBD.getInstance();
        tutorias = BDaccess.getTutorias().getTutoriasConcertadas();
        subjects = BDaccess.getTutorias().getAsignaturas();
        students = BDaccess.getTutorias().getAlumnosTutorizados();

        comboBoxSubjects.setItems(subjects);
        comboBoxStudents.setItems(students);
        studentsSelected = FXCollections.observableArrayList();
        listViewStudents.setItems(studentsSelected);

        // CELL FACTORIES AND CONVERTERS
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
        //TODO: Cambiar el factory del Spinner para poner dos dígitos. https://stackoverflow.com/questions/45470508/javafx-spinner-units

        // BINDINGS
        addStudent.disableProperty().bind(Bindings.lessThan(comboBoxStudents.getSelectionModel().
            selectedIndexProperty(), 0));

        addTutoriaButton.disableProperty().bind(
            Bindings.or(Bindings.isEmpty(studentsSelected),
                        Bindings.or(Bindings.lessThan(comboBoxSubjects.getSelectionModel().
                            selectedIndexProperty(), 0), Bindings.isNull(datePicker.valueProperty())))
        );

        // EVENT HANDLER
        sliderTime.setOnMouseReleased((event) -> {
            LocalTime maxTime = LocalTime.of(20, 0);
            int h = Integer.parseInt(hours.getValue().toString());
            int m = Integer.parseInt(minutes.getValue().toString());
            LocalTime initTime = LocalTime.of(h, m);
            if (initTime.plusMinutes((long) sliderTime.getValue()).isAfter(maxTime)) {
                double max = ChronoUnit.MINUTES.between(initTime, maxTime);
                sliderTime.setValue(max);
            }
        });
    }

    public boolean pressedOk() {
        return pressedOk;
    }

    public Tutoria getNewTutoria() {
        return tutoria;
    }

    public void setTimeParameters(LocalDateTime start) {
        datePicker.setValue(start.toLocalDate());
        hours.getValueFactory().setValue(start.getHour());
        minutes.getValueFactory().setValue(start.getMinute());
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void pressedAddStudent(ActionEvent event) {
        Alumno selected = comboBoxStudents.getSelectionModel().getSelectedItem();
        if (selected != null && !studentsSelected.contains(selected)) {
            studentsSelected.add(selected);
        }
    }

    @FXML
    private void addTutoria(ActionEvent event) {
        subjectSelected = comboBoxSubjects.getSelectionModel().getSelectedItem();
        
        tutoria = new Tutoria();

        //ANOTACIONES
        tutoria.setAnotaciones(anotacionesField.getText());

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
        
        //COMPROBANDO QUE LOS HORARIOS ESTÁN LIBRES
        boolean freeHour = true;    //ver si la hora está libre y no hay otra tutoría
        boolean notWeekend = true;  //ver que no coincida en fin de semana
        boolean inTime = true;  //que no se pase de las 20h
        
        //tutoría no puede caer en fin de semana
        DayOfWeek day = tutoria.getFecha().getDayOfWeek();
        
        if (day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY)) {
            notWeekend = false;
        }
        
        //FINAL
        m += durationInt;
        h += m / 60;
        m = m % 60;
        LocalTime endTime = LocalTime.of(h, m);
        //System.out.println(endTime);
        LocalTime eightOClock = LocalTime.of(20, 0);
        if (notWeekend && endTime.compareTo(eightOClock) > 0) {
            inTime = false;
        }
        
        if (notWeekend && inTime) {
            for (Tutoria t : tutorias) {
                int comparation = tutoria.getInicio().compareTo(t.getInicio());
                //System.out.println(comparation); 
                int hOther = t.getInicio().getHour();
                int mOther = t.getInicio().getMinute();
                int durationOther = (int) t.getDuracion().toMinutes();
                //System.out.println(hOther + ":" + mOther + ", " + durationOther);
                mOther += durationOther;
                hOther += mOther / 60;
                mOther = mOther % 60;
                LocalTime otherEndTime = LocalTime.of(hOther, mOther);
                //System.out.println(otherEndTime);

                if (tutoria.getFecha().equals(t.getFecha())) {
                    if (comparation == 0 || (comparation < 0 && endTime.compareTo(t.getInicio()) > 0) || comparation > 0 && tutoria.getInicio().compareTo(otherEndTime)< 0) {
                        freeHour = false;
                        break;
                    }
                }
            }
        }
            
        if (!freeHour || !notWeekend || !inTime) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            if (notWeekend && inTime) {
                alerta.setHeaderText("El horario elegido está ocupado");
            } else if (notWeekend) {
                alerta.setHeaderText("A partir de las 20h no se realizan tutorías");
            } else {
                alerta.setHeaderText("No se realizan tutorías los fines de semana");
            }
            alerta.showAndWait();
        } else {
            pressedOk = true;

            ((Stage) cancelButton.getScene().getWindow()).close();
        }
    }

}
