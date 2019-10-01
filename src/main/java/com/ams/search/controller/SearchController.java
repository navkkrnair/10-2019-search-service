package com.ams.search.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ams.search.entity.Flight;
import com.ams.search.exception.SearchError;
import com.ams.search.exception.SearchErrorBuilder;
import com.ams.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RefreshScope
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController
{
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	private final SearchService searchService;

	@Value("${originairports.shutdown}")
	private String shutDown;

	@PostMapping
	public ResponseEntity<?> search(@Valid @RequestBody SearchQuery query, Errors errors)
	{
		if (errors.hasErrors())
		{
			return ResponseEntity.badRequest()
				.body(SearchErrorBuilder.fromBindingErrors(errors));
		}
		if (Arrays.asList(shutDown.split(","))
			.contains(query.getOrigin()))
		{
			return ResponseEntity.badRequest()
				.body(SearchErrorBuilder.create()
					.withError("Origin airport " + query.getOrigin() + " is shutdown")
					.build());
		}
		logger.info(">> Searching for flights : " + query);
		List<Flight> flights = searchService.search(query);
		logger
			.info("Number of Flights for this query: {}", !flights.isEmpty() ? flights.size()
					: "0");
		return !flights.isEmpty() ? ResponseEntity.ok(flights)
				: ResponseEntity.notFound()
					.build();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public SearchError handleException(Exception e)
	{
		logger.info(">> Exception caught {}", e.getClass()
			.getTypeName());
		return SearchErrorBuilder.create()
			.withError(e.getMessage())
			.build();

	}

}
