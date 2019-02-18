package com.mortgage.controller;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mortgage.service.TakeHomeClientLocalServiceImpl;
import com.mortgage.service.TakeHomeClientRemoteServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/property")
public class TakeHomeController {
	private static final Logger logger = LoggerFactory.getLogger(TakeHomeController.class);

	@Autowired
	private TakeHomeClientLocalServiceImpl takeHomeClientLocalServiceImpl;
	
	@Autowired
	private TakeHomeClientRemoteServiceImpl takeHomeClientRemoteServiceImpl;
	
	@GetMapping("/value")
	public Mono<Map> getTakeHomeValue(@RequestParam String[] property_ids) {
		return takeHomeClientRemoteServiceImpl.getPropertyByValue(property_ids);
	}

	@GetMapping("/details5")
	public Flux<String> getTakeHomeDetails5(@RequestParam String[] property_ids) {
		Arrays.stream(property_ids).forEach(id -> logger.info("getTakeHomeDetails5: {} ", id));
		Flux<String> dynamicFlux = Flux.create(sink -> {
			Subscriber.count(sink, property_ids);
		});
		return dynamicFlux;
	}

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
		logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(),
				ex);
		return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
	}
}
