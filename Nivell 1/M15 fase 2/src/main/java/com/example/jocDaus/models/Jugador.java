package com.example.jocDaus.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "jugadors") // Annotation is required to specify a JPA and Hibernate entity
public class Jugador {
	@Id // Annotation is required to specify the identifier property of the entity
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id=null;

	@Field(name = "name")
	private String name;

	@Field(name = "dataEntrada")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataEntrada;

	@Field(name = "encerts")
	private double encerts;

	public Jugador() {

	}

	public double getEncerts() {
		return encerts;
	}

	public void setEncerts(double encerts) {
		this.encerts = encerts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getData_entrada() {
		return dataEntrada;
	}

	public void setData_entrada() {
		LocalDate dataActual = LocalDate.now();
		this.dataEntrada = dataActual;
	}

	public void setData_entrada(LocalDate data_entrada) {
		this.dataEntrada = data_entrada;
	}

}
