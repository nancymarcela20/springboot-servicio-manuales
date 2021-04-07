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
import com.ac75.springboot.app.manuales.util.FileUtil;

@Service
public class ManualServiceImpl implements IManualService {

	private static final String MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE = "Ya existe un manual con el mismo nombre";
	private static final String MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO = "El nombre del manual es requerido";
	private static final String MSJ_EL_MANUAL_NO_EXISTE = "No existe un manual con el identificado indicado";
	private static final String MSJ_TIPO_EXTENSION_DE_IMAGEN_NO_VALIDO = "Extensión de imagen no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_ARCHIVO_NO_VALIDO = "Extensión de archivo no válida";
	private static final String MSJ_TIPO_EXTENSION_DE_VIDEO_NO_VALIDO = "Extensión de video no válida";
	private static final String MSJ_ERROR_LA_IMAGEN_NO_EXISTE_EN_LA_RUTA_INDICADA = "Error, la imagen no existe en la ruta indicada";
	private static final String MSJ_ERROR_EL_ARCHIVO_NO_EXISTE_EN_LA_RUTA_INDICADA = "Error, el archivo no existe en la ruta indicada";
	private static final String MANUALES_IMAGENES = "manuales/imagenes";
	private static final String MANUALES_ARCHIVOS = "manuales/archivos";
	String ruta = "C:/xampp/htdocs/";
	
	
	
	@Autowired
	private IManualRepository manualRepository;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Override
	public Manual save(Manual manual, byte[] fileImagen, String nombreImagen, byte[] fileArchivo, String nombreArchivo, byte[] fileVideo) throws Exception {
							
		if(manual.getNombre().isEmpty()) {
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		}
		
		if(manualRepository.findByNombre(manual.getNombre())!=null) {
			throw new Exception(MSJ_YA_EXISTE_UN_MANUAL_CON_EL_MISMO_NOMBRE);
		}
		
		Date fecha = new Date();
		
		String urlImagen = fecha.getTime()+"-"+nombreImagen;
		manual.setUrlImagen(urlImagen);
		String urlArchivo = fecha.getTime()+"-"+nombreArchivo;
		manual.setUrlArchivo(urlArchivo);
		
		this.saveArchivos(manual, fileImagen, fileArchivo, fileVideo);
		
					
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
		
		manual.setUrlImagen(MANUALES_IMAGENES+"/"+manual.getUrlImagen());
		this.writeFile(fileImagen, this.ruta, manual.getUrlImagen());
		
		manual.setUrlArchivo(MANUALES_ARCHIVOS+"/"+manual.getUrlArchivo());	
		this.writeFile(fileArchivo, this.ruta, manual.getUrlArchivo());
		
		
		if(fileVideo!=null) {
			manual.setUrlVideo("manuales/videos"+"/"+manual.getUrlVideo());
			this.writeFile(fileVideo,this.ruta, manual.getUrlVideo());			
		}	
		
	}
		
	private void validarExtensiones(String urlImagen, String urlArchivo, String urlVideo) throws Exception {
		
		String tipo="";
		
		String [] extImagen = urlImagen.split("\\.");
		tipo = extImagen[extImagen.length-1];
		
		if(!tipo.equals("jpg")&&!tipo.equals("png")&&!tipo.equals("gif")&&!tipo.equals("jpeg")) 
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
	public Manual edit(Manual manual, Long id, byte[] fileImagen, String nombreImagen, byte[] fileArchivo, String nombreArchivo, byte[] fileVideo) throws Exception {
		
		Manual manualbd = manualRepository.findById(id).get();
		boolean sw=false;
		
		if(manualbd==null)
			throw new Exception(MSJ_EL_MANUAL_NO_EXISTE);
		
		if(manual.getNombre().isEmpty())
			throw new Exception(MSJ_EL_NOMBRE_DEL_MANUAL_ES_REQUERIDO);
		
		Date fecha = new Date();
		
		manualbd.setFechaActualizacion(fecha);
		manualbd.setNombre(manual.getNombre());
		manualbd.setDescripcion(manual.getDescripcion());
		manualbd.setEstado(manual.isEstado());	
		manualbd.setClasificacion(manual.getClasificacion());
		
		if(fileImagen!=null) {
			sw = this.deleteFile(this.ruta+manualbd.getUrlImagen());
			if(!sw)
				throw new Exception(MSJ_ERROR_LA_IMAGEN_NO_EXISTE_EN_LA_RUTA_INDICADA);
			nombreImagen = MANUALES_IMAGENES+"/"+fecha.getTime()+"-"+nombreImagen;
			manualbd.setUrlImagen(nombreImagen);
			this.writeFile(fileImagen, this.ruta, nombreImagen);
		}
		
		if(fileArchivo!=null) {
			sw = this.deleteFile(this.ruta+manualbd.getUrlArchivo());
			if(!sw)
				throw new Exception(MSJ_ERROR_EL_ARCHIVO_NO_EXISTE_EN_LA_RUTA_INDICADA);
			nombreArchivo = MANUALES_ARCHIVOS+"/"+fecha.getTime()+"-"+nombreArchivo;
			manualbd.setUrlArchivo(nombreArchivo);
			this.writeFile(fileArchivo, this.ruta, nombreArchivo);
		}
		
		//this.validarAndEliminarArchivo(manualbd.getUrlVideo(), manual.getUrlVideo());
				
		return manualRepository.save(manualbd);
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
		boolean sw = false;
		Manual manual = manualRepository.findById(id).get();
		
		if(manual == null)
			throw new Exception(MSJ_EL_MANUAL_NO_EXISTE);
		
		sw = this.deleteFile(this.ruta+manual.getUrlImagen());
		
		if(!sw)
			throw new Exception(MSJ_ERROR_LA_IMAGEN_NO_EXISTE_EN_LA_RUTA_INDICADA);
		
		sw = this.deleteFile(this.ruta+manual.getUrlArchivo());
		
		if(!sw)
			throw new Exception(MSJ_ERROR_EL_ARCHIVO_NO_EXISTE_EN_LA_RUTA_INDICADA);
		
		manualRepository.deleteById(id);	
	}


	@Override
	public List<Manual> getAllActiveManuales() {
		return manualRepository.findAllActiveManuales();
	}


}
