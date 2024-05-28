package com.ecomerce.service;

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
	
}
