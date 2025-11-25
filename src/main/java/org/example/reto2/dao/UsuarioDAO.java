package org.example.reto2.dao;

import org.example.reto2.Model.Usuario;
import org.example.reto2.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UsuarioDAO {

    public Usuario autenticar(String nombreUsuario, String contrasena) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.nombreUsuario = :user AND u.contrasena = :pass", Usuario.class);
            query.setParameter("user", nombreUsuario);
            query.setParameter("pass", contrasena);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
