package com.ac75.springboot.app.manuales.service;

import java.util.List;

import com.ac75.springboot.app.manuales.domain.Manual;

public interface IManualService {

	Manual save(Manual manual, byte[] fileImagen, String nombreImagen, byte[] fileArchivo, String nombreArchivo, byte[] fileVideo) throws Exception;
	
	Manual edit(Manual manual, Long id, byte[] fileImagen, String nombreImagen, byte[] fileArchivo, String nombreArchivo, byte[] fileVideo) throws Exception;
	
	List<Manual> getAllManuales();
	
	Manual getManualById(Long id) throws Exception;
	
	void delete(Long id) throws Exception; 
	
	List<Manual> getAllActiveManuales();
	
}
