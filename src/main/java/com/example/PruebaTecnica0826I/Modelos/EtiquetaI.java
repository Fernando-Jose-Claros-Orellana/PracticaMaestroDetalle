package com.example.PruebaTecnica0826I.Modelos;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "etiquetas")
public class EtiquetaI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombreI;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    private ProductoI producto;

    public EtiquetaI(){

    }

    public EtiquetaI(ProductoI producto, @NotBlank(message = "El nombre es requerido") String nombreI){
        this.nombreI = nombreI;
        this.producto = producto;
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

    @Nonnull
    public ProductoI getProducto() {
        return producto;
    }

    public void setProducto(@Nonnull ProductoI producto) {
        this.producto = producto;
    }
}
