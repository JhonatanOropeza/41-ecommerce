package com.ecomerce.service;

import java.util.Optional;

import com.ecomerce.model.Usuario;

public interface IUsuarioService {
	Optional<Usuario> findById(Integer id);
}
