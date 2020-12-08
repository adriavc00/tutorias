/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLMainController implements Initializable {
    @FXML
    private MenuItem addSubjectItem;
    @FXML
    private MenuItem addStudentsItem;
    @FXML
    private MenuItem modifySubjectItem;
    @FXML
    private MenuItem deleteSubjectItem;
    @FXML
    private MenuItem modifyStudentItem;
    @FXML
    private MenuItem deleteStudentItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addSubject(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLAddSubject.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir asignatura");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLaddSubjectController controller = customLoader.getController();
        // TODO: Functionality
    }

    @FXML
    private void addStudent(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLAddStudent.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir alumno");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLaddStudentController controller = customLoader.getController();
    }

    @FXML
    private void modifySubject(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLListSubjects.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lista de asignaturas");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLListSubjectsController controller = customLoader.getController();
    }

    @FXML
    private void deleteSubject(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLListSubjects.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lista de asignaturas");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLListSubjectsController controller = customLoader.getController();
    }

    @FXML
    private void modifyStudent(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLListStudents.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lista de asignaturas");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLListStudentsController controller = customLoader.getController();
    }

    @FXML
    private void deleteStudent(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLListStudents.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lista de alumnos");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLListStudentsController controller = customLoader.getController();
    }

}
