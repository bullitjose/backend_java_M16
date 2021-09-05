package com.example.demo.rest;

//Spring Data JPA provides a collection of repository interfaces that
//help reducing boilerplate code required to implement the data access 
//layer for various databases
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {
	// Metode retorna jugador amb millor percentatge d'encerts
	Jugador findTopByOrderByEncertsDesc();

	// Metode retorna jugador amb pitjor percentatge d'encerts
	Jugador findTopByOrderByEncertsAsc();
	
	
	//Metode retorna suma encerts del jugador
	@Query(value = "SELECT sum(encerts) FROM Jugador")
    public double sumEncerts();

	// Metode retorna nombre de jugadors
	long count();


}