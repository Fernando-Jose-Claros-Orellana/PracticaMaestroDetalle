package com.example.PruebaTecnica0826I.Controladores;

import com.example.PruebaTecnica0826I.Modelos.EtiquetaI;
import com.example.PruebaTecnica0826I.Modelos.ProductoI;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.ICategoriaIService;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.IProductoIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/producto")
public class ProductoIController {
    @Autowired
    private IProductoIService productoIService;

    @Autowired
    private ICategoriaIService categoriaIService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<ProductoI> productos = productoIService.buscarTodosPaginados(pageable);
        model.addAttribute("productos", productos);

        int totalPages = productos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "producto/index";
    }

    @GetMapping("/create")
    public String create(ProductoI producto, Model model){
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        return "producto/create";
    }

    @PostMapping("/addetiquetas")
    public String addEt(ProductoI producto, Model model) {
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        if (producto.getEtiquetas() == null)
            producto.setEtiquetas(new ArrayList<>());
        producto.getEtiquetas().add(new EtiquetaI(producto, ""));

        if (producto.getEtiquetas() != null) {
            Integer idDet = 0;
            for (EtiquetaI item : producto.getEtiquetas()) {
                if (item.getId() == null || item.getId() < 1) {
                    idDet--;
                    item.setId(idDet);
                }
            }
        }

        model.addAttribute(producto);
        if (producto.getId() != null && producto.getId() > 0)
            return "producto/edit";
        else
            return "producto/create";
    }

    @PostMapping("/deletiquetas/{id}")
    public String delEt(@PathVariable("id") Integer id, ProductoI producto, Model model) {
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        producto.getEtiquetas().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute(producto);
        if (producto.getId() != null && producto.getId() > 0)
            return "producto/edit";
        else
            return "producto/create";
    }

    @PostMapping("/save")
    public String save(ProductoI producto, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(producto);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "producto/create";
        }

        if (producto.getEtiquetas() != null) {
            for (EtiquetaI item : producto.getEtiquetas()) {
                if (item.getId() != null && item.getId() < 0)
                    item.setId(null);
                item.setProducto(producto);
            }
        }

        if (producto.getId() != null && producto.getId() > 0) {
            // funcionalidad para cuando es modificar un registro
            ProductoI productoUpdate = productoIService.buscarPorId(producto.getId()).get();
            // almacenar en un dicionario los telefono que estan
            // guardados en la base de datos para mejor acceso a ellos
            Map<Integer, EtiquetaI> etiquetasData = new HashMap<>();
            if (productoUpdate.getEtiquetas() != null) {
                for (EtiquetaI item : productoUpdate.getEtiquetas()) {
                    etiquetasData.put(item.getId(), item);
                }
            }
            // actualizar los registro que viene de la vista hacia el que se encuentra por id
            productoUpdate.setNombreI(producto.getNombreI());
            productoUpdate.setPrecioI(producto.getPrecioI());
            productoUpdate.setExistenciaI(producto.getExistenciaI());
            productoUpdate.setCategoria(producto.getCategoria());
            // recorrer los telefonos obtenidos desde la vista y actualizar
            // alumnoiUpdate para que implemente los cambios
            if (producto.getEtiquetas() != null) {
                for (EtiquetaI item : producto.getEtiquetas()) {
                    if (item.getId() == null) {
                        if (productoUpdate.getEtiquetas() == null)
                            productoUpdate.setEtiquetas(new ArrayList<>());
                        item.setProducto(productoUpdate);
                        productoUpdate.getEtiquetas().add(item);
                    }
                    else{
                        if(etiquetasData.containsKey(item.getId())){
                            EtiquetaI etiqueteUpdate= etiquetasData.get(item.getId());
                            // actualizar las propiedades de TelefonosAlumno
                            // si ya existe en la base de datos
                            etiqueteUpdate.setNombreI(item.getNombreI());
                            // remover del dicionario los telefonos datas para
                            // saber que cuales se van eliminar que serian
                            // todos aquellos que no se lograron remove porque no viene desde
                            // la vista
                            etiquetasData.remove(item.getId());
                        }
                    }
                }
            }
            if(etiquetasData.isEmpty()==false){
                for (Map.Entry<Integer, EtiquetaI> item : etiquetasData.entrySet()) {
                    productoUpdate.getEtiquetas().removeIf(elemento -> elemento.getId() ==item.getKey() );
                }

            }
            producto = productoUpdate;
        }

        productoIService.crearOEditar(producto);
        attributes.addFlashAttribute("msg", "Producto creado correctamente");
        return "redirect:/producto";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        ProductoI producto = productoIService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        return "producto/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        ProductoI producto = productoIService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        return "producto/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        ProductoI producto = productoIService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaIService.obtenerTodos());
        return "producto/delete";
    }

    @PostMapping("/delete")
    public String delete(ProductoI producto, RedirectAttributes attributes){
        productoIService.eliminarPorId(producto.getId());
        attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
        return "redirect:/producto";
    }
}
