package com.ac75.springboot.app.manuales.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name= "departamentos")
public class Departamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406291177321559498L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDepartamento;
	
	@Column(name="nombre", nullable = false, length = 100)
	private String nombre;
	
	@Column(name="codigo", unique = true, nullable = false, length = 45)
	private String codigo;
	
	@Column(name="fecharegistro", nullable = false)
	private Date fechaRegistro;
	
	@Column(name="fechaactualiacion", nullable = false)
	private Date fechaActualizacion;
	
	@Column(name = "estado")
	private boolean estado;	
	
	
	@OneToMany(mappedBy = "departamento") 	
	@JsonManagedReference
	private	List<Clasificacion> clasificaciones;
	 

	public Long getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(Long idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	
	public String getNombre() { 
		return nombre; 
	}
	
	public void setNombre(String nombre) { 
		this.nombre = nombre; 
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<Clasificacion> getClasificaciones() {
		return clasificaciones;
	}

	public void setClasificaciones(List<Clasificacion> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}

	
}
