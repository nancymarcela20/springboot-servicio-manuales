package com.ac75.springboot.app.manuales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ac75.springboot.app.manuales.domain.Manual;

public interface IManualRepository extends JpaRepository<Manual, Long>{

	Manual findByNombre(String nombre);
	
}
