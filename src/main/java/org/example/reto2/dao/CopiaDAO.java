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

    public List<String> obtenerValoresDistintos(String columna, Usuario usuario) {
        String hql = String.format("SELECT DISTINCT c.%s FROM Copia c WHERE c.usuario.id = :userId ORDER BY c.%s", columna, columna);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("userId", usuario.getId());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Copia> filtrarCopias(Usuario usuario, String estado, String soporte) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Copia c WHERE c.usuario.id = :userId");
            if (estado != null) hql.append(" AND c.estado = :estado");
            if (soporte != null) hql.append(" AND c.soporte = :soporte");
            hql.append(" ORDER BY c.pelicula.titulo");

            Query<Copia> query = session.createQuery(hql.toString(), Copia.class);
            query.setParameter("userId", usuario.getId());
            if (estado != null) query.setParameter("estado", estado);
            if (soporte != null) query.setParameter("soporte", soporte);

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

    public List<Pelicula> obtenerTodasLasPeliculas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pelicula p ORDER BY p.titulo", Pelicula.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
