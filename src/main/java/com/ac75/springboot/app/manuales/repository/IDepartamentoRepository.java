package com.ac75.springboot.app.manuales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ac75.springboot.app.manuales.domain.Departamento;


public interface IDepartamentoRepository extends JpaRepository<Departamento, Long>{

	Departamento findByCodigo(String codigo);
}
