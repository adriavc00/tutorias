/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorias;

import accesoBD.AccesoBD;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;

/**
 *
 * @author ADRIA - LP
 */
public class Tutorias extends Application {

    private ObservableList<Alumno> students;
    private ObservableList<Asignatura> subjects;
    private AccesoBD BDaccess;

    @Override
    public void start(Stage stage) throws Exception {

        BDaccess = AccesoBD.getInstance();
        subjects = BDaccess.getTutorias().getAsignaturas();

        Asignatura tfg = new Asignatura("TFG", "Trabajo Fin de Grado");
        Asignatura tfm = new Asignatura("TFM", "Trabajo Fin de Master");

        if (!subjects.get(0).getCodigo().equals(tfg.getCodigo())
                || !subjects.get(1).getCodigo().equals(tfm.getCodigo())) {
            subjects.add(0, tfg);
            subjects.add(1, tfm);
            BDaccess.salvar();
        }

        //ESTO ES PARA PRUEBAS
        students = BDaccess.getTutorias().getAlumnosTutorizados();
        if (students.isEmpty()) {
            Alumno a = new Alumno();
            Alumno b = new Alumno("n", "a", "email@");
            students.add(a);
            students.add(a);
            BDaccess.salvar();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/views/FXMLLogin.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMinHeight(420);
        stage.setMinWidth(650);
        stage.setTitle("Iniciar sesión");
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
