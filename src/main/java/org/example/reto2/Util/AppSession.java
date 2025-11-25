package org.example.reto2.Util;
import org.example.reto2.Model.Usuario;

// Clase Singleton para mantener el estado de la sesión del usuario
public class AppSession {

    private static Usuario usuarioActual;

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean isAdmin() {
        return usuarioActual != null && usuarioActual.esAdministrador();
    }
}
