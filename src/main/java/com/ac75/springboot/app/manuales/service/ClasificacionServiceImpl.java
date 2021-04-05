package com.ac75.springboot.app.manuales.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ac75.springboot.app.manuales.domain.Clasificacion;
import com.ac75.springboot.app.manuales.repository.IClasificacionRepository;

@Service
public class ClasificacionServiceImpl implements IClasificacionService {

	private static final String MSJ_EL_NOMBRE_DE_LA_CLASIFICACION_ES_REQUERIDO = "El nombre de la clasificación es requerido";
	private static final String MSJ_LA_CLASIFICACION_NO_EXISTE = "La clasificacion no existe";
		
	@Autowired
	private IClasificacionRepository clasificacionRepository;
	
	@Override
	public Clasificacion save(Clasificacion clasificacion) throws Exception {
			
		if(clasificacion.getNombre().isEmpty())
			throw new Exception(MSJ_EL_NOMBRE_DE_LA_CLASIFICACION_ES_REQUERIDO);
				
		Date fecha = new Date();
		
		clasificacion.setFechaRegistro(fecha);
		clasificacion.setFechaActualizacion(fecha);
		clasificacion.setEstado(true);
		
		return clasificacionRepository.save(clasificacion);
	}

	@Override
	public Clasificacion edit(Clasificacion clasificacion, Long id) throws Exception {
		
		Clasificacion clasificacionbd = clasificacionRepository.findById(id).get(); 
		
		if(clasificacionbd==null)
			throw new Exception(MSJ_LA_CLASIFICACION_NO_EXISTE);
		
		if(clasificacion.getNombre().isEmpty())
			throw new Exception(MSJ_EL_NOMBRE_DE_LA_CLASIFICACION_ES_REQUERIDO);
		
		clasificacionbd.setFechaActualizacion(new Date());
		clasificacionbd.setNombre(clasificacion.getNombre());
		clasificacionbd.setDescripcion(clasificacion.getDescripcion());
		clasificacionbd.setEstado(clasificacion.isEstado());			
		return clasificacionRepository.save(clasificacionbd);
	}

	@Override
	public List<Clasificacion> getAllClasificaciones() {
					
		return clasificacionRepository.findAll();
	}

	@Override
	public void delete(Long id) throws Exception {
		
		if(!clasificacionRepository.existsById(id))
			throw new Exception(MSJ_LA_CLASIFICACION_NO_EXISTE);
		
		clasificacionRepository.deleteById(id);			 
	}

	@Override
	public Clasificacion getClasificacionById(Long id) throws Exception {
		
		Clasificacion clasificacion = clasificacionRepository.findById(id).get();
		if(clasificacion == null)
			throw new Exception(MSJ_LA_CLASIFICACION_NO_EXISTE);
						
		return clasificacion;
	}


}
