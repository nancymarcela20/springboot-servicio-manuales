package com.ac75.springboot.app.manuales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ac75.springboot.app.manuales.domain.Clasificacion;

public interface IClasificacionRepository extends JpaRepository<Clasificacion, Long>{

	@Query("SELECT c FROM Clasificacion c WHERE c.estado = 1")
	List<Clasificacion> findAllActiveClasificacion();
	
}
