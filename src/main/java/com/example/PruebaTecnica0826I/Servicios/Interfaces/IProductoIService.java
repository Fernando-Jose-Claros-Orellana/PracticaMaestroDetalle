package com.example.PruebaTecnica0826I.Servicios.Interfaces;

import com.example.PruebaTecnica0826I.Modelos.ProductoI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductoIService {
    Page<ProductoI> buscarTodosPaginados(Pageable pageable);

    List<ProductoI> obtenerTodos();

    Optional<ProductoI> buscarPorId(Integer id);

    ProductoI crearOEditar(ProductoI producto);

    void eliminarPorId(Integer id);
}
