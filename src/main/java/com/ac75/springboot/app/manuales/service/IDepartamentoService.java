package com.ac75.springboot.app.manuales.service;

import java.util.List;

import com.ac75.springboot.app.manuales.domain.Clasificacion;
import com.ac75.springboot.app.manuales.domain.Departamento;

public interface IDepartamentoService {

	Departamento save(Departamento departamento) throws Exception;
	
	Departamento edit(Departamento departamento, Long id) throws Exception;
	
	List<Departamento> getAllDepartamentos();
	
	Departamento getDepartamentoById(Long id) throws Exception;
	
	void delete(Long id) throws Exception;
	
	List<Clasificacion> getClasificacionesByIdDepartamento(Long id);
	
	List<Departamento> getAllActiveDepartamentos();
	
}
