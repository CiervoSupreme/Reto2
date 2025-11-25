package org.example.reto2.Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.example.reto2.Model.Usuario; // Asegúrate de importar todas las clases Model
import org.example.reto2.Model.Pelicula;
import org.example.reto2.Model.Copia;

public class HibernateUtil {

    // Usamos 'static' para que la SessionFactory se inicialice una única vez.
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Construye la SessionFactory cargando la configuración desde hibernate.cfg.xml.
     * Si falla, imprime la traza de error y detiene la aplicación.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Crea la configuración
            Configuration configuration = new Configuration();

            // Carga el archivo de configuración por defecto (hibernate.cfg.xml)
            configuration.configure("hibernate.cfg.xml");

            // Añadir las clases de entidad (MAI)
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Pelicula.class);
            configuration.addAnnotatedClass(Copia.class);

            // Construye la SessionFactory
            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            // Muestra un error CRÍTICO en la consola si falla la inicialización de la DB
            System.err.println("La inicialización de SessionFactory falló. Verifica hibernate.cfg.xml y tu conexión a la DB.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Método público para acceder a la SessionFactory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory. Se recomienda llamar al salir de la aplicación.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}