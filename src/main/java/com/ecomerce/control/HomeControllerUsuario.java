package com.ecomerce.control;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.model.Producto;
import com.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeControllerUsuario {
	
	private final Logger log = LoggerFactory.getLogger(HomeControllerUsuario.class);
	
	@Autowired
	private ProductoService productoService;
	
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
}
