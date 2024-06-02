package com.ecomerce.control;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.model.Orden;
import com.ecomerce.model.Producto;
import com.ecomerce.service.IOrdenService;
import com.ecomerce.service.IUsuarioService;
import com.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorControler {
	
	@Autowired
	private  ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	private Logger logg = LoggerFactory.getLogger(AdministradorControler.class);
	
	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuario(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes_admin";
	}
	
	@GetMapping("/detallesOrden_admin/{id}")
	public String detallesOrden(Model model, @PathVariable("id") Integer id) {
		logg.info("XB Id de la orden: {}", id);
		Orden orden = ordenService.findById(id).get();
		
		model.addAttribute("detalles", orden.getDetalle());
		return "administrador/detalleOrden_admin";
	}
}
