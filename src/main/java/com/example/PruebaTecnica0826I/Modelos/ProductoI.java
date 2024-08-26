package com.example.PruebaTecnica0826I.Modelos;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

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

    @Valid
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtiquetaI> etiquetas;

    public Integer getId() {
        return id;
    }

    public List<EtiquetaI> getEtiquetas(){
        return etiquetas;
    }

    public void setEtiquetas(List<EtiquetaI> etiquetas){
        this.etiquetas = etiquetas;
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
