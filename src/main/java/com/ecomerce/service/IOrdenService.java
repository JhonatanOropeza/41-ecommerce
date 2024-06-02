package com.ecomerce.service;

import java.util.List;
import java.util.Optional;

import com.ecomerce.model.Orden;
import com.ecomerce.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save(Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
}
