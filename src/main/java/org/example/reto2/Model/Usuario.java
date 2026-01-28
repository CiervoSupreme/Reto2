package org.example.reto2.Model;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "es_admin")
    private boolean esAdministrador;

    public Usuario() {}

    public Usuario(String nombreUsuario, String contrasena, boolean esAdministrador) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.esAdministrador = esAdministrador;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public boolean esAdministrador() { return esAdministrador; }
    public void setEsAdministrador(boolean esAdministrador) { this.esAdministrador = esAdministrador; }
}