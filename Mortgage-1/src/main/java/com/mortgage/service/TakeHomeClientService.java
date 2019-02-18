package com.mortgage.service;

import com.mortgage.domain.TakeHome;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TakeHomeClientService {
	public Flux<TakeHome> searchPropertyListByIds(String[] ids);
	public Mono<TakeHome> searchPropertyById();
	public Mono<TakeHome> searchPropertyListById(String id);

}