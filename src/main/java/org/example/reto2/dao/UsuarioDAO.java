package org.example.reto2.dao;

import org.example.reto2.Model.Usuario;
import org.example.reto2.Util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UsuarioDAO {

    public Usuario autenticar(String nombreUsuario, String contrasena) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Lógica para asegurar que admin/1234 siempre funcione
            if ("admin".equals(nombreUsuario) && "1234".equals(contrasena)) {
                try {
                    TypedQuery<Usuario> query = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nombreUsuario = :user", Usuario.class);
                    query.setParameter("user", "admin");
                    return query.getSingleResult();
                } catch (NoResultException e) {
                    // Si no existe, lo creamos
                    em.getTransaction().begin();
                    Usuario admin = new Usuario("admin", "1234", true);
                    em.persist(admin);
                    em.getTransaction().commit();
                    return admin;
                }
            }

            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombreUsuario = :user AND u.contrasena = :pass", Usuario.class);
            query.setParameter("user", nombreUsuario);
            query.setParameter("pass", contrasena);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
