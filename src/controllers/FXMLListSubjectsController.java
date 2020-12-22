/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import accesoBD.AccesoBD;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author lipez
 */
public class FXMLListSubjectsController implements Initializable {
    private AccesoBD BDaccess;
    private ObservableList<Asignatura> subjects = null;

    @FXML
    private Button addSubject;
    @FXML
    private Button deleteSubject;
    @FXML
    private TableView<Asignatura> subjectsTable;
    @FXML
    private TableColumn<Asignatura, String> codeColumn;
    @FXML
    private TableColumn<Asignatura, String> descriptionColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteSubject.disableProperty().bind(Bindings.lessThan(subjectsTable.getSelectionModel().
            selectedIndexProperty(), 0));
        BDaccess = AccesoBD.getInstance();
        subjects = BDaccess.getTutorias().getAsignaturas();
        subjectsTable.setItems(subjects);
        codeColumn.setCellValueFactory((cellData) -> cellData.getValue().codigoProperty());
        descriptionColumn.setCellValueFactory((cellData) -> cellData.getValue().
            descripcionProperty());
        //subjectList.setCellFactory((cell) -> new ListCell<Asignatura>() {
        //    @Override
        //    protected void updateItem(Asignatura item, boolean empty) {
        //        super.updateItem(item, empty);
        //        if (empty || item == null) {
        //            setText("");
        //        } else {
        //            setText(item.getCodigo());
        //        }
        //    }
        //});
    }

    @FXML
    private void add(ActionEvent event) throws IOException {
        FXMLLoader customLoader = new FXMLLoader(getClass().
            getResource("/views/FXMLAddSubject.fxml"));
        Parent root = customLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Añadir asignatura");
        stage.setScene(scene);
        stage.showAndWait();
        FXMLNewSubjectController controller = customLoader.getController();

        if (controller.pressedOk()) {
            subjects.add(controller.getNewSubject());
            BDaccess.salvar();
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        subjects.remove(subjectsTable.getSelectionModel().getSelectedIndex());
        BDaccess.salvar();
    }

    void setMain(FXMLMainController aThis) {
        //para pasar parámetros otros controladores
    }

}
