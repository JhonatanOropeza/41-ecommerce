package com.ecomerce.control;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecomerce.model.Usuario;
import com.ecomerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

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
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("XXXCCredenciales: {}", usuario);
		
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		//logger.info("XXXUsuario buscado en la bd: {}", user.get());
		
		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
		}else {
			logger.info("XXXEl usuario no existe.");
		}
		return "redirect:/";
	}
}
