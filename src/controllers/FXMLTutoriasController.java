/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ADRIA - LP
 */
public class FXMLTutoriasController implements Initializable {
    @FXML
    private HBox mainPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader customLoader = new FXMLLoader(getClass().getResource("/views/FXMLGridTimeSlot.fxml"));
            Node timeSlot = customLoader.load();
            ObservableList<Node> children = mainPane.getChildren();
            children.add(timeSlot);
        } catch (IOException e) {
        }
    }

}
