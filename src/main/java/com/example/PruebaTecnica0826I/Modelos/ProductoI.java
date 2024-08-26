package com.example.PruebaTecnica0826I.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "productos")
public class ProductoI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombreI;

    private Integer precioI;

    private Integer existenciaI;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaI categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre es requerido") String getNombreI() {
        return nombreI;
    }

    public void setNombreI(@NotBlank(message = "El nombre es requerido") String nombreI) {
        this.nombreI = nombreI;
    }

    public Integer getPrecioI() {
        return precioI;
    }

    public void setPrecioI(Integer precioI) {
        this.precioI = precioI;
    }

    public Integer getExistenciaI() {
        return existenciaI;
    }

    public void setExistenciaI(Integer existenciaI) {
        this.existenciaI = existenciaI;
    }

    public CategoriaI getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaI categoria) {
        this.categoria = categoria;
    }
}
