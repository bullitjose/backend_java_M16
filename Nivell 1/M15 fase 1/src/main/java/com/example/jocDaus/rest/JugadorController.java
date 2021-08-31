package com.example.jocDaus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.jocDaus.models.Tirada;
import com.example.jocDaus.models.Jugador;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class JugadorController {
	private final JugadorRepository jugadorRepository;
	private final TiradaRepository tiradaRepository;

	@Autowired
	public JugadorController(JugadorRepository jugadorRepository, TiradaRepository tiradaRepository) {
		this.jugadorRepository = jugadorRepository;
		this.tiradaRepository = tiradaRepository;
	}
	// ----------------------------------------------
	// Crear un Jugador
	// ----------------------------------------------
	// jugadorRepository.save,create a list of new child entities(tirada) when
	// creating a new parent(library)
	// Postman: Post-localhost:8080/api/v1/POST/players

	@PostMapping("/POST/players")
	public ResponseEntity<Jugador> create(@Valid @RequestBody Jugador jugador) {
		jugador.setData_entrada();
		Jugador savedJugador = jugadorRepository.save(jugador);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedJugador.getId()).toUri();

		return ResponseEntity.created(location).body(savedJugador);
	}
	// ----------------------------------------------
	// Modifica el nom del jugador
	// ----------------------------------------------
	// Create a list of new child entities(tirada) when updating an existing
	// parent(library) parent
	// via jugadorRepository.save(library);
	// Postman: Put-localhost:8080/api/v1/PUT/players/1/

	@PutMapping("/PUT/players/{id}")
	public ResponseEntity<Jugador> update(@PathVariable Integer id, @Valid @RequestBody Jugador jugadorModificat) {
		/*
		 * Optional.A container object which may or may not contain a non-null value.If
		 * a value is present, isPresent() returns true. If novalue is present, the
		 * object is considered empty and isPresent() returns false.
		 */
		Optional<Jugador> optionalJugador = jugadorRepository.findById(id);
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		Jugador jugador = optionalJugador.get();
		jugador.setName(jugadorModificat.getName());
		jugadorRepository.save(jugador);

		return ResponseEntity.noContent().build();
	}

	// ----------------------------------------------
	// Un jugador especific realitza una tirada de daus
	// ----------------------------------------------
	// tiradaRepository.save(tirada),create a new tirada
	// Postman: Post-localhost:8080/api/v1/POST/players/{jugadorId}/games
	@PostMapping("/POST/players/{jugadorId}/games")
	public ResponseEntity<Tirada> create(@RequestBody @Valid Tirada tirada, @PathVariable Integer jugadorId) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(jugadorId);
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		tirada.setJugador(optionalJugador.get());
		// Cridar metode per generar les dues tirades de daus
		tirada.jugada();

		Tirada savedTirada = tiradaRepository.save(tirada);

		Jugador jugador = tirada.getJugador();

		// Actualitzar tirades amb encerts del jugador
		int partidesGuanyades = tiradaRepository.countFindByJugadorIdAndGuanya(jugadorId, true);

		// Actualitzar tirades del jugador
		int partides = tiradaRepository.countFindByJugadorId(jugadorId);

		// Actualitzar percentatge d'encerts
		jugador.setEncerts((partidesGuanyades / partides) * 100);
		jugadorRepository.save(jugador);
		//

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedTirada.getId()).toUri();

		return ResponseEntity.created(location).body(savedTirada);
	}

	// Retrieve a list of child entities(tirada) when retrieving a parent via
	// jugadorRepository.findById(id)
	// in the Get Jugador API
	// Postman: Get-localhost:8080/api/v1/GET/players/1/

	@GetMapping("/GET/players/{id}")
	public ResponseEntity<Jugador> getById(@PathVariable Integer id) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(id);
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok(optionalJugador.get());
	}
	// ----------------------------------------------
	// Llistar jugadors del sistema amb el seu percentatge mig d'exits
	// ----------------------------------------------
	// Postman: Get- localhost:8080/api/v1/GET/players/

	@GetMapping("/GET/players/")
	public ResponseEntity<Page<Jugador>> getAll(Pageable pageable) {

		actualitzarEncerts();

		return ResponseEntity.ok(jugadorRepository.findAll(pageable));
	}

	// Metode per actualitzar variable encerts
	public void actualitzarEncerts() {
		int i = 0;
		// Actualitzar variable pencerts que guarda % d'encerts de les tirades
		for (Jugador jugador : jugadorRepository.findAll()) {
			int partidesGuanyades = 0;
			int partidesRealitzades = 0;
			i = jugador.getId();
			double encerts = 0;

			partidesGuanyades = tiradaRepository.countFindByJugadorIdAndGuanya(i, true);
			partidesRealitzades = tiradaRepository.countFindByJugadorId(i);

			if (partidesRealitzades > 0) {

				encerts = partidesGuanyades * 100;
				encerts = Math.round(encerts / partidesRealitzades);
				jugador.setEncerts(encerts);

			} else {

				jugador.setEncerts(0.0);

			}

			jugadorRepository.save(jugador);
		}
	}

	// ----------------------------------------------
	// Eliminar les tirades d'un jugador
	// ----------------------------------------------
	// Delete a library by ID and all tirades belong to it
	// via jugadorRepository.delete(optionalJugador.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/DELETE/players/1/games

	@DeleteMapping("/DELETE/players/{id}/games")
	public ResponseEntity<Jugador> eliminarTirades(@PathVariable Integer id) {

		Optional<Jugador> optionalJugador = jugadorRepository.findById(id);
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		tiradaRepository.deleteByJugadorId(id);
		return delete(id);
	}

	// Delete a library by ID and all tirades belong to it
	// via jugadorRepository.delete(optionalJugador.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/DELETE/PLAYERSjugadors/1/games

	@DeleteMapping("/{id}")
	public ResponseEntity<Jugador> delete(@PathVariable Integer id) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(id);
		if (!optionalJugador.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		deleteJugadorInTransaction(optionalJugador.get());

		return ResponseEntity.noContent().build();
	}

	@Transactional
	public void deleteJugadorInTransaction(Jugador jugador) {
		tiradaRepository.deleteByJugadorId(jugador.getId());
		
	}

	// ----------------------------------------------
	// Ranking mig encerts de tots el jugadors del sistema
	// ----------------------------------------------
	// Postman: Get- localhost:8080/api/v1/GET/players/ranking

	@GetMapping("/GET/players/ranking")
	public ResponseEntity<Double> getRanking() {
		actualitzarEncerts();
		// Calcular suma de tots percentatges d'encerts dels jugadors del repository
		double sumaEncerts = jugadorRepository.sumEncerts();
		// Calcular nombre de jugadors del repository
		long sumaJugadors = jugadorRepository.count();

		return ResponseEntity.ok(sumaEncerts / sumaJugadors);

	}

	// ----------------------------------------------
	// Retorna llistat de jugades d'un jugador
	// ----------------------------------------------
	// Retrieve a list of child entities(tirada) when retrieving a parent via
	// tiradaRepository.findByJugador(jugadorId, pageable));
	// in the Get Jugador API
	// Postman: Get-localhost:8080/api/v1/GET/players/{jugadorId}/games
	@GetMapping("/GET/players/{jugadorId}/games")
	public ResponseEntity<Page<Tirada>> getByJugadorId(@PathVariable Integer jugadorId, Pageable pageable) {
		return ResponseEntity.ok(tiradaRepository.findByJugadorId(jugadorId, pageable));
	}

	// ----------------------------------------------
	// Retorna llistat de jugades guanyades d'un jugador
	// ----------------------------------------------
	// Retrieve a list of child entities(tirada) when retrieving a parent via
	// tiradaRepository.findByJugador(jugadorId, pageable));
	// in the Get Jugador API
	// Postman: Get-localhost:8080/api/v1/GET/players/{jugadorId}/encertsgames
	@GetMapping("/GET/players/{jugadorId}/encertsgames")
	public ResponseEntity<Page<Tirada>> getencertssByJugadorId(@PathVariable Integer jugadorId, Pageable pageable) {

		return ResponseEntity.ok(tiradaRepository.findByJugadorIdAndGuanya(jugadorId, true, pageable));
	}

	// ----------------------------------------------
	// Ranking jugadors amb millor % encerts
	// ----------------------------------------------
	// Postman: Get- localhost:8080/api/v1/GET/players/ranking/winner

	@GetMapping("/GET/players/ranking/winner")
	public ResponseEntity<Jugador> getWinner(Pageable pageable) {
		actualitzarEncerts();

		return ResponseEntity.ok(jugadorRepository.findTopByOrderByEncertsDesc());
	}
	// ----------------------------------------------
	// Ranking jugadors amb pitjor % encerts
	// ----------------------------------------------
	// Postman: Get- localhost:8080/api/v1/GET/players/ranking/winner

	@GetMapping("/GET/players/ranking/loser")
	public ResponseEntity<Jugador> getLooser(Pageable pageable) {
		actualitzarEncerts();

		return ResponseEntity.ok(jugadorRepository.findTopByOrderByEncertsAsc());
	}

}