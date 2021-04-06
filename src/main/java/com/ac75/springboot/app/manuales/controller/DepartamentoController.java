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

import com.ac75.springboot.app.manuales.domain.Departamento;
import com.ac75.springboot.app.manuales.service.IDepartamentoService;
import com.google.gson.Gson;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,RequestMethod.PUT })
@RestController
@RequestMapping("/departamento")
public class DepartamentoController {

	private static final String MSJ_DEPARTAMENTO_ACTUALIZADO_CORRECTAMENTE = "Departamento actualizado correctamente";
	private static final String MSJ_DEPARTAMENTO_REGISTRADO_CORRECTAMENTE = "Departamento registrado correctamente";
	private static final String MSJ_SE_HA_ELIMINADO_EL_DEPARTAMENTO_CON_IDENTIFICADOR = "Se ha eliminado el departamento con identificador: ";
	private static final String MSJ_ERROR_DEPARTAMENTO_NO_CREADO = "Error, departamento no creado";
	private static final String MSJ_ERROR_DEPARTAMENTO_NO_EDITADO = "Error, departamento no editado";
	private static final String MSJ_ERROR_CONEXION_PERDIDA = "Error, conexion perdida";
	private static final String MSJ_ERROR_NO_EXISTE_EL_DEPARTAMENTO = "Error, no existe el departamento";
	private static final String MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_EL_DEPARTAMENTO = "Error, no se pudo eliminar el departamento";
	private static final String MSJ_LISTA_DE_DEPARTAMENTOS = "Lista de departamentos";
	private static final String DEPARTAMENTO = "Departamento";
	private static final String MSJ = "msj";
	private static final String STATUS = "status";
	private static final String SUCCESS = "success";
	private static final String DEPARTAMENTOS = "Departamentos";
	
	
	@Autowired
	private IDepartamentoService departamentoService;

	@PostMapping(path = "registrarDepartamento")
	public ResponseEntity<Object> saveDepartamento(@RequestBody Departamento departamento) {
		
		HashMap<String, Object> datos = new HashMap<>();
		Gson gson = new Gson();

		try {
			Departamento newDepartamento = departamentoService.save(departamento);
			datos.put(MSJ, MSJ_DEPARTAMENTO_REGISTRADO_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			datos.put(DEPARTAMENTO, newDepartamento);
			String json = gson.toJson(datos);
			return new ResponseEntity<>(json, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_DEPARTAMENTO_NO_CREADO, e);
		}

	}
	
	@PutMapping(path = "editarDepartamento/{id}")
	public ResponseEntity<Object> editDepartamento(@RequestBody Departamento departamento, @PathVariable Long id){
								
		HashMap<String, Object> datos= new HashMap<>();
				
		try {
			datos.put(DEPARTAMENTO,departamentoService.edit(departamento, id));
			datos.put(MSJ, MSJ_DEPARTAMENTO_ACTUALIZADO_CORRECTAMENTE);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_DEPARTAMENTO_NO_EDITADO, e);			
		}
		
	}
	
	@GetMapping(path = "obtenerDepartamentos")
	public ResponseEntity<Object> getAllDepartamento(HttpServletRequest httpServletRequest){
		
		HashMap<String, Object> datos= new HashMap<>();
		
		try {
			  List<Departamento> departamentos = departamentoService.getAllDepartamentos();
			  datos.put(DEPARTAMENTOS, departamentos); 
			  datos.put(MSJ, MSJ_LISTA_DE_DEPARTAMENTOS); 
			  datos.put(STATUS, SUCCESS); 
			  
			  return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_CONEXION_PERDIDA, e);
		}
		
	}
	
	@GetMapping("getDepartamentoById/{id}")
	public ResponseEntity<Object> getDepartamentoById(@PathVariable Long id){
		
		HashMap<String, Object> datos= new HashMap<>();
		
		try {
			Departamento departamento = departamentoService.getDepartamentoById(id);
			datos.put(DEPARTAMENTO, departamento);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_EXISTE_EL_DEPARTAMENTO, e);
		}
		
	}
	
	@DeleteMapping("eliminarDepartamento/{id}")
	public ResponseEntity<Object> deleteClasificacion(@PathVariable Long id){
		HashMap<String, Object> datos= new HashMap<>();
		try {
			departamentoService.delete(id);
			datos.put(MSJ, MSJ_SE_HA_ELIMINADO_EL_DEPARTAMENTO_CON_IDENTIFICADOR+id);
			datos.put(STATUS, SUCCESS);
			return new ResponseEntity<>(datos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MSJ_ERROR_NO_SE_PUEDE_ELIMINAR_EL_DEPARTAMENTO, e);
		}
				
	}
}
