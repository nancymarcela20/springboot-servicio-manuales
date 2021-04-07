package com.ac75.springboot.app.manuales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ac75.springboot.app.manuales.domain.Path;

public interface IPathRepository extends JpaRepository<Path, Long>{

	@Query("SELECT p FROM Path p WHERE p.nombre = ?1")
	Path findPathByNombre(String nombre);
	
}
