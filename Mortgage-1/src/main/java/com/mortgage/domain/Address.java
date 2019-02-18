package com.mortgage.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;

	public Address() {
	}

	public Address(String city, String state) {
		super();
		this.city = city;
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Address [city=" + city + ", state=" + state + "]";
	}
	
	

}
