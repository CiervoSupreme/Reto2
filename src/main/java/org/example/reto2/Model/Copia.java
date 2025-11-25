package org.example.reto2.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "copia")
public class Copia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private String estado; // e.g., "bueno", "dañado"
    private String soporte; // e.g., "DVD", "Blu-ray", "VHS"

    public Copia() {}

    public Copia(Pelicula pelicula, Usuario usuario, String estado, String soporte) {
        this.pelicula = pelicula;
        this.usuario = usuario;
        this.estado = estado;
        this.soporte = soporte;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getSoporte() { return soporte; }
    public void setSoporte(String soporte) { this.soporte = soporte; }

    // Propiedades convenientes para la TableView
    public String getTituloPelicula() {
        return pelicula != null ? pelicula.getTitulo() : "N/A";
    }
}
