package com.ac75.springboot.app.manuales.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="clasificaciones")
public class Clasificacion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1279221258518622609L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idClasificacion;
	
	@Column(name="nombre", nullable = false, length = 100)
	private String nombre;
	
	@Column(name="descripcion", length = 200)	
	private String descripcion;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;
	
	@Column(name="fechaactualiacion")
	private Date fechaActualizacion;
	
	@Column(name = "estado")
	private boolean estado;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clasificacion")
	private List<Manual> manuales;
	
	@ManyToOne(cascade = {CascadeType.DETACH})
	@JoinColumn(name = "FK_DEPARTAMENTO", nullable = false, updatable = false)	
	@JsonBackReference
	private Departamento departamento;

	public Long getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(Long idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public List<Manual> getManuales() {
		return manuales;
	}

	public void setManuales(List<Manual> manuales) {
		this.manuales = manuales;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	
	
}
