package com.example.jocDaus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.jocDaus.models.Jugador;
import com.example.jocDaus.models.Tirada;


import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tirades")
public class TiradaController {
	private final TiradaRepository tiradaRepository;
	private final JugadorRepository jugadorRepository;

	@Autowired
	public TiradaController(TiradaRepository tiradaRepository, JugadorRepository jugadorRepository) {
		this.tiradaRepository = tiradaRepository;
		this.jugadorRepository = jugadorRepository;
	}

	// Tirada Create
	// tiradaRepository.save(tirada),create a new tirada
	// Postman: Post-localhost:8080/api/v1/tirades
	@PostMapping
	public ResponseEntity<Tirada> create(@RequestBody @Valid Tirada tirada) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(tirada.getJugador().getId());
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		tirada.setJugador(optionalJugador.get());
		
		
		
		tirada.jugada();

		Tirada savedTirada = tiradaRepository.save(tirada);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedTirada.getId())
				.toUri();

		return ResponseEntity.created(location).body(savedTirada);
	}

	// Tirada Update By id
	// Update a child entity(tirada) when updating an existing
	// parent(jugador) parent
	// via tiradaRepository.save(tirada)
	// Postman: Put-localhost:8080/api/v1/tirades/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Tirada> update(@RequestBody @Valid Tirada tirada, @PathVariable String id) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(tirada.getJugador().getId());
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		Optional<Tirada> optionalTirada = tiradaRepository.findById(id);
		if (!optionalTirada.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		
		
		tirada.setJugador(optionalJugador.get());
		tirada.setId(optionalTirada.get().getId());
		tiradaRepository.save(tirada);

		return ResponseEntity.noContent().build();
	}
	// Delete a tirada by ID
	// tiradaRepository.delete(optionalTirada.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/tirades/{i}

	@DeleteMapping("/{id}")
	public ResponseEntity<Tirada> delete(@PathVariable String id) {
		Optional<Tirada> optionalTirada = tiradaRepository.findById(id);
		if (!optionalTirada.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		tiradaRepository.delete(optionalTirada.get());

		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Page<Tirada>> getAll(Pageable pageable) {
		return ResponseEntity.ok(tiradaRepository.findAll(pageable));
	}

	// Retrieve a tirada via tiradaRepository.findById(id);
	// in the Get Jugador API
	// Postman: Get-localhost:8080/api/v1/tirades/1
	@GetMapping("/{id}")
	public ResponseEntity<Tirada> getById(@PathVariable String id) {
		Optional<Tirada> optionalTirada = tiradaRepository.findById(id);
		if (!optionalTirada.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok(optionalTirada.get());
	}

	// Retrieve a list of child entities(tirada) when retrieving a parent via
	// tiradaRepository.findByJugadorId(jugadorId, pageable));
	// in the Get Jugador API
	// Postman: Get-localhost:8080/api/v1/jugadors/1/
	@GetMapping("/jugador/{jugadorId}")
	public ResponseEntity<Page<Tirada>> getByJugadorId(@PathVariable String jugadorId, Pageable pageable) {
		return ResponseEntity.ok(tiradaRepository.findByJugadorId(jugadorId, pageable));
	}
	
	
	
	
	
}