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
	private static final String MSJ_TIPO_EXTENSION_DE_IMAGEN_NO_VALIDO = "Extensión de imagen no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO = "Extensión de imagen no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_VIDEO_NO_VALIDO = "Extensión de imagen no válida";
	private static final String rutaImagen = "C:/xampp/htdocs/manuales/imagenes";
	private static final String rutaArchivo = "C:/xampp/htdocs/manuales/archivos";
	private static final String rutaVideo = "C:/xampp/htdocs/manuales/videos";
	
	
	@Autowired
	private IManualRepository manualRepository;
	
	@Autowired
	private FileWriter fileWriter;
	
	@Override
	public Manual save(Manual manual, byte[] fileImagen, byte[] fileArchivo, byte[] fileVideo) throws Exception {
			
		String tipo = "";
		
		if(manual.getNombre().isEmpty()) {
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		}
		
		if(manualRepository.findByNombre(manual.getNombre())!=null) {
			throw new Exception(MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE);
		}
		
		String [] extImagen = manual.getUrlImagen().split(".");
		tipo = extImagen[1];
		
		if(tipo.equals("jpg")||tipo.equals("png")||tipo.endsWith("gif")) {
			this.saveArchivo(fileImagen, rutaImagen, manual.getUrlImagen());
			manual.setUrlImagen(rutaImagen+"/"+manual.getUrlImagen());
		}else{
			throw new Exception(MSJ_TIPO_EXTENSION_DE_IMAGEN_NO_VALIDO);
		}
			
		String [] extArchivo = manual.getUrlArchivo().split(".");
		tipo = extArchivo[1];
		
		if(tipo.equals("pdf")||tipo.equals("PDF")){
			this.saveArchivo(fileArchivo, rutaArchivo, manual.getUrlArchivo());
			manual.setUrlArchivo(rutaArchivo+"/"+manual.getUrlArchivo());
		}else {
			throw new Exception(MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO);
		}
		
		String [] extVideo = manual.getUrlArchivo().split(".");
		tipo = extVideo[1];
		
		if(fileVideo!=null&&(tipo.equals("mp4")||tipo.equals("WMV"))) {
			this.saveArchivo(fileArchivo,rutaVideo, manual.getUrlVideo());
			manual.setUrlVideo(rutaVideo+"/"+manual.getUrlVideo());
		}else {
			throw new Exception(MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO);
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
