package org.example.reto2.dao;

import org.example.reto2.Model.Copia;
import org.example.reto2.Model.Pelicula;
import org.example.reto2.Model.Usuario;
import org.example.reto2.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CopiaDAO {

    public List<Copia> obtenerCopiasPorUsuario(Usuario usuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Copia> query = session.createQuery(
                    "FROM Copia c WHERE c.usuario.id = :userId ORDER BY c.pelicula.titulo", Copia.class);
            query.setParameter("userId", usuario.getId());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void guardarCopia(Copia copia) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(copia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void eliminarCopia(Copia copia) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Verificar que la copia pertenezca al usuario actual antes de eliminar (Integridad)
            if (copia != null && copia.getUsuario().getId() == copia.getUsuario().getId()) {
                session.delete(copia);
                transaction.commit();
            } else {
                if (transaction != null) transaction.rollback();
                System.err.println("Error: No se puede eliminar una copia que no pertenece a este usuario.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Método para la historia de usuario "Añadir nueva película" (solo admin)
    public void guardarPelicula(Pelicula pelicula) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(pelicula);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Método auxiliar para obtener todas las películas para el ComboBox de añadir copia
    public List<Pelicula> obtenerTodasLasPeliculas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pelicula p ORDER BY p.titulo", Pelicula.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
