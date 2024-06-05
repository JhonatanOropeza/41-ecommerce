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
import com.ecomerce.service.IUsuarioService;
import com.ecomerce.service.ProductoService;
import com.ecomerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
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
	public String save(
			Producto producto, @RequestParam("img") MultipartFile file, HttpSession session
			) throws IOException {
		LOGGER.info("--- Info para guardar producto: {}", producto);
		LOGGER.info("--- Datos de la imagen: {}", file);
		//Esta implementación sirvio para prueas antes de tener sessiones
		//Usuario u = new Usuario(1,"","","","","","","");
		//Haciendo uso de la sesión del usuario de manera dinámmica
		Usuario u = usuarioService.findById(
				Integer.parseInt(session.getAttribute("idusuario").toString())
				).get();
		
		producto.setUsuario(u);
		//Para guardar la imagen
		//Validación cuando el producto es cargado por primera vez y nunca habŕa imagen
		if (producto.getId()==null) { // cuando se crea un producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
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
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		if (file.isEmpty()) {//edición de la imagen sin cambiar la imagen
			producto.setImagen(p.getImagen());
		}else {//Se cambia la imagen en la edición
			//1. Eliminación de la imagen
			//1.1 Eliminación de la imagen cuando este por defecto (default.jpg)
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImagen(p.getImagen());
			}
			//2. Se coloca la nueva imagen
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		//1. Eliminación de la imagen
		LOGGER.info("--- 1.- Id del usuario: {}", id);
		Producto p = new Producto();
		p = productoService.get(id).get();
		LOGGER.info("--- 2.- Producto a eliminar: {}", p);
		//1.1 Eliminación de la imagen cuando este por defecto (default.jpg)
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImagen(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";
	}
}
