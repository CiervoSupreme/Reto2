package org.example.reto2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import org.example.reto2.Util.HibernateUtil;

public class MainApp extends Application {

    private static Stage primaryStage;

    private static final String FXML_LOGIN = "login-view.fxml";
    private static final String FXML_PRINCIPAL = "main-view.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        HibernateUtil.getSessionFactory();

        primaryStage = stage;
        primaryStage.setTitle("Gestor de Colección de Películas");

        mostrarLogin();
    }

    /**
     * Muestra la ventana de inicio de sesión. DEBE ser estático para llamarse desde los Controllers.
     * @throws IOException Si el archivo FXML no se encuentra o hay un error de carga.
     */
    public static void mostrarLogin() throws IOException {
        if (primaryStage == null) {
            System.err.println("Error: primaryStage no está inicializado. Esto no debería ocurrir.");
            return;
        }

        URL fxmlUrl = MainApp.class.getResource(FXML_LOGIN);

        if (fxmlUrl == null) {
            System.err.println("¡ERROR CRÍTICO! No se pudo encontrar el archivo FXML: " + FXML_LOGIN);
            throw new IOException("FXML resource not found: " + FXML_LOGIN);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Muestra la ventana principal. Útil tras un login exitoso.
     * @throws IOException Si el archivo FXML no se encuentra o hay un error de carga.
     */
    public static void mostrarPrincipal() throws IOException {
        if (primaryStage == null) {
            System.err.println("Error: primaryStage no está inicializado. Esto no debería ocurrir.");
            return;
        }

        URL fxmlUrl = MainApp.class.getResource(FXML_PRINCIPAL);

        if (fxmlUrl == null) {
            System.err.println("¡ERROR CRÍTICO! No se pudo encontrar el archivo FXML: " + FXML_PRINCIPAL);
            throw new IOException("FXML resource not found: " + FXML_PRINCIPAL);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Ventana Principal - Colección");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Cierra completamente la aplicación. Necesario para el PrincipalController.
     */
    public static void cerrarAplicacion() {
        if (primaryStage != null) {
            primaryStage.close();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}