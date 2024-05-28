package com.ecomerce.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeControllerUsuario {
	
	private final Logger log = LoggerFactory.getLogger(HomeControllerUsuario.class);
	
	@Autowired
	private ProductoService productoService;
	
	//Para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//Almacena los datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String homeUsuario(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/homeUsuario";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHomeUsuario(@PathVariable("id") Integer id, Model model) {
		log.info("Id producto enviado como parámetro: {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam("id") Integer id, @RequestParam("cantidad") Integer cantidad, Model model) {
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
		
		detalles.add(detalleOrden);
		
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden); 

		return "usuario/carrito";
	}
	//quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProducto(@PathVariable("id") Integer id, Model model) {
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
		
		return "usuario/carrito";
	}
}
