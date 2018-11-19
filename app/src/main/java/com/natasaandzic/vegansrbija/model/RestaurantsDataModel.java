package com.natasaandzic.vegansrbija.model;

public class RestaurantsDataModel {

	private String name;
	private String address;
	private String distance;
	private String logo;

	public RestaurantsDataModel() {
	}

	public RestaurantsDataModel(String name, String address, String distance, String logo) {
		this.name = name;
		this.address = address;
		this.distance=distance;
		this.logo=logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
