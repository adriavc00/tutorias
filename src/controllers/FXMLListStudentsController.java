/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLListStudentsController implements Initializable {
    private AccesoBD BDaccess;
    private ObservableList<Alumno> students;

    @FXML
    private Button addStudent;
    @FXML
    private Button deleteStudent;
    @FXML
    private TableView<Alumno> studentsTable;
    @FXML
    private TableColumn<Alumno, String> nameColumn;
    @FXML
    private TableColumn<Alumno, String> surnameColumn;
    @FXML
    private TableColumn<Alumno, String> emailColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteStudent.disableProperty().bind(Bindings.lessThan(studentsTable.getSelectionModel().
            selectedIndexProperty(), 0));
        BDaccess = AccesoBD.getInstance();
        students = BDaccess.getTutorias().getAlumnosTutorizados();
        studentsTable.setItems(students);
        nameColumn.setCellValueFactory((cellData) -> cellData.getValue().nombreProperty());
        surnameColumn.setCellValueFactory((cellData) -> cellData.getValue().apellidosProperty());
        emailColumn.setCellValueFactory((cellData) -> cellData.getValue().emailProperty());
    }

    @FXML
    private void add(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().
            getResource("/views/FXMLAddStudent.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir alumno");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLNewStudentController controller = customLoader.getController();

        if (controller.pressedOk()) {
            students.add(controller.getNewStudent());
            BDaccess.salvar();
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        Alumno selected = studentsTable.getSelectionModel().getSelectedItem();
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Alumno");
        alerta.setHeaderText("Confirmación");
        alerta.setContentText("¿Seguro que quieres eliminar el alumno " + selected.getNombre() + " "
                                  + selected.getApellidos() + "? Se mantendrá el alumno en las tutorías que ya han sido creadas");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            students.remove(studentsTable.getSelectionModel().getSelectedIndex());
            BDaccess.salvar();
        }
    }

}
