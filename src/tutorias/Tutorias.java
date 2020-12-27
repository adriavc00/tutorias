package tutorias;

import accesoBD.AccesoBD;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Asignatura;

/**
 * Clase principal de la aplicación desde la cual se abria una ventana cargada de el fichero
 * views.FXMLMain.fxml situado en el paquete views. A partir de ahí los eventos se gestionaran desde
 * controllers.FXMLMainController y los consecuentes.
 *
 * @author Adria V.
 * @author Felipe Z.
 */
public class Tutorias extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        /*
         * Guardamos las asignaturas obligatorias la primera vez que la aplicación se arranca en la
         * primera y segunda posicion de la lista de asignaturas de la base de datos.
         */
        Asignatura tfg = new Asignatura("TFG", "Trabajo Fin de Grado");
        Asignatura tfm = new Asignatura("TFM", "Trabajo Fin de Master");

        AccesoBD BDaccess = AccesoBD.getInstance();
        ObservableList<Asignatura> subjects = BDaccess.getTutorias().getAsignaturas();

        if (!subjects.get(0).getCodigo().equals(tfg.getCodigo())
                || !subjects.get(1).getCodigo().equals(tfm.getCodigo())) {
            subjects.add(0, tfg);
            subjects.add(1, tfm);
            BDaccess.salvar();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/views/FXMLMain.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle("Gestor de tutorias");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
