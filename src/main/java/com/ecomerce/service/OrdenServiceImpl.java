package com.ecomerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.model.Orden;
import com.ecomerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements  IOrdenService{

	@Autowired
	private IOrdenRepository ordenRepository;
	
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";
		
		//Retornar todas las ordenes para saber cual fue el último número ingresado
		//String 00010 a Integer 10
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero() ) ));
		
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero>10) {//1-9
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if (numero<100) {//10-99
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if (numero<1000) {//100-999
			numeroConcatenado="0000000"+String.valueOf(numero);
		}
		return numeroConcatenado;
	}	
}
