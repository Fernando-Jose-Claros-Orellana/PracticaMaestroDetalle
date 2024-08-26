package com.example.PruebaTecnica0826I.Servicios.Implementaciones;

import com.example.PruebaTecnica0826I.Modelos.CategoriaI;
import com.example.PruebaTecnica0826I.Repositorios.ICategoriaIRepository;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.ICategoriaIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaIService implements ICategoriaIService {
    @Autowired
    private ICategoriaIRepository categoriaIRepositoy;

    @Override
    public Page<CategoriaI> buscarTodosPaginados(Pageable pageable) {
        return categoriaIRepositoy.findAll(pageable);
    }

    @Override
    public List<CategoriaI> obtenerTodos() {
        return categoriaIRepositoy.findAll();
    }

    @Override
    public Optional<CategoriaI> buscarPorId(Integer id) {
        return categoriaIRepositoy.findById(id);
    }
    @Override
    public CategoriaI crearOEditar(CategoriaI categoria) {
        return categoriaIRepositoy.save(categoria);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaIRepositoy.deleteById(id);
    }
}
