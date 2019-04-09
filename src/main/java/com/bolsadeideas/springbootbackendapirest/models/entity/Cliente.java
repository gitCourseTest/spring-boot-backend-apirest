package com.bolsadeideas.springbootbackendapirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name="clientes")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotEmpty(message = "no puede estar vacío")
	@Size(min=4, max=20, message="el tamaño tiene que estar entre 4 y 20")
	@Column(nullable=false)
	private String nombre;
	
	@NotEmpty(message = "no puede estar vacío")
	private String apellido;
	
	@NotEmpty(message = "no puede estar vacío")
	@Email(message="no es una direccion de correo válida")
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)//se guarda como tipo date en la base de datos
	private Date createAt;
	
	@PrePersist
	public void prePersist() {
		createAt = new Date();//se agrega la fecha de creación antes de persistir el objeto.
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
