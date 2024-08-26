package com.example.PruebaTecnica0826I.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categorias")
public class CategoriaI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombreI;

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
