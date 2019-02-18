package com.mortgage.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TakeHome {

	@JsonProperty("home_id")
	private String homeid;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("value")
	private String value;
	@JsonProperty("address")
	private Address address;

	public TakeHome() {
	}

	public TakeHome(String homeid, String owner, String value, Address address) {
		super();
		this.homeid = homeid;
		this.owner = owner;
		this.value = value;
		this.address = address;
	}

	public String getHomeid() {
		return homeid;
	}

	public void setHomeid(String homeid) {
		this.homeid = homeid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((homeid == null) ? 0 : homeid.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TakeHome [homeid=" + homeid + ", owner=" + owner + ", value=" + value + ", address=" + address + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TakeHome other = (TakeHome) obj;
		if (homeid == null) {
			if (other.homeid != null)
				return false;
		} else if (!homeid.equals(other.homeid))
			return false;
		return true;
	}

}
