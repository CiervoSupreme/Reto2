package org.example.reto2.controller;

import org.example.reto2.MainApp;
import org.example.reto2.dao.UsuarioDAO;
import org.example.reto2.Model.Usuario;
import org.example.reto2.Util.AppSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void initialize() {
        lblMensaje.setText("");
    }

    @FXML
    private void handleLogin() {
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Por favor, introduce usuario y contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(user, pass);

        if (usuario != null) {
            AppSession.setUsuarioActual(usuario);
            try {
                MainApp.mostrarPrincipal();
            } catch (IOException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al cargar la pantalla principal.");
            }
        } else {
            lblMensaje.setText("Credenciales incorrectas.");
        }
    }

    @FXML
    private void handleCerrarAplicacion() {
        MainApp.cerrarAplicacion();
    }
}
