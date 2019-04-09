package com.bolsadeideas.springbootbackendapirest.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.DescriptorKey;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springbootbackendapirest.models.entity.Cliente;
import com.bolsadeideas.springbootbackendapirest.models.services.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})//configuracion de cross para permitir el acceso a recursos en distintos dominios
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.findAll();
	}
	
	//Paginador
	@GetMapping("/clientes/page/{page}")//{page} es el numero de pagina que se pasa desde angular
	public Page<Cliente> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);//4 registros por pagina
//		return clienteService.findAll(PageRequest.of(page, 4));
		return clienteService.findAll(pageable);
	}

/*	
	@GetMapping("/clientes/{id}")
//	@RequestMapping(value="/clientes/{id}", method = RequestMethod.GET)
	public Cliente show(@PathVariable Long id) {//el status por defualt es 200.
		return clienteService.findById(id);
	}
*/
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {//Manejo de errores. se usa la clase ResponseEntity<?> generico para devover la respuesta.
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El cliente con ID: "+id.toString()+ " No se encuentra en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
/*	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)//cambiamos el status a 201 (creado)
	public Cliente create(@RequestBody Cliente cliente) {//@RequestBody porque recibido un json en el cuerpo del request.
		return clienteService.save(cliente);
	}
*/
/*	@PostMapping("/clientes")
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {//Se modifica para manejo de errores
		
		Cliente clienteNuevo = null;
		Map<String, Object> response = new HashMap<>();
		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado con exito!!");
		response.put("cliente", clienteNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
*/
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {//Se modifica para manejo validar propiedades
		
		Cliente clienteNuevo = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			/*List<String> errors = new ArrayList<>();
			for (FieldError error : result.getFieldErrors()) {
				errors.add("El campo '"+ error.getField() + "' " + error.getDefaultMessage());
			}
			*/
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(error -> "El campo '"+ error.getField() + "' " + error.getDefaultMessage() )
					.collect(Collectors.toList());
			
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado con exito!!");
		response.put("cliente", clienteNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
/*	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)//cambiamos el status a 201 (creado)
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id ) {
	
		Cliente	clienteActual = clienteService.findById(id);
		
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setEmail(cliente.getEmail());
		
		return clienteService.save(clienteActual);
	}
*/

	
/*	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id ) {
		Cliente clienteActual = null;
		Cliente clienteUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		clienteActual = clienteService.findById(id);
		
		if(clienteActual == null ) {
			response.put("mensaje", "Error. El cliente con el ID= "+id.toString() +" no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado!!");
		response.put("cliente", clienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
*/
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id ) {
		Cliente clienteActual = null;
		Cliente clienteUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		clienteActual = clienteService.findById(id);
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(error -> "El campo '"+ error.getField() + "' "+ error.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		if(clienteActual == null ) {
			response.put("mensaje", "Error. El cliente con el ID= "+id.toString() +" no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado!!");
		response.put("cliente", clienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
/*	
	@DeleteMapping("clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)//cambiamos el status a 204 (no content)
	public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}
*/
	
	@DeleteMapping("clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)//cambiamos el status a 204 (no content)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error. Ocurrio un error al eliminar el cliente. ");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			response.put("mensaje", "Error. Ocurrio un error al eliminar el cliente. ");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Cliente eliminado exitosamente!!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	
	
	public static void main(String[] args) {
/*		int total = 10;
		int contador = 4;
		total -= --contador;
//		total -= contador--;
		System.out.println(contador);
		System.out.println(total);
*/
/*		
		Integer b2 = 128;
		Integer b3 = 128;
		System.out.println(b2 == b3);
*/
/*		
		String s1 = "abc";
		 String s2 = new String("abc");
		 System.out.println("resultado : " + s1 == s2);
*/		 
		 
		}
}
