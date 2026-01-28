package org.example.reto2.Model;

import javax.persistence.*;

@Entity
@Table(name = "pelicula")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    private String genero;
    private int ano;
    private String descripcion;
    private String director;

    public Pelicula() {}

    public Pelicula(String titulo, String genero, int ano, String descripcion, String director) {
        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.descripcion = descripcion;
        this.director = director;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
}