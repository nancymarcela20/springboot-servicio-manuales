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


@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET,RequestMethod.DELETE, RequestMethod.PUT})
@RestController
@RequestMapping("/clasificacion")
public class ClasificacionController {

	private static final String MSJ_CLASIFICACION_ACTUALIZADA_CORRECTAMENTE = "Clasificación actualizada correctamente";
	private static final String MSJ_CLASIFICACION_REGISTRADA_CORRECTAMENTE = "Clasificación registrada correctamente";
	private static final String MSJ_ELIMINACION = "Se ha eliminado la clasificación con identificador: ";
	private static final String MSJ_ERROR_CONEXION_PERDIDA = "Error, conexión perdida";
	private static final String CLASIFICACION = "Clasificacion";
	private static final String CLASIFICACIONES = "Clasificaciones";
	private static final String LISTA_DE_CLASIFICACIONES = "Lista de clasificaciones";
	private static final String MSJ = "msj";
	private static final String STATUS = "status";
	private static final String SUCCESS = "success";
	
	
	@Autowired
	private IClasificacionService clasificacionService;
	
	
	@PostMapping(path="registrarClasificacion")
	public ResponseEntity<Object> saveClasificacion(@RequestBody Clasificacion clasificacion){
		HashMap<String, Object> datos= new HashMap<>();	
		
		
		try {
			Clasificacion newclasificacion= clasificacionService.save(clasificacion);
			datos.put(MSJ, MSJ_CLASIFICACION_REGISTRADA_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			datos.put(CLASIFICACION, newclasificacion);
			return new ResponseEntity<>(datos, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}		
		
	}
	
		
	@PutMapping(path = "editarClasificacion/{id}")
	public ResponseEntity<Object> editClasificacion(@RequestBody Clasificacion clasificacion, @PathVariable Long id){
								
		HashMap<String, Object> datos= new HashMap<>();	
		
		try {
			datos.put(CLASIFICACION,clasificacionService.edit(clasificacion, id));
			datos.put(MSJ, MSJ_CLASIFICACION_ACTUALIZADA_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);			
		}
		
	}
	
	
	@GetMapping(path = "obtenerClasificaciones")
	public ResponseEntity<Object> getAllClasificaciones(HttpServletRequest httpServletRequest){
		
		HashMap<String, Object> datos= new HashMap<>();	
		
		try {
			List<Clasificacion> clasificaciones = clasificacionService.getAllClasificaciones();
			datos.put(CLASIFICACIONES, clasificaciones);
			datos.put(STATUS, SUCCESS);
			datos.put(MSJ, LISTA_DE_CLASIFICACIONES);
			return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CONEXION_PERDIDA, e);
		}
		
	}
	
	
	@GetMapping("getClasificacionById/{id}")
	public ResponseEntity<Object> getClasificacionById(@PathVariable Long id){
		HashMap<String, Object> datos= new HashMap<>();
		try {
			Clasificacion clasificacion = clasificacionService.getClasificacionById(id);
			datos.put(CLASIFICACION, clasificacion);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
		
	}
	
	@DeleteMapping("eliminarClasificacion/{id}")
	public ResponseEntity<Object> deleteClasificacion(@PathVariable Long id){
		HashMap<String, Object> datos= new HashMap<>();
		try {
			clasificacionService.delete(id);
			datos.put(MSJ, MSJ_ELIMINACION+id);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
				
	}
	
	@GetMapping("listarClasificacionesActivas")
	public ResponseEntity<Object> getAllActiveClasificaciones(){
		return new ResponseEntity<>(clasificacionService.getAllActiveClasificacion(), HttpStatus.OK);
	}
	
}
