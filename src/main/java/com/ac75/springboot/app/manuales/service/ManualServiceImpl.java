package com.ac75.springboot.app.manuales.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ac75.springboot.app.manuales.domain.Manual;
import com.ac75.springboot.app.manuales.repository.IManualRepository;
import com.ac75.springboot.app.manuales.util.FileWriter;

@Service
public class ManualServiceImpl implements IManualService {

	private static final String MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE = "Ya existe un manual con el mismo nombre";
	private static final String MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO = "El nombre del manual es requerido";
	private static final String MSJ_EL_MANUAL_NO_EXISTE = "No existe un manual con el identificado indicado";
	
	@Autowired
	private IManualRepository manualRepository;
	
	@Autowired
	private FileWriter fileWriter;
	
	@Override
	public Manual save(Manual manual, byte[] fileImagen, byte[] fileArchivo, byte[] fileVideo) throws Exception {
		
		if(manual.getNombre().isEmpty()) {
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		}
		
		if(manualRepository.findByNombre(manual.getNombre())!=null) {
			throw new Exception(MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE);
		}
		
				
		//Se obtienen las url para imagen, archivo y video de la tabla path
		String rutaImagen = "C:/xampp/htdocs/manual/imagenes";
		String rutaArchivo = "C:/xampp/htdocs/manual/archivos";
		String rutaVideo = "C:/xampp/htdocs/manual/videos";
		
		
		this.saveArchivo(fileImagen, rutaImagen, manual.getUrlImagen());
		manual.setUrlImagen(rutaImagen+"/"+manual.getUrlImagen());
		
		this.saveArchivo(fileArchivo, rutaArchivo, manual.getUrlArchivo());
		manual.setUrlArchivo(rutaArchivo+"/"+manual.getUrlArchivo());
		
		if(fileVideo!=null) {
			this.saveArchivo(fileArchivo,rutaVideo, manual.getUrlVideo());
			manual.setUrlVideo(rutaVideo+"/"+manual.getUrlVideo());
		}else {
			manual.setUrlVideo("");
		}
		
		Date fecha = new Date();
		
		manual.setFechaRegistro(fecha);
		manual.setFechaActualizacion(fecha);
		manual.setEstado(true);
		
		try {
			manualRepository.save(manual);
		} catch(JDBCException e) {
			SQLException cause = (SQLException) e.getCause();
			System.out.println(cause.getMessage());
		}
		return manual;
	}
	
	private void saveArchivo(byte[] archivo, String ruta, String fileName)  {
		
		Path url = Paths.get(ruta, fileName);		
		
		try {
			fileWriter.write(url, archivo);
		} catch (IOException e) {
			throw new RuntimeException("Error creando archivo");
		}
		
	}

	@Override
	public Manual edit(Manual manual, Long id, byte[] fileImagen, byte[] fileArchivo, byte[] fileVideo) throws Exception {
		
		Manual manualbd = manualRepository.findById(id).get();
		
		if(manualbd==null)
			throw new Exception(MSJ_EL_MANUAL_NO_EXISTE);
		
		if(manual.getNombre().isEmpty())
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		
		manualbd.setFechaActualizacion(new Date());
		manualbd.setNombre(manual.getNombre());
		manualbd.setDescripcion(manual.getDescripcion());
		manualbd.setEstado(manual.isEstado());		
		
		//Se obtienen las url para imagen, archivo y video de la tabla path
		String rutaImagen = "C:/xampp/htdocs/manual/imagenes";
		String rutaArchivo = "C:/xampp/htdocs/manual/archivos";
		String rutaVideo = "C:/xampp/htdocs/manual/videos";
				
				
		this.saveArchivo(fileImagen, rutaImagen, manual.getUrlImagen());
		manualbd.setUrlImagen(rutaImagen+"/"+manual.getUrlImagen());
				
		this.saveArchivo(fileArchivo, rutaArchivo, manual.getUrlArchivo());
		manualbd.setUrlArchivo(rutaArchivo+"/"+manual.getUrlArchivo());
				
		if(fileVideo!=null) {
			this.saveArchivo(fileArchivo,rutaVideo, manual.getUrlVideo());
			manualbd.setUrlVideo(rutaVideo+"/"+manual.getUrlVideo());
		}else {
			manualbd.setUrlVideo("");
		}
		
		return manualRepository.save(manualbd);
	}

	@Override
	public List<Manual> getAllManuales() {
		return manualRepository.findAll();
	}

	@Override
	public Manual getManualById(Long id) throws Exception {
		Manual manual = manualRepository.findById(id).get();
		if(manual == null)
			throw new Exception(MSJ_EL_MANUAL_NO_EXISTE);
						
		return manual;
	}

	@Override
	public void delete(Long id) throws Exception {
		if(!manualRepository.existsById(id))
			throw new Exception(MSJ_EL_MANUAL_NO_EXISTE);
		
		manualRepository.deleteById(id);	
	}
		

}
