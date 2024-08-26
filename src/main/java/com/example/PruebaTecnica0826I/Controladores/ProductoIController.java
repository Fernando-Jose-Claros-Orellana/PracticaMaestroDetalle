package com.example.PruebaTecnica0826I.Controladores;

import com.example.PruebaTecnica0826I.Modelos.EtiquetaI;
import com.example.PruebaTecnica0826I.Modelos.ProductoI;
import com.example.PruebaTecnica0826I.Servicios.Implementaciones.ImageStorageService;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.ICategoriaIService;
import com.example.PruebaTecnica0826I.Servicios.Interfaces.IProductoIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @Autowired
    private ImageStorageService imageStorageService;

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
    public String save(ProductoI producto, BindingResult result, Model model, MultipartFile file, RedirectAttributes attributes) throws IOException {
        if (result.hasErrors()) {
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
            ProductoI productoUpdate = productoIService.buscarPorId(producto.getId()).get();
            Map<Integer, EtiquetaI> etiquetasData = new HashMap<>();
            if (productoUpdate.getEtiquetas() != null) {
                for (EtiquetaI item : productoUpdate.getEtiquetas()) {
                    etiquetasData.put(item.getId(), item);
                }
            }

            productoUpdate.setNombreI(producto.getNombreI());
            productoUpdate.setPrecioI(producto.getPrecioI());
            productoUpdate.setExistenciaI(producto.getExistenciaI());
            productoUpdate.setCategoria(producto.getCategoria());

            if (producto.getEtiquetas() != null) {
                for (EtiquetaI item : producto.getEtiquetas()) {
                    if (item.getId() == null) {
                        if (productoUpdate.getEtiquetas() == null)
                            productoUpdate.setEtiquetas(new ArrayList<>());
                        item.setProducto(productoUpdate);
                        productoUpdate.getEtiquetas().add(item);
                    } else {
                        if (etiquetasData.containsKey(item.getId())) {
                            EtiquetaI etiqueteUpdate = etiquetasData.get(item.getId());
                            etiqueteUpdate.setNombreI(item.getNombreI());
                            etiquetasData.remove(item.getId());
                        }
                    }
                }
            }
            if (!etiquetasData.isEmpty()) {
                for (Map.Entry<Integer, EtiquetaI> item : etiquetasData.entrySet()) {
                    productoUpdate.getEtiquetas().removeIf(elemento -> elemento.getId() == item.getKey());
                }
            }
            producto = productoUpdate;
        }

        if (file != null && !file.isEmpty()) {
            try {
                UUID uuid = UUID.randomUUID();
                producto.setUrlImagenI(imageStorageService.storeImage(file, uuid.toString()));
            } catch (IOException e) {
                attributes.addFlashAttribute("error", "Error al cargar la imagen.");
                return "producto/create";
            }
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
    public String delete(ProductoI producto, RedirectAttributes attributes) throws IOException{
        if (producto.getUrlImagenI() != null && producto.getUrlImagenI().trim().length() > 0)
            imageStorageService.deleteImage(producto.getUrlImagenI());
        productoIService.eliminarPorId(producto.getId());
        attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
        return "redirect:/producto";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Integer id) {
        try {
            ProductoI producto = productoIService.buscarPorId(id).get();
            Resource resource = imageStorageService.loadImageAsResource(producto.getUrlImagenI());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // o MediaType.IMAGE_PNG según el tipo de imagen
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            // Manejo de error si la imagen no se puede cargar
            return ResponseEntity.notFound().build();
        }
    }
}
