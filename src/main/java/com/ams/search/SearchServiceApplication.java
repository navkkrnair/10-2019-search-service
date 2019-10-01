package com.ams.search;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.ams.search.entity.Fare;
import com.ams.search.entity.Flight;
import com.ams.search.entity.Inventory;
import com.ams.search.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class SearchServiceApplication
{
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceApplication.class);

	private final FlightRepository flightRepository;

	public static void main(String[] args)
	{
		SpringApplication.run(SearchServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init()
	{
		return args ->
		{
			List<Flight> flights = new ArrayList<>();
			flights.add(new Flight("BF100", "SEA", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("100", "USD"), new Inventory(100)));
			flights.add(new Flight("BF101", "NYC", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("101", "USD"), new Inventory(100)));
			flights.add(new Flight("BF105", "NYC", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("105", "USD"), new Inventory(100)));
			flights.add(new Flight("BF106", "NYC", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("106", "USD"), new Inventory(100)));
			flights.add(new Flight("BF102", "CHI", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("102", "USD"), new Inventory(100)));
			flights.add(new Flight("BF103", "HOU", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("103", "USD"), new Inventory(100)));
			flights.add(new Flight("BF104", "LAX", "SFO", LocalDate
				.of(2019, 01, 22), new Fare("104", "USD"), new Inventory(100)));

			flightRepository.saveAll(flights);

			logger.info("Looking to load flights...");
			for (Flight flight : flightRepository
				.findByOriginAndDestinationAndFlightDate("NYC", "SFO", LocalDate.of(2019, 01, 22)))
			{
				logger.info(flight.toString());
			}
		};
	}

}
