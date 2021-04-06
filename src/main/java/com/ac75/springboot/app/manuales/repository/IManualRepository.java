package com.ac75.springboot.app.manuales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ac75.springboot.app.manuales.domain.Manual;

public interface IManualRepository extends JpaRepository<Manual, Long>{

	Manual findByNombre(String nombre);
	
	@Query("SELECT m FROM Manual m WHERE m.estado = 1")
	List<Manual> findAllActiveManuales();
	
}
