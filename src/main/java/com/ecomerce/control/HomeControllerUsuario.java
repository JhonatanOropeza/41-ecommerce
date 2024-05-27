package com.ecomerce.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeControllerUsuario {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")
	public String homeUsuario(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/homeUsuario";
	}
}
