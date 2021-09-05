package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity//Annotation is required to specify a JPA and Hibernate entity
public class Jugador {
	 @Id//Annotation is required to specify the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@NotNull
    private String name;
    
    //@NotNull
   	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data_entrada;
    
	//@NotNull
	//Variable amb % encerts
	private double encerts;
    
    

	

	public double getEncerts() {
		return encerts;
	}

	public void setEncerts(double encerts) {
		this.encerts = encerts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getData_entrada() {
		return data_entrada;
	}

	public void setData_entrada() {
		LocalDate dataActual=LocalDate.now();
		this.data_entrada = dataActual;
	}
	public void setData_entrada(LocalDate data_entrada) {
		this.data_entrada = data_entrada;
	}

	

	
    
}

