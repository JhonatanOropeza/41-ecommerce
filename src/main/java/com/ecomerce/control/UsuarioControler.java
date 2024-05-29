package com.ecomerce.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.model.Usuario;
import com.ecomerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioControler {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioControler.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	// /usuario/registro
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}
}
