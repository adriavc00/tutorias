/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLTutoriasController implements Initializable {
    private AccesoBD BDaccess = AccesoBD.getInstance();
    private ObservableList<Tutoria> tutoriasCon;

    @FXML
    private HBox mainPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button noDoneButton;
    @FXML
    private Button addCommentButton;
    @FXML
    private DatePicker day;
    @FXML
    private GridPane grid;
    @FXML
    private Label lunesCol;
    @FXML
    private Label martesCol;
    @FXML
    private Label miercoles;
    @FXML
    private Label juevesCol;
    @FXML
    private Label viernesCol;
    @FXML
    private Label slotSelected;

    private final LocalTime firstSlotStart = LocalTime.of(10, 0);
    private final Duration slotLength = Duration.ofMinutes(10);
    private final LocalTime lastSlotStart = LocalTime.of(17, 30);
    private final int NUMBER_OF_SLOTS_PER_DAY = (lastSlotStart.toSecondOfDay()
                                                     - firstSlotStart.toSecondOfDay()) / 600;

    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<List<TimeSlot>> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<TimeSlot> timeSlotSelected;

    private List<Label> diasSemana;

    public class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;
        protected final Pane view;

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start;
            this.duration = duration;
            view = new Pane();
            view.getStyleClass().add("time-slot");
            // ---------------------------------------------------------------
            // de esta manera cambiamos la apariencia del TimeSlot cuando los seleccionamos
            selectedProperty().addListener((obs, wasSelected, isSelected)
                -> view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));

        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public LocalDate getDate() {
            return start.toLocalDate();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }

        public Node getView() {
            return view;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //creamos los slots necesarios para un dia
        LocalTime time = firstSlotStart;
        for (int i = 1; i <= NUMBER_OF_SLOTS_PER_DAY; i++) {
            String period = "";
            period += time.toString();
            period += " - ";
            time = time.plusMinutes(10);
            period += time.toString();
            Label label = new Label(period);
            label.setFont(Font.font(10));
            grid.add(label, 0, i);
        }
        // dejo los label de las columnas en un list<> para usarlo en un bucle
        diasSemana = new ArrayList<>();
        diasSemana.add(lunesCol);
        diasSemana.add(martesCol);
        diasSemana.add(miercoles);
        diasSemana.add(juevesCol);
        diasSemana.add(viernesCol);

        timeSlotSelected = new SimpleObjectProperty<>();

        //---------------------------------------------------------------------
        //inicializa el DatePicker al dia actual
        day.setValue(LocalDate.now());

        //---------------------------------------------------------------------
        // pinta los SlotTime en el grid
        setTimeSlotsGrid(day.getValue());

        //---------------------------------------------------------------------
        //cambia los SlotTime al cambiar de dia
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
        });

        //---------------------------------------------------------------------
        // enlazamos timeSlotSelected con el label para mostrar la seleccion
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
        timeSlotSelected.addListener((a, b, c) -> {
            if (c == null) {
                slotSelected.setText("");
            } else {
                slotSelected.setText(c.getDate().format(dayFormatter)
                                         + "-"
                                         + c.getStart().format(timeFormatter));
            }
        });

        BDaccess = AccesoBD.getInstance();
        tutoriasCon = BDaccess.getTutorias().getTutoriasConcertadas();

        for (Tutoria tutoria : tutoriasCon) {
            paintTutoria(tutoria);
        }
    }

    private void setTimeSlotsGrid(LocalDate date) {
        //actualizamos la seleccion
        timeSlotSelected.setValue(null);

        //--------------------------------------------
        //borramos los SlotTime del grid
        ObservableList<Node> children = grid.getChildren();
        for (List<TimeSlot> dias : timeSlots) {
            for (TimeSlot timeSlot : dias) {
                for (Node node : children) {

                }
                children.remove(timeSlot.getView());
            }
        }

        timeSlots = new ArrayList<>();

        //---------------------------------------------------------------------------
        // recorremos para cada Columna y creamos para cada slot
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(4);
        int diaIndex = 1;
        for (LocalDate dia = startOfWeek; !dia.isAfter(endOfWeek); dia = dia.plusDays(1)) {
            diasSemana.get(diaIndex - 1).setText(dia.getDayOfWeek() + System.lineSeparator() + dia.
                toString());
            List<TimeSlot> slotsDia = new ArrayList<TimeSlot>();
            timeSlots.add(slotsDia);
            //----------------------------------------------------------------------------------
            // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
            int slotIndex = 1;
            for (LocalDateTime startTime = dia.atTime(firstSlotStart);
                 !startTime.isAfter(dia.atTime(lastSlotStart));
                 startTime = startTime.plus(slotLength)) {

                //---------------------------------------------------------------------------------------
                // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
                TimeSlot timeSlot = new TimeSlot(startTime, slotLength);
                slotsDia.add(timeSlot);
                registerHandlers(timeSlot);
                //-----------------------------------------------------------
                // lo anyadimos al grid en la posicion x= pista+1, y= slotIndex
                grid.add(timeSlot.getView(), diaIndex, slotIndex);
                slotIndex++;
            }
            diaIndex++;
        }

    }

    private void registerHandlers(TimeSlot timeSlot) {

        timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            //---------------------------------------------slot----------------------------
            //solamente puede estar seleccionado un slot dentro de la lista de slot
            //sin el bucle exterior se podria seleccionar un SlotTime por cada columna
            timeSlots.forEach((dia) -> {
                dia.forEach(slot -> {
                    slot.setSelected(slot == timeSlot);
                });
            });

            //----------------------------------------------------------------
            //actualizamos el label Dia-Hora-Pista, esto es ad hoc,  para mi diseño
            timeSlotSelected.setValue(timeSlot);
            //----------------------------------------------------------------
            // si es un doubleClik  vamos a mostrar una alerta y cambiar el estilo de la celda
            if (event.getClickCount() > 1) {
                FXMLLoader customLoader = new FXMLLoader(getClass().
                    getResource("/views/FXMLAddTutoria.fxml"));
                try {
                    Parent root = customLoader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Añadir tutoria");
                    stage.setScene(scene);
                    stage.showAndWait();

                } catch (IOException e) {
                }
                FXMLAddTutoriaController controller = customLoader.getController();
                controller.setTimeParameters(timeSlot.start);
                if (controller.pressedOk()) {
                    Tutoria newTutoria = controller.getNewTutoria();
                    paintTutoria(newTutoria);
                    tutoriasCon.add(newTutoria);
                    BDaccess.salvar();
                }
            }
        });

    }

    @FXML
    private void registerPressed(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().
            getResource("/views/FXMLAddTutoria.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir tutoria");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLAddTutoriaController controller = customLoader.getController();

        if (controller.pressedOk()) {
            Tutoria newTutoria = controller.getNewTutoria();
            paintTutoria(newTutoria);
            tutoriasCon.add(newTutoria);
            BDaccess.salvar();
        }
    }

    private void paintTutoria(Tutoria tutoria) {
        int dayIndex = tutoria.getFecha().getDayOfWeek().getValue() - 1;
        int timeIndex
            = (tutoria.getInicio().toSecondOfDay() - firstSlotStart.toSecondOfDay()) / 600;
        for (int i = 0; (i * 10) < tutoria.getDuracion().toMinutes(); i += 1) {
            TimeSlot timeSlot = timeSlots.get(dayIndex).get(timeIndex + i);
            timeSlot.getView().setStyle("-fx-background-color: blue;");
            //ObservableList<String> styles = timeSlot.getView().getStyleClass();
            //if (styles.contains("time-slot")) {
            //    styles.remove("time-slot");
            //    styles.add("time-slot-libre");
            //} else {
            //    styles.remove("time-slot-libre");
            //    styles.add("time-slot");
            //}
        }
    }
}
