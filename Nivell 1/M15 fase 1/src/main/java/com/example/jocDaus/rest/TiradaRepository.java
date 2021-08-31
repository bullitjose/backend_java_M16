package com.example.jocDaus.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.jocDaus.models.Tirada;

import javax.transaction.Transactional;

//Spring Data JPA provides a collection of repository interfaces that
//help reducing boilerplate code required to implement the data access 
//layer for various databases
public interface TiradaRepository extends JpaRepository<Tirada, Integer> {
	// Metode retorna tirades d'un jugador
	Page<Tirada> findByJugadorId(Integer jugadorId, Pageable pageable);

	//Metode retorna les tirades guanyades d'un jugador
	Page<Tirada> findByJugadorIdAndGuanya(Integer jugadorId, boolean guanya, Pageable pageable);

	// Metode retorna nombre de tirades guanyades d'un jugador
	Integer countFindByJugadorIdAndGuanya(Integer jugadorId, boolean guanya);

	// Metode retorna nombre de tirades d'un jugador
	Integer countFindByJugadorId(Integer jugadorId);

	@Modifying
	@Transactional
	@Query("DELETE FROM Tirada t WHERE t.jugador.id = ?1")
	void deleteByJugadorId(Integer jugadorId);

}