package com.ecomerce.control;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecomerce.model.Producto;
import com.ecomerce.model.Usuario;
import com.ecomerce.service.ProductoService;
import com.ecomerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {//Para lelvar los datos del backend a la vista
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto producto {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		//Para guardar la imagen
		if (producto.getId()==null) {//Cuando se crea un producto
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			if (file.isEmpty()) {//edición de la imagen sin cambiar la imagen
				Producto p = new Producto();
				p = productoService.get(producto.getId()).get();
				producto.setImagen(p.getImagen());
			}else {//Se cambia la imagen en la edición
				String nombreImagen = upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}
		}
		//Para finalizar el guardado del producto
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	//Método para bscar el producto a modificar y enviarlo a la vista
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		//Obteneindo el producto buscado por id
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		//Confirmación de la busqueda en consola
		LOGGER.info("Producto encontrado:{}", producto);
		//Enviando al información a la plantilla
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	//Método para editar datos del producto 
	@PostMapping("/update")
	public String update(Producto producto) {
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		productoService.delete(id);
		return "redirect:/productos";
	}
}
