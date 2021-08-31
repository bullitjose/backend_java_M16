package com.example.jocDaus.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.jocDaus.models.Tirada;

import javax.transaction.Transactional;

//Spring Data JPA provides a collection of repository interfaces that
//help reducing boilerplate code required to implement the data access 
//layer for various databases
public interface TiradaRepository extends MongoRepository<Tirada, String> {
	// Metode retorna tirades d'un jugador
	Page<Tirada> findByJugadorId(String jugadorId, Pageable pageable);

	//Metode retorna les tirades guanyades d'un jugador
	Page<Tirada> findByJugadorIdAndGuanya(String jugadorId, boolean guanya, Pageable pageable);

	// Metode retorna nombre de tirades guanyades d'un jugador
	Integer countFindByJugadorIdAndGuanya(String jugadorId, boolean guanya);

	// Metode retorna nombre de tirades d'un jugador
	Integer countFindByJugadorId(String jugadorId);

	@Modifying
	@Transactional
	@Query("DELETE FROM Tirada t WHERE t.jugador.id = ?1")
	void deleteByJugadorId(String string);

}