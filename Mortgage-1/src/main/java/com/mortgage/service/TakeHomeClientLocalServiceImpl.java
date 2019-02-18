package com.mortgage.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.mortgage.domain.TakeHome;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TakeHomeClientLocalServiceImpl  {
	private static final String MIME_TYPE = "application/json";
	private static final String API_BASE_URL = "http://localhost:8080";
	private static final String USER_AGENT = "WebClient";
	private static final Logger logger = LoggerFactory.getLogger(TakeHomeClientLocalServiceImpl.class);

	private final WebClient webClient;

	public TakeHomeClientLocalServiceImpl() {
		this.webClient = WebClient
					.builder()
					.baseUrl(API_BASE_URL)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MIME_TYPE)
					.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
					.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
					.filter(logRequest())
					.build();
	}
	public Mono<Map>  getPropertyByValue(String[] property_ids) {
		Arrays.stream(property_ids).forEach(id -> logger.info("proerty ids:" +id, property_ids)); 
		Mono<Map>  fluxList = webClient
					.get()
					.uri(uriBuilder -> uriBuilder.path("/ServerProperty/value").queryParam("property_ids", String.join(",",property_ids)).build())
					.retrieve()
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("getPropertyByValue 4xx")))
					.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("getPropertyByValue 5xx")))
					.bodyToMono(Map.class);
			return fluxList;
	}
	
	public Flux<TakeHome>  getPropertyDetails(String property_ids) {
		logger.info("getPropertyDetails-id:{} " , property_ids); 
		Flux<TakeHome>  wClient = webClient
					.get()
					.uri(uriBuilder -> uriBuilder.path("/ServerProperty/details").queryParam("property_ids", property_ids).build())
					.retrieve()
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("getPropertyDetails 4xx")))
					.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("getPropertyDetails 5xx")))
					.bodyToFlux(TakeHome.class);
			return wClient;
	}
	
	
	
	
	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			logger.info("Request: method {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}:{}", name, value)));
			return  Mono.just(clientRequest);
		});
	} 
	

}
