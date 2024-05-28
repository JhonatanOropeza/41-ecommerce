package com.ecomerce.service;

import java.util.List;

import com.ecomerce.model.Orden;

public interface IOrdenService {
	List<Orden> findAll();
	Orden save(Orden orden);
	String generarNumeroOrden();
}
