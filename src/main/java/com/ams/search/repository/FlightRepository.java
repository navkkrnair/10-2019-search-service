package com.ams.search.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.search.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, String>
{
	List<Flight> findByOriginAndDestinationAndFlightDate(String origin, String destination, LocalDate flightDate);

	Flight findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);
}