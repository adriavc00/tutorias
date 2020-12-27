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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
        private ObjectProperty<Tutoria> tutoria = new SimpleObjectProperty<Tutoria>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final ObjectProperty<Tutoria> tutoriaProperty() {
            return tutoria;
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
            view.getStyleClass().add("time-slot-libre");
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

        public void setTutoria(Tutoria tutoria) {
            this.tutoria.set(tutoria);
        }

        public Tutoria getTutoria() {
            return tutoria.get();
        }
    }
    @FXML
    private ScrollPane scrollPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //hacer que el grid ocupe todo
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        //creamos los slots necesarios para un dia
        LocalTime time = LocalTime.from(firstSlotStart);
        //firstSlotStart;
        for (int i = 1; i <= NUMBER_OF_SLOTS_PER_DAY; i++) {
            String period = "";
            period += time.toString();
            //period += " - ";
            time = time.plusMinutes(10);

            //period += time.toString();
            Label label = new Label(period);
            if (i % 6 == 1) {
                label.setFont(Font.font(10));
                label.setTextAlignment(TextAlignment.RIGHT);
                label.setPadding(new Insets(0, 5, 0, 0));
                grid.add(label, 0, i);
            } else {
                label.setText("");
                label.setFont(Font.font(5));
                grid.add(label, 0, i);
            }
        }
        //guardo las tutorias concertadas en la variable
        BDaccess = AccesoBD.getInstance();
        tutoriasCon = BDaccess.getTutorias().getTutoriasConcertadas();

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
        for (Tutoria tutoria : tutoriasCon) {
            LocalDate c = day.getValue();
            LocalDate startOfWeek = c.minusDays(c.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(4);
            if (tutoria.getFecha().isAfter(startOfWeek.minusDays(1)) // HE AÑADIDO LO DE MINUSDAYS(1)
                    && tutoria.getFecha().isBefore(endOfWeek)) {
                paintAndLinkTutoria(tutoria);
            }
        }

        //---------------------------------------------------------------------
        //cambia los SlotTime al cambiar de dia
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
            LocalDate startOfWeek = c.minusDays(c.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(4);
            for (Tutoria tutoria : tutoriasCon) {
                if (tutoria.getFecha().isAfter(startOfWeek.minusDays(1)) //HE AÑADIDO MINUSDAYS(1)
                        && tutoria.getFecha().isBefore(endOfWeek)) {
                    paintAndLinkTutoria(tutoria);
                }
            }
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

        // BINDINGS
        timeSlotSelected.addListener((a, oldV, newV) -> {
            if (a.getValue() == null) {
                cancelButton.disableProperty().set(true);
            } else if (a.getValue().getTutoria() == null || !a.getValue().getTutoria().getEstado().
                equals(Tutoria.EstadoTutoria.PEDIDA)) {
                cancelButton.disableProperty().set(true);
            } else {
                cancelButton.disableProperty().set(false);
            }
        });
    }

    public Tutoria getSelectedTutoria() {
        return timeSlotSelected.get().getTutoria();
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

                if (startTime.getMinute() == 0) {
                    timeSlot.getView().setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: red;");
                }

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
                if (timeSlot.getTutoria() == null) {
                    FXMLLoader customLoader = new FXMLLoader(getClass().
                        getResource("/views/FXMLAddTutoria.fxml"));
                    try {
                        Parent root = customLoader.load();
                        FXMLAddTutoriaController controller = customLoader.getController();
                        controller.setTimeParameters(timeSlot.start);

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Añadir tutoria");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.showAndWait();

                        if (controller.pressedOk()) {
                            Tutoria newTutoria = controller.getNewTutoria();
                            paintAndLinkTutoria(newTutoria);
                            tutoriasCon.add(newTutoria);
                            BDaccess.salvar();
                        }
                    } catch (IOException e) {
                    }
                } else {
                    FXMLLoader customLoader = new FXMLLoader(getClass().
                        getResource("/views/FXMLDetailTutoria.fxml"));
                    try {
                        Parent root = customLoader.load();
                        FXMLDetailTutoriaController controllerDetail = customLoader.getController();
                        controllerDetail.setController(this);
                        controllerDetail.initialize();

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Detalle de tutoría");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.showAndWait();
                        Tutoria tutoriaSelected = getSelectedTutoria();
                        if (controllerDetail.modifiedA()) {
                            //NO SÉ SI HAY QUE HACER ALGO ESPECIAL PARA GUARDAR LA TUTORÍA MODIFICADA
                            tutoriaSelected.setEstado(Tutoria.EstadoTutoria.ANULADA);
                            tutoriaSelected.setAnotaciones(controllerDetail.getNotesTutoria());
                            paintAndLinkTutoria(tutoriaSelected);
                            BDaccess.salvar();
                        } else if (controllerDetail.modifiedR()) {
                            tutoriaSelected.setEstado(Tutoria.EstadoTutoria.REALIZADA);
                            tutoriaSelected.setAnotaciones(controllerDetail.getNotesTutoria());
                            paintAndLinkTutoria(tutoriaSelected);
                            BDaccess.salvar();
                        }
                    } catch (IOException e) {
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
        stage.setResizable(false);
        stage.showAndWait();
        FXMLAddTutoriaController controller = customLoader.getController();

        if (controller.pressedOk()) {
            Tutoria newTutoria = controller.getNewTutoria();
            paintAndLinkTutoria(newTutoria);

            //PARA ELIMINAR LAS TUTORÍAS DE LA BASE DE DATOS
            /*
             * tutoriasCon.remove(0, tutoriasCon.size()-1); //PARA BORRAR TODAS LAS TUTORÍAS for
             * (Tutoria tutoria : tutoriasCon) { System.out.println(tutoria.getInicio()); }
             */
            tutoriasCon.add(newTutoria);
            BDaccess.salvar();
        }
    }

    private void paintAndLinkTutoria(Tutoria tutoria) {
        int dayIndex = tutoria.getFecha().getDayOfWeek().getValue() - 1;
        int timeIndex
            = (tutoria.getInicio().toSecondOfDay() - firstSlotStart.toSecondOfDay()) / 600;
        for (int i = 0; (i * 10) < tutoria.getDuracion().toMinutes(); i++) {    //HE QUITADO EL IGUAL, PINTABA UN SLOT DE MÁS
            TimeSlot timeSlot = timeSlots.get(dayIndex).get(timeIndex + i);
            timeSlot.setTutoria(tutoria);
            ObservableList<String> styleClass = timeSlot.getView().getStyleClass();
            switch (tutoria.getEstado()) {
                case ANULADA:
                    styleClass.remove("time-slot-libre");
                    styleClass.remove("time-slot-no-asistida");
                    styleClass.remove("time-slot-realizada");
                    styleClass.remove("time-slot-pedida");
                    styleClass.add("time-slot-anulada");
                    break;
                case NO_ASISTIDA:
                    styleClass.remove("time-slot-libre");
                    styleClass.remove("time-slot-realizada");
                    styleClass.remove("time-slot-anulada");
                    styleClass.remove("time-slot-pedida");
                    styleClass.add("time-slot-no-asistida");
                    break;
                case REALIZADA:
                    styleClass.remove("time-slot-libre");
                    styleClass.remove("time-slot-anulada");
                    styleClass.remove("time-slot-pedida");
                    styleClass.remove("time-slot-no-asistida");
                    styleClass.add("time-slot-realizada");
                    break;
                case PEDIDA:
                    styleClass.remove("time-slot-libre");
                    styleClass.remove("time-slot-anulada");
                    styleClass.remove("time-slot-no-asistida");
                    styleClass.remove("time-slot-realizada");
                    styleClass.add("time-slot-pedida");
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void notDonePressed(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = day.getValue();
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(4);
        for (Tutoria tutoria : tutoriasCon) {
            LocalDate tutoriaDate = tutoria.getFecha();
            if ((tutoriaDate.isBefore(now.toLocalDate())
                     && tutoria.getEstado().equals(Tutoria.EstadoTutoria.PEDIDA))
                    || (tutoriaDate.isEqual(now.toLocalDate())
                            && tutoria.getInicio().isBefore(now.toLocalTime())
                            && tutoria.getEstado().equals(Tutoria.EstadoTutoria.PEDIDA))) {
                tutoria.setEstado(Tutoria.EstadoTutoria.NO_ASISTIDA);
                if (tutoria.getFecha().isAfter(startOfWeek.minusDays(1))
                        && tutoria.getFecha().isBefore(endOfWeek)) {
                    paintAndLinkTutoria(tutoria);
                }
            }
        }
        BDaccess.salvar();
    }

    @FXML
    private void cancelPressed(ActionEvent event) {
        Tutoria tutoria = timeSlotSelected.get().getTutoria();
        tutoria.setEstado(Tutoria.EstadoTutoria.ANULADA);
        paintAndLinkTutoria(tutoria);
        BDaccess.salvar();
    }
}
