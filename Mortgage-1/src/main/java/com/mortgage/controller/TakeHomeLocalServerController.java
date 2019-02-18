package com.mortgage.controller;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mortgage.domain.Address;
import com.mortgage.domain.TakeHome;

@RestController
@RequestMapping("/ServerProperty")
public class TakeHomeLocalServerController {
	private static final Logger logger = LoggerFactory.getLogger(TakeHomeLocalServerController.class);
	

	List<TakeHome> dataSource2 = Arrays.asList(
			new TakeHome("home_482", "AAA Gould", "305372", new Address("A Ronaldborough", "VA")),
			new TakeHome("home_d50", "BBB Gould", "305373", new Address("B Ronaldborough", "VA")),
			new TakeHome("home_1c9", "CCC Gould", "305374", new Address("C Ronaldborough", "CA")),
			new TakeHome("home_1cd", "DDD Gould", "305375", new Address("C Ronaldborough", "VA")),
			new TakeHome("home_7d1", "EEE Gould", "305376", new Address("D Ronaldborough", "VA")),
			new TakeHome("home_554", "FFF Gould", "305377", new Address("E Ronaldborough", "VA")),
			new TakeHome("home_c8d", "GGG Gould", "305377", new Address("F Ronaldborough", "VA")),
			new TakeHome("home_f5e", "HHH Gould", "305379", new Address("G Ronaldborough", "VA")),
			new TakeHome("home_ae4", "III Gould", "305380", new Address("H Ronaldborough", "VA")),
			new TakeHome("home_c42", "JJJ Gould", "305381", new Address("I Ronaldborough", "VA")));
	@GetMapping("/value")
	public Map<String, List<? extends Object>> getPropertyByValue(@RequestParam String[] property_ids) {
		Arrays.stream(property_ids).forEach(id -> logger.info("getPropertyByValue -> proerty id:" +id, property_ids)); 
		List<String> propertyFound = Arrays.asList(property_ids)
				.stream()
				.filter(id -> dataSource2.stream().anyMatch(th -> th.getHomeid().equals(id)))
				.collect(Collectors.toList());
		
		List<String> propertyNotFoundList = Arrays.asList(property_ids)
				.stream()
				.filter(id -> !dataSource2.stream().anyMatch(th -> th.getHomeid().equals(id)))
				.collect(Collectors.toList());
		
		List<TakeHome> propertyDetailList =dataSource2
									.stream()
									.filter(data -> propertyFound.contains(data.getHomeid()))
									.collect(Collectors.toList());
		TakeHome highestValueVA = propertyDetailList
									.stream()
									.filter(id -> id.getAddress().getState().equals("VA"))
									.max(Comparator.comparing(TakeHome::getValue))
									.orElse(null);

		Map<String, List<? extends Object>> consolidatedList = new HashMap<>();
		consolidatedList.put("Highest Value", Arrays.asList(highestValueVA));
		if(!CollectionUtils.isEmpty(propertyNotFoundList)) {
			consolidatedList.put("Property NotFound", propertyNotFoundList);
		}
		logger.info("takeHome count : {} ",propertyDetailList.size());
		propertyDetailList.forEach(id -> logger.info("takeHome found {}", id)); 
		return consolidatedList;
	}

	@GetMapping("details")
	public TakeHome getPropertyDetials(@RequestParam String[] property_ids) {
		Arrays.stream(property_ids).forEach(id -> logger.info("getPropertyDetials -> {}" , property_ids)); 
		List<String> propertyFound = Arrays.asList(property_ids);
		TakeHome propertyDetails =dataSource2
				.stream()
				.filter(data -> propertyFound.contains(data.getHomeid()))
				.findFirst().orElse(new TakeHome());
		return propertyDetails;
	}

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
		logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(),
				ex);
		return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
	}
}
