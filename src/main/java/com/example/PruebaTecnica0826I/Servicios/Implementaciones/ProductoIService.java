package com.example.PruebaTecnica0826I.Servicios.Implementaciones;

import com.example.PruebaTecnica0826I.Modelos.ProductoI;
import com.example.PruebaTecnica0826I.Repositorios.IProductoIRepository;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.IProductoIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoIService implements IProductoIService {
    @Autowired
    private IProductoIRepository productoIRepository;

    @Override
    public Page<ProductoI> buscarTodosPaginados(Pageable pageable) {
        return productoIRepository.findAll(pageable);
    }

    @Override
    public List<ProductoI> obtenerTodos() {
        return productoIRepository.findAll();
    }

    @Override
    public Optional<ProductoI> buscarPorId(Integer id) {
        return productoIRepository.findById(id);
    }
    @Override
    public ProductoI crearOEditar(ProductoI producto) {
        return productoIRepository.save(producto);
    }

    @Override
    public void eliminarPorId(Integer id) {
        productoIRepository.deleteById(id);
    }
}
