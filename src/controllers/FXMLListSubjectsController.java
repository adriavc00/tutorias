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
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLListSubjectsController implements Initializable {

    @FXML
    private Button addSubject;
    @FXML
    private Button deleteSubject;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        deleteSubject.setDisable(true);
        
    }    

    @FXML
    private void add(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLAddSubject.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir asignatura");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLNewSubjectController controller = customLoader.getController();
    }

    @FXML
    private void delete(ActionEvent event) {
    }
    
}
