package com.ecomerce.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.model.Producto;
import com.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdminsitradorControler {
	
	@Autowired
	private  ProductoService productoService;
	
	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
}
