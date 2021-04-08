package com.ac75.springboot.app.manuales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ac75.springboot.app.manuales.domain.Departamento;


public interface IDepartamentoRepository extends JpaRepository<Departamento, Long>{

	Departamento findByCodigo(String codigo);
	
	@Query("SELECT d FROM Departamento d WHERE d.estado = 1")
	List<Departamento> findAllActiveDepartamento();
			
}
