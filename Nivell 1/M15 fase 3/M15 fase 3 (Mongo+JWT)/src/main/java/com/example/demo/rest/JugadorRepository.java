package com.example.demo.rest;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.models.Jugador;

public interface JugadorRepository extends MongoRepository<Jugador, String> {
	// Metode retorna jugador amb millor percentatge d'encerts
	Jugador findTopByOrderByEncertsDesc();

	// Metode retorna jugador amb pitjor percentatge d'encerts
	Jugador findTopByOrderByEncertsAsc();

	// Metode retorna suma encerts del jugador
	@Aggregation(pipeline= {"{$group: {_id:'',total:{$sum:$encerts}}}"})
	public double sumEncerts();

	// Metode retorna nombre de jugadors
	long count();
	
	//
	

}