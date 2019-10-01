package com.ams.search.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ams.search.entity.Flight;
import com.ams.search.entity.Inventory;
import com.ams.search.repository.FlightRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.ams.search.controller.SearchQuery;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchService
{
	private static final Logger    logger = LoggerFactory.getLogger(SearchService.class);
	private final FlightRepository flightRepository;

	@HystrixCommand(fallbackMethod = "searchFallBack", commandProperties =
	{ @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") })
	public List<Flight> search(SearchQuery query)
	{
		logger.info(">> Searching flights in Flight repository");
		List<Flight> flights = flightRepository.findByOriginAndDestinationAndFlightDate(query
			.getOrigin(), query.getDestination(), query.getFlightDate());

		List<Flight> searchResult = new ArrayList<Flight>();
		searchResult.addAll(flights);

		flights.forEach(flight ->
		{
			flight.getFare();
			int inv = flight.getInventory()
				.getCount();
			if (inv < 0)
			{
				searchResult.remove(flight);
			}
		});
		return searchResult;
	}

	public List<Flight> searchFallBack(SearchQuery query)
	{
		logger.info("Searching fallback to return a subset of flights");
		return flightRepository.findAll()
			.subList(0, 4);
	}

	public void updateInventory(String flightNumber, LocalDate flightDate, int inventory)
	{
		logger.info(">> Updating inventory for flight " + flightNumber + " inventory " + inventory);
		Flight    flight = flightRepository
			.findByFlightNumberAndFlightDate(flightNumber, flightDate);
		Inventory inv    = flight.getInventory();
		inv.setCount(inventory);
		flightRepository.save(flight);
	}
}
