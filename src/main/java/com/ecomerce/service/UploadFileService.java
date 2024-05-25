package com.ecomerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	private String folder="images//";
	
	public String saveImage(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			//Pasamos la imagen a bites para poder enviarse
			byte [] bytes =  file.getBytes();
			Path path = Paths.get(folder+file.getOriginalFilename());
			//para escribir en el directorio en el que estamos señalando 
			Files.write(path, bytes);
			return file.getOriginalFilename();
		}
		return "default.jpg";
	}
	
	public void deleteImagen(String nombre) {
		String ruta = "images//";
		File file = new File(ruta+nombre);
		file.delete();
	}
}
