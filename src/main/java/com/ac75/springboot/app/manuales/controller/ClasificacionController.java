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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ac75.springboot.app.manuales.domain.Clasificacion;
import com.ac75.springboot.app.manuales.service.IClasificacionService;
import com.google.gson.Gson;


@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET,RequestMethod.DELETE, RequestMethod.PUT})
@RestController
@RequestMapping("/clasificacion")
public class ClasificacionController {

	private static final String MSJ_CLASIFICACION_ACTUALIZADA_CORRECTAMENTE = "Clasificación actualizada correctamente";
	private static final String MSJ_CLASIFICACION_REGISTRADA_CORRECTAMENTE = "Clasificación registrada correctamente";
	private static final String MSJ_ELIMINACION = "Se ha eliminado la clasificación con identificador: ";
	private static final String MSJ_ERROR_CLASIFICACION_NO_CREADA = "Error, clasificación no creada";
	private static final String MSJ_ERROR_CLASIFICACION_NO_EDITADA = "Error, clasificación no editada";
	private static final String MSJ_ERROR_CONEXION_PERDIDA = "Error, conexión perdida";
	private static final String MSJ_ERROR_NO_EXISTE_CLASIFICACION = "Error, no existe la clasificación";
	private static final String MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_LA_CLASIFICACION = "Error, no se pudo eliminar la clasificación";
	private static final String CLASIFICACION = "Clasificacion";
	private static final String MSJ = "msj";
	private static final String STATUS = "status";
	private static final String SUCCESS = "success";
	
	
	@Autowired
	private IClasificacionService clasificacionService;
	
	
	@PostMapping(path="registrarClasificacion")
	public ResponseEntity<Object> saveClasificacion(@RequestBody Clasificacion clasificacion){
		HashMap<String, Object> datos= new HashMap<>();	
		
		Gson gson = new Gson(); 
		
		try {
			Clasificacion newclasificacion= clasificacionService.save(clasificacion);
			datos.put(MSJ, MSJ_CLASIFICACION_REGISTRADA_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			datos.put(CLASIFICACION, newclasificacion);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CLASIFICACION_NO_CREADA, e);
		}		
		
	}
	
		
	@PutMapping(path = "editarClasificacion/{id}")
	public ResponseEntity<Object> editClasificacion(@RequestBody Clasificacion clasificacion, @PathVariable Long id){
								
		HashMap<String, Object> datos= new HashMap<>();
		Gson gson = new Gson(); 		
		
		try {
			datos.put(CLASIFICACION,clasificacionService.edit(clasificacion, id));
			datos.put(MSJ, MSJ_CLASIFICACION_ACTUALIZADA_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CLASIFICACION_NO_EDITADA, e);			
		}
		
	}
	
	
	@GetMapping(path = "obtenerClasificaciones")
	public ResponseEntity<Object> getAllClasificaciones(HttpServletRequest httpServletRequest){
		
		try {
			List<Clasificacion> clasificaciones = clasificacionService.getAllClasificaciones();
			Gson gson = new Gson(); 
			String json = gson.toJson(clasificaciones);
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CONEXION_PERDIDA, e);
		}
		
	}
	
	
	@GetMapping("getClasificacionById/{id}")
	public ResponseEntity<Clasificacion> getClasificacionById(@PathVariable Long id){
		
		try {
			Clasificacion clasificacion = clasificacionService.getClasificacionById(id);
			return new ResponseEntity<>(clasificacion, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_EXISTE_CLASIFICACION, e);
		}
		
	}
	
	@DeleteMapping("eliminarClasificacion/{id}")
	public ResponseEntity<Object> deleteClasificacion(@PathVariable Long id){
		HashMap<String, Object> datos= new HashMap<>();
		Gson gson = new Gson(); 
		try {
			clasificacionService.delete(id);
			datos.put(MSJ, MSJ_ELIMINACION+id);
			datos.put(STATUS, SUCCESS);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_LA_CLASIFICACION, e);
		}
				
	}
	
}
