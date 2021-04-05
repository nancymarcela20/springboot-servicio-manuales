package com.ac75.springboot.app.manuales.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.ac75.springboot.app.manuales.domain.Manual;
import com.ac75.springboot.app.manuales.service.IManualService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,RequestMethod.PUT })
@RestController
@RequestMapping("/manual")
public class ManualController {

	private static final String MSJ_ERROR_MANUAL_NO_CREADO = "Error, manual no creado";
	private static final String MSJ_MANUAL_EDITADO_CORRECTAMENTE = "Manual editado correctamente";  
	private static final String MSJ_MANUAL_REGISTRADO_CORRECTAMENTE = "Manual registrado correctamente";
	private static final String MSJ_ERROR_CONEXION_PERDIDA = "Se perdió la conexión";
	private static final String MSJ_ERROR_NO_EXISTE_EL_MANUAL = "No existe el manual";
	private static final String MSJ_ELIMINACION = "Se ha eliminado el manual con el id: ";
	private static final String MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_EL_MANUAL = "No se puede eliminar el manual";
	private static final String MANUAL = "Manual";
	private static final String MSJ = "msj";
	private static final String STATUS = "status";
	private static final String SUCCESS = "success";	
	
	@Autowired
	private IManualService manualService;
	
	
	@PostMapping(path = "registrarManual")
	public ResponseEntity<Object> saveManual(@RequestParam String manual, @RequestParam MultipartFile fileImagen, @RequestParam MultipartFile fileArchivo, @RequestParam MultipartFile fileVideo) throws Exception{
		
		ObjectMapper newMapper= new ObjectMapper();
		Manual objManual=newMapper.readValue(manual, Manual.class);
		
		HashMap<String, Object> datos = new HashMap<>();
		Gson gson = new Gson();
		
		byte[] byteFileVideo=null;
		
		if(!fileVideo.isEmpty()) {
			byteFileVideo=fileVideo.getBytes();
		}
		
		
		try {
			Manual newManual = manualService.save(objManual, fileImagen.getBytes(), fileArchivo.getBytes(), byteFileVideo); 
			datos.put(MSJ, MSJ_MANUAL_REGISTRADO_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			datos.put(MANUAL, newManual);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_MANUAL_NO_CREADO, e);
		}
		
	}
	
	@PutMapping(path = "editarManual/{id}")
	public ResponseEntity<Object> editarManual(@RequestParam String manual, @RequestParam MultipartFile fileImagen, @RequestParam MultipartFile fileArchivo, @RequestParam MultipartFile fileVideo, @PathVariable Long id) throws Exception{
		
		ObjectMapper newMapper= new ObjectMapper();
		Manual objManual=newMapper.readValue(manual, Manual.class);
		
		HashMap<String, Object> datos = new HashMap<>();
		Gson gson = new Gson();
		
		byte[] byteFileVideo=null;
		
		if(!fileVideo.isEmpty()) {
			byteFileVideo=fileVideo.getBytes();
		}
		
		
		try {
			Manual newManual = manualService.edit(objManual, id, fileImagen.getBytes(), fileArchivo.getBytes(), byteFileVideo); 
			datos.put(MSJ, MSJ_MANUAL_EDITADO_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			datos.put(MANUAL, newManual);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_MANUAL_NO_CREADO, e);
		}
		
	}
	
	@GetMapping(path = "obtenerManuales")
	public ResponseEntity<Object> getAllManuales(HttpServletRequest httpServletRequest){
		
		try {
			List<Manual> manuales = manualService.getAllManuales();
			Gson gson = new Gson(); 
			String json = gson.toJson(manuales);
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CONEXION_PERDIDA, e);
		}
		
	}
	
	@GetMapping("getManualById/{id}")
	public ResponseEntity<Manual> getManualById(@PathVariable Long id){
		
		try {
			Manual manual = manualService.getManualById(id);
			return new ResponseEntity<>(manual, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_EXISTE_EL_MANUAL, e);
		}
		
	}
	
	@DeleteMapping("eliminarManual/{id}")
	public ResponseEntity<Object> deleteManual(@PathVariable Long id){
		HashMap<String, Object> datos= new HashMap<>();
		Gson gson = new Gson(); 
		try {
			manualService.delete(id);
			datos.put(MSJ, MSJ_ELIMINACION+id);
			datos.put(STATUS, SUCCESS);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_EL_MANUAL, e);
		}
				
	}
	
	
	
}
