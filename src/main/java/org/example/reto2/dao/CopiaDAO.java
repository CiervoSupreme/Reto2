package org.example.reto2.dao;

import org.example.reto2.Model.Copia;
import org.example.reto2.Model.Pelicula;
import org.example.reto2.Model.Usuario;
import org.example.reto2.Util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CopiaDAO {

    public List<Copia> obtenerCopiasPorUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Copia> query = em.createQuery(
                    "SELECT c FROM Copia c WHERE c.usuario.id = :userId ORDER BY c.pelicula.titulo", Copia.class);
            query.setParameter("userId", usuario.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    public List<String> obtenerValoresDistintos(String columna, Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        String hql = String.format("SELECT DISTINCT c.%s FROM Copia c WHERE c.usuario.id = :userId ORDER BY c.%s", columna, columna);
        try {
            TypedQuery<String> query = em.createQuery(hql, String.class);
            query.setParameter("userId", usuario.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    public List<Copia> filtrarCopias(Usuario usuario, String estado, String soporte) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder hql = new StringBuilder("SELECT c FROM Copia c WHERE c.usuario.id = :userId");
            if (estado != null) hql.append(" AND c.estado = :estado");
            if (soporte != null) hql.append(" AND c.soporte = :soporte");
            hql.append(" ORDER BY c.pelicula.titulo");

            TypedQuery<Copia> query = em.createQuery(hql.toString(), Copia.class);
            query.setParameter("userId", usuario.getId());
            if (estado != null) query.setParameter("estado", estado);
            if (soporte != null) query.setParameter("soporte", soporte);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    public void guardarCopia(Copia copia) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (copia.getId() == 0) {
                em.persist(copia);
            } else {
                em.merge(copia);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminarCopia(Copia copia) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Copia managedCopia = em.find(Copia.class, copia.getId());
            if (managedCopia != null) {
                 em.remove(managedCopia);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void guardarPelicula(Pelicula pelicula) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(pelicula);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Pelicula> obtenerTodasLasPeliculas() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pelicula p ORDER BY p.titulo", Pelicula.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }
}