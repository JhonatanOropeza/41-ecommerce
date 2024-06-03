package com.ecomerce.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecomerce.model.DetalleOrden;
import com.ecomerce.model.Orden;
import com.ecomerce.model.Producto;
import com.ecomerce.model.Usuario;
import com.ecomerce.service.IDetalleOrdenService;
import com.ecomerce.service.IOrdenService;
import com.ecomerce.service.IUsuarioService;
import com.ecomerce.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeControllerUsuario {
	
	private final Logger log = LoggerFactory.getLogger(HomeControllerUsuario.class);
	
	//Para persistir en la BD: Autowired
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	//Para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//Almacena los datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String homeUsuario(Model model, HttpSession session) {
		log.info("Sesión usuario / : {}", session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findAll());
		
		//Envio de variable de sesión a la vista
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/homeUsuario";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHomeUsuario(@PathVariable("id") Integer id, Model model, HttpSession session) {
		log.info("Id producto enviado como parámetro: {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		//Envio de variable de sesión a la vista
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/productohome";
	}
	
	@PostMapping("cart")
	public String addCart(@RequestParam("id") Integer id, @RequestParam("cantidad") Integer cantidad, Model model, HttpSession session) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProdcuto = productoService.get(id);
		log.info("Productoañadido: {}", optionalProdcuto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProdcuto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		//(B) Validación para que un producto no sea añadido en dos ocasiones.
		Integer idProducto=producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		//Fin de la validación (B)
				
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden); 
		//sesión
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}
	//quitar un producto del carrito
	@GetMapping("delete/cart/{id}")
	public String deleteProducto(@PathVariable("id") Integer id, Model model, HttpSession session) {
		List<DetalleOrden> ordenNueva = new ArrayList<DetalleOrden>();
		for(DetalleOrden detalleOrden: detalles) {
			if (detalleOrden.getProducto().getId()!=id) {
				ordenNueva.add(detalleOrden);
			}
		}
		detalles=ordenNueva;
		
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		//sesión
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/carrito";
	}
	@GetMapping("getCart")
	public String getCart(Model model, HttpSession session) {
		log.info("Sesión usuario /getCart : {}", session.getAttribute("idusuario"));
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		//sesión
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}
	
	@GetMapping("order")
	public String order(Model model, HttpSession session) {
		log.info("---Usuario en order: {}", session.getAttribute("idusuario"));
		Usuario usuario = usuarioService.findById(
				Integer.parseInt(session.getAttribute("idusuario").toString())
				).get();
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		//sesión
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/resumenorden";
	}
	
	@GetMapping("saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		//Usuario
		Usuario usuario = usuarioService.findById(
				Integer.parseInt(session.getAttribute("idusuario").toString())
				).get();

		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//Guardar orden y detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		//Limpiar valores para añadir otros productos al carrito
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/";
	}
	
	@PostMapping("search")
	public String searchProduct(@RequestParam("nombre") String nombre, Model model) {
		log.info("Nombre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		
		model.addAttribute("productos", productos);
		
		return "usuario/homeUsuario";
	}
}
