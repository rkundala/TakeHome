package com.mortgage.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.mortgage.domain.TakeHome;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TakeHomeClientRemoteServiceImpl implements TakeHomeClientService {
	private static final String MIME_TYPE = "application/json";
	private static final String API_BASE_URL = "https://webservice-takehome-1.herokuapp.com/property";
	private static final String USER_AGENT = "WebClient";
	private static final Logger logger = LoggerFactory.getLogger(TakeHomeClientRemoteServiceImpl.class);

	private final WebClient webClient;

	public TakeHomeClientRemoteServiceImpl() {
		this.webClient = WebClient.builder().baseUrl(API_BASE_URL).defaultHeader(HttpHeaders.CONTENT_TYPE, MIME_TYPE)
				.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT).defaultUriVariables(Collections.singletonMap("url", "http://webservice-takehome-1.spookle.xyz")).filter(logRequest()).build();
	}

	@Override
	public Flux<TakeHome> searchPropertyListByIds(String[] property_id) {
		Arrays.stream(property_id).forEach(id -> logger.info("proerty id:" +id, property_id)); 

		final MultiValueMap<String, String> ids = new LinkedMultiValueMap<>();
		ids.put("property_id", Arrays.asList(property_id));

		return webClient.get().uri(uriBuilder -> uriBuilder.path("").queryParam("property_id", ids).build())
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("searchPropertyListByIds 4xx")))
				.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("searchPropertyListByIds 5xx")))
				.bodyToFlux(TakeHome.class);
	}
	

	@Override
	public Mono<TakeHome> searchPropertyListById(String property_id) {
		logger.info("searchPropertyById(Id) ", property_id);
		 Mono<TakeHome> test = (Mono<TakeHome>) webClient
				.get()
				.uri(uriBuilder -> uriBuilder.path("").queryParam("property_id", property_id).build())
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("searchPropertyListById 4xx")))
				.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("searchPropertyListById 5xx")))
				.bodyToMono(TakeHome.class);
		 return test;
	}
	
	@Override
	public Mono<TakeHome> searchPropertyById() {
		logger.info("Id ");
		return webClient
				.get()
				.uri("https://webservice-takehome-1.herokuapp.com/property?property_id=home_154")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono.error(new Exception("mono error")))
				.bodyToMono(TakeHome.class);
	}
	
	public Mono<Map>  getPropertyByValue(String[] property_ids) {
		Arrays.stream(property_ids).forEach(id -> logger.info("proerty ids:" +id, property_ids)); 
		Mono<Map>  fluxList = webClient
					.get()
					.uri(uriBuilder -> uriBuilder.path("").queryParam("property_ids", String.join(",",property_ids)).build())
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
					.uri(uriBuilder -> uriBuilder.path("").queryParam("property_ids", property_ids).build())
					.retrieve()
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("getPropertyDetails 4xx")))
					.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("getPropertyDetails 5xx")))
					.bodyToFlux(TakeHome.class);
			return wClient;
	}
	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return Mono.just(clientRequest);
		});
	} 
	

}
