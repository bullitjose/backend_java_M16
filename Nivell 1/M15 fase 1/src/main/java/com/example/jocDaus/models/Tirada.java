package com.example.jocDaus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Random;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity // Annotation is required to specify a JPA and Hibernate entity
public class Tirada {
	@Id // Annotation is required to specify the identifier property of the entity
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private boolean guanya;

	private int tirada1;

	private int tirada2;

	@NotNull
	// defines many-to-one relationship between 2 entities
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Jugador jugador;




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isGuanya() {
		return guanya;
	}

	public void setGuanya(boolean guanya) {
		this.guanya = guanya;
	}

	public int getTirada1() {
		return tirada1;
	}

	public void setTirada1(int tirada1) {
		this.tirada1 = tirada1;
	}

	public int getTirada2() {
		return tirada2;
	}

	public void setTirada2(int tirada2) {
		this.tirada2 = tirada2;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	

	/*
	 * Metode per assignar valors aleatoris a les tirades i a la variable setGuanya
	 */
	public void jugada() {

		int tirada1 = 0, tirada2 = 0;

		int suma = 0;

		tirada1 = tiraDau();
		setTirada1(tirada1);
		tirada2 = tiraDau();
		setTirada2(tirada2);
		suma = tirada1 + tirada2;
		

		if (suma == 7) {
			// generarTiradesGuanyades();
			setGuanya(true);

		} else {

			setGuanya(false);
		}

	}

	/**
	 * Metode que retorna el resultat de llenca un dau de 6 cares
	 *
	 * @return number
	 */
	public int tiraDau() {
		Random randValue = new Random();
		return randValue.nextInt(6) + 1;
	}

}
