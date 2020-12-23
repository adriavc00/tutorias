/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLTutoriasController implements Initializable {
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

    private final LocalTime firstSlotStart = LocalTime.of(8, 0);
    private final Duration slotLength = Duration.ofMinutes(10);
    private final LocalTime lastSlotStart = LocalTime.of(20, 0);

    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<List<TimeSlot>> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<TimeSlot> timeSlotSelected;

    private List<Label> diasSemana;

    private class TimeSlot {

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
        // creamos todas las filas necesarias para el dia.

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
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("SlotTime");
                alerta.setHeaderText("Confirma la selecció");
                alerta.setContentText("Has seleccionat: "
                                          + timeSlot.getDate().format(DateTimeFormatter.
                        ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                          + timeSlot.getTime().format(DateTimeFormatter.
                        ofLocalizedTime(FormatStyle.SHORT)));
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ObservableList<String> styles = timeSlot.getView().getStyleClass();
                    if (styles.contains("time-slot")) {
                        styles.remove("time-slot");
                        styles.add("time-slot-libre");
                    } else {
                        styles.remove("time-slot-libre");
                        styles.add("time-slot");
                    }
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
    }
}
