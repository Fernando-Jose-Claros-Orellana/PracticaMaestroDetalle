package com.example.PruebaTecnica0826I.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class CategoriaI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombreI;

    @OneToMany(mappedBy = "categoria")
    private Set<ProductoI> productos = new HashSet<>();

    public Set<ProductoI> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoI> productos) {
        this.productos = productos;
    }

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
}
