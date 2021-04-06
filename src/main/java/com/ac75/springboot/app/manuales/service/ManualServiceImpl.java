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
import org.springframework.transaction.annotation.Transactional;

import com.ac75.springboot.app.manuales.domain.Manual;
import com.ac75.springboot.app.manuales.repository.IManualRepository;
import com.ac75.springboot.app.manuales.util.FileUtil;

@Service
public class ManualServiceImpl implements IManualService {

	private static final String MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE = "Ya existe un manual con el mismo nombre";
	private static final String MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO = "El nombre del manual es requerido";
	private static final String MSJ_EL_MANUAL_NO_EXISTE = "No existe un manual con el identificado indicado";
	private static final String MSJ_TIPO_EXTENSION_DE_IMAGEN_NO_VALIDO = "Extensión de imagen no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO = "Extensión de archivo no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_VIDEO_NO_VALIDO = "Extensión de video no válida";
	private static final String MSJ_ERROR_NO_EXISTE_EL_ARCHIVO = "Error, no existe el archivo";
	String rutaImagen = "C:/xampp/htdocs/manual/imagenes";
	String rutaArchivo = "C:/xampp/htdocs/manual/archivos";
	String rutaVideo = "C:/xampp/htdocs/manual/videos";
	
	
	@Autowired
	private IManualRepository manualRepository;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Override
	public Manual save(Manual manual, byte[] fileImagen, byte[] fileArchivo, byte[] fileVideo) throws Exception {
							
		if(manual.getNombre().isEmpty()) {
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		}
		
		if(manualRepository.findByNombre(manual.getNombre())!=null) {
			throw new Exception(MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE);
		}
		
		this.saveArchivos(manual, fileImagen, fileArchivo, fileVideo);
		
		
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
	
		
	private void saveArchivos(Manual manual, byte[] fileImagen, byte[] fileArchivo, byte[] fileVideo) throws Exception {
				
		this.validarExtensiones(manual.getUrlImagen(), manual.getUrlArchivo(), manual.getUrlVideo());
		
		this.writeFile(fileImagen, this.rutaImagen, manual.getUrlImagen());
		manual.setUrlImagen(this.rutaImagen+"/"+manual.getUrlImagen());
			
		this.writeFile(fileArchivo, this.rutaArchivo, manual.getUrlArchivo());
		manual.setUrlArchivo(this.rutaArchivo+"/"+manual.getUrlArchivo());
		
		if(fileVideo!=null) {
			this.writeFile(fileVideo,this.rutaVideo, manual.getUrlVideo());
			manual.setUrlVideo(this.rutaVideo+"/"+manual.getUrlVideo());
		}	
		
	}
		
	private void validarExtensiones(String urlImagen, String urlArchivo, String urlVideo) throws Exception {
		
		String tipo="";
		
		String [] extImagen = urlImagen.split("\\.");
		tipo = extImagen[extImagen.length-1];
		
		if(!tipo.equals("jpg")&&!tipo.equals("png")&&!tipo.equals("gif")) 
			throw new Exception(MSJ_TIPO_EXTENSION_DE_IMAGEN_NO_VALIDO);
		
		
		String [] extArchivo = urlArchivo.split("\\.");
		tipo = extArchivo[extArchivo.length-1];
		
		if(!tipo.equals("pdf")&&!tipo.equals("PDF")) 
			throw new Exception(MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO);
		
		if(!urlVideo.isEmpty()) {
			
			String [] extVideo = urlVideo.split("\\.");
			tipo = extVideo[extVideo.length-1];
		
			if(!tipo.equals("mp4")&&!tipo.equals("WMV"))
				throw new Exception(MSJ_TIPO_EXTENSION_DE_VIDEO_NO_VALIDO);
		}	
	}
	
	private void writeFile(byte[] archivo, String ruta, String fileName)  {
		
		Path url = Paths.get(ruta, fileName);		
		
		try {
			fileUtil.write(url, archivo);
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
		
		this.validarAndEliminarArchivo(manualbd.getUrlImagen(), this.rutaImagen+"/"+manual.getUrlImagen());
		
		this.validarAndEliminarArchivo(manualbd.getUrlArchivo(), this.rutaArchivo+"/"+manual.getUrlArchivo());
		
		this.validarAndEliminarArchivo(manualbd.getUrlVideo(), this.rutaVideo+"/"+manual.getUrlVideo());
		
		this.saveArchivos(manual, fileImagen, fileArchivo, fileVideo);
		
		manual.setIdManual(id);
		
		return manualRepository.save(manual);
	}
	
	private void validarAndEliminarArchivo(String urlAnterior, String urlNueva){
		
		boolean sw = false;
		
		if(!urlAnterior.equals(urlNueva)) 
			sw = this.deleteFile(urlAnterior); 
				
		if(!sw)
			throw new RuntimeException(MSJ_ERROR_NO_EXISTE_EL_ARCHIVO);
			
		
	}

	private boolean deleteFile(String ruta) {
		
		Path url = Paths.get(ruta);		
		boolean sw = false;
		
		try {
			sw= fileUtil.delete(url);
		} catch (IOException e) {
			throw new RuntimeException("Error creando archivo");
		}
		
		return sw;
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
