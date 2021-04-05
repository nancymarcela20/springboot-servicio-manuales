package com.ac75.springboot.app.manuales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ac75.springboot.app.manuales.domain.Clasificacion;

public interface IClasificacionRepository extends JpaRepository<Clasificacion, Long>{

	
}
