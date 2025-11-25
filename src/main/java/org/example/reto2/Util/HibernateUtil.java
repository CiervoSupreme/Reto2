package org.example.reto2.Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.example.reto2.Model.Usuario; // Asegúrate de importar todas las clases Model
import org.example.reto2.Model.Pelicula;
import org.example.reto2.Model.Copia;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Construye la SessionFactory cargando la configuración desde hibernate.cfg.xml.
     * Si falla, imprime el error y detiene la aplicación.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.configure("hibernate.cfg.xml");

            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Pelicula.class);
            configuration.addAnnotatedClass(Copia.class);

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
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
     * Cierra la SessionFactory.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}