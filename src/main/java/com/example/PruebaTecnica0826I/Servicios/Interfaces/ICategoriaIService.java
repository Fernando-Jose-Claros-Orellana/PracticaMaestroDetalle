package com.example.PruebaTecnica0826I.Servicios.Interfaces;

import com.example.PruebaTecnica0826I.Modelos.CategoriaI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ICategoriaIService {
    Page<CategoriaI> buscarTodosPaginados(Pageable pageable);

    List<CategoriaI> obtenerTodos();

    Optional<CategoriaI> buscarPorId(Integer id);

    CategoriaI crearOEditar(CategoriaI categoria);

    void eliminarPorId(Integer id);
}
