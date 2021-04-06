package com.ac75.springboot.app.manuales.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ac75.springboot.app.manuales.domain.Clasificacion;
import com.ac75.springboot.app.manuales.domain.Departamento;
import com.ac75.springboot.app.manuales.repository.IDepartamentoRepository;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

	private static final String MSJ_EL_NOMBRE_DEL_DEPARTAMENTO_ES_REQUERIDO = "El nombre del departamento es requerido";
	private static final String MSJ_EL_CODIGO_DEL_DEPARTAMENTO_ES_REQUERIDO = "El código del departamento es requerido";	
	private static final String MSJ_EL_DEPARTAMENTO_NO_EXISTE = "El departamento no existe";
	private static final String YA_EXISTE_UN_DEPARTAMENTO_REGISTRADO_CON_ESTE_CODIGO = "Ya existe un departamento creado con este código";
	private static final String EL_DEPARTAMENTO_NO_SE_PUEDE_ELIMINAR = "El departamento no se puede eliminar porque tiene clasificaciones asociadas: "; 
	
	@Autowired
	private IDepartamentoRepository departamentoRepository;
	
	@Override
	@Transactional
	public Departamento save(Departamento departamento) throws Exception {
		if(departamento.getCodigo().isEmpty()) {
			throw new Exception(MSJ_EL_CODIGO_DEL_DEPARTAMENTO_ES_REQUERIDO);
		}
		
		if(departamentoRepository.findByCodigo(departamento.getCodigo())!=null) {
			throw new Exception(YA_EXISTE_UN_DEPARTAMENTO_REGISTRADO_CON_ESTE_CODIGO);
		}
		
		if(departamento.getNombre().isEmpty()) {
			throw new Exception(MSJ_EL_NOMBRE_DEL_DEPARTAMENTO_ES_REQUERIDO);
		}
		
		Date fecha = new Date();
		
		departamento.setFechaRegistro(fecha);
		departamento.setFechaActualizacion(fecha);
		departamento.setEstado(true);		
		
		
		return departamentoRepository.save(departamento);
	}

	@Override
	@Transactional
	public Departamento edit(Departamento departamento, Long id) throws Exception {

		Departamento departamentobd = departamentoRepository.findById(id).get();
		
		if(departamentobd==null)
			throw new Exception(MSJ_EL_DEPARTAMENTO_NO_EXISTE);
		
		if(departamento.getNombre().isEmpty())
			throw new Exception(MSJ_EL_NOMBRE_DEL_DEPARTAMENTO_ES_REQUERIDO);
		
		if(departamento.getCodigo().isEmpty()){
			throw new Exception(MSJ_EL_CODIGO_DEL_DEPARTAMENTO_ES_REQUERIDO);
		}
		
		departamentobd.setFechaActualizacion(new Date());
		departamentobd.setNombre(departamento.getNombre());
		departamentobd.setCodigo(departamento.getCodigo());
		departamentobd.setEstado(departamento.isEstado());
		
		return departamentoRepository.save(departamentobd);
	}

	@Override
	public List<Departamento> getAllDepartamentos() {
		return departamentoRepository.findAll();
	}

	@Override
	public Departamento getDepartamentoById(Long id) throws Exception {
		Departamento departamento = departamentoRepository.findById(id).get();
		if(departamento == null)
			throw new Exception(MSJ_EL_DEPARTAMENTO_NO_EXISTE);
						
		return departamento;
	}

	@Override
	@Transactional
	public void delete(Long id) throws Exception {
		
		Departamento departamento = departamentoRepository.findById(id).get();
		
		if(departamento == null)
			throw new Exception(MSJ_EL_DEPARTAMENTO_NO_EXISTE);
		
		List<Clasificacion> clasificaciones = departamento.getClasificaciones();
		
		if(!clasificaciones.isEmpty()) {
			throw new Exception(EL_DEPARTAMENTO_NO_SE_PUEDE_ELIMINAR
								+listarClasificaciones(clasificaciones).replace(",", " "));
		}
		
		departamentoRepository.deleteById(id);	
	}
	
	private String listarClasificaciones(List<Clasificacion> clasificaciones) {
		
		String datos = "";
		
		for(Clasificacion c: clasificaciones) {
			datos+= c.getNombre()+",";
		}
		
		return datos;
		
	}

	@Override
	public List<Clasificacion> getClasificacionesByIdDepartamento(Long id) {
		
		Departamento departamento = departamentoRepository.findById(id).get();
		
		List<Clasificacion> clasificaciones = departamento.getClasificaciones();
		
		return clasificaciones;
	}

}
