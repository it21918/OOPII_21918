package it21918;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import exception.CountryException;
import exception.OutOfBounds;
import weather.OpenWeatherMap;

public abstract class Traveller {

	/* This vector contains the ratings of the user from 0-10 for the nouns */
	private Vector<Integer> terms_vector = new Vector<Integer>(10);

	/* This vector contains the latitude and longitude (lat, lon) of his/her City */
	private Vector<Double> geodesic_vector = new Vector<Double>(2);

	private String vatNumber;
	private int age;
	private String cityName;
	private String countryName;
	private static final String appid = "6adefb3191788a7b0ffefbff19ba0448";

	public Traveller() {
		vatNumber = "";
		age = 0;
		cityName = "";
		countryName = "";
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) throws OutOfBounds {
		if (age >= 16 && age <= 115) {
			this.age = age;
		} else
			throw new OutOfBounds(age, "16 to 115");
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;

	}

	/* This method adds an integer from 0 to 10 to the terms_vector */
	public void addRatingOfterms(int term) {
		if (terms_vector.size() >= 10) {
			System.out.println("Out of bounds, you can't enter more than 10 ratings");
			return;
		}
		terms_vector.add(term);
	}

	/* This method removes a rating from the terms_vector in the position pos */
	public void removeRatingOfterms(int pos) {
		if (pos < 0 || pos > 10) {
			System.out.println("Out of bounds, you can't remove that rating");
			return;
		}
		terms_vector.remove(pos);
	}

	/* This method removes all the ratings from the terms_vector */
	public void removeRatingsOfTerms() {
		terms_vector.removeAllElements();
	}

	/* This method returns all the ratings from the terms_vector */
	public Vector<Integer> getRatingsOfInterests() {
		return terms_vector;
	}

	public Integer getRatingsOfInterests(int pos) {
		return terms_vector.get(pos);
	}

	/* This method adds the lat and lon of the City the user is currently */
	public void addLocation(Double lat, Double lon) {
		if (geodesic_vector.isEmpty()) {
			geodesic_vector.add(lat);
			geodesic_vector.add(lon);
			return;
		}
		System.out.println("You have already entered your location.");
	}

	/* This method removes the lat lon from geodesic_vector */
	public void removeLocation() {
		geodesic_vector.removeAllElements();
	}

	/* This method returns the lat lon from the geodesic_vector */
	public Vector<Double> getCoordinates() {
		return geodesic_vector;
	}

	/* This method returns the Lat of the City */
	public Double getCoordinatesLat() {
		return geodesic_vector.get(0);
	}

	/* This method return the Lon of the City */
	public Double getCoordinatesLon() {
		return geodesic_vector.get(1);
	}

	// The method retrieves the lat and lon of the user city.
	public void retrieveLatLon() throws JsonParseException, JsonMappingException, IOException, CountryException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		if (getCountryName().length() == 1)
			throw new CountryException(getCountryName());
		WebResource service = client.resource(UriBuilder.fromUri("http://api.openweathermap.org/data/2.5/weather?q="
				+ getCityName() + "," + getCountryName() + "&APPID=" + appid + "").build());
		ObjectMapper mapper = new ObjectMapper();
		String json = service.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (json.contains('"' + "country" + '"' + ":" + '"' + getCountryName().toLowerCase() + '"')
				|| json.contains('"' + "country" + '"' + ":" + '"' + getCountryName().toUpperCase() + '"')) {
			OpenWeatherMap weather_obj = mapper.readValue(json, OpenWeatherMap.class);
			addLocation(weather_obj.getCoord().getLat(), weather_obj.getCoord().getLon());
		} else {
			throw new CountryException(getCountryName());
		}
	}

	/*
	 * This abstract method calculates the similarity of the obj city according to
	 * user terms_vector
	 */
	abstract double similarity_terms_vector(City obj);

	abstract double calculate_similarity(City obj);

	/*
	 * It consulates the similarity_geodesic_vector between user city and the city
	 * he wants to go
	 */
	public static double similarity_geodesic_vector(double distance) {
		double maxdis = 15317; // Athens-Sydney
		return log2(2 / (2 - distance / maxdis));
	}

	public static double log2(double v) {
		return Math.log(v) / Math.log(2);
	}

	/*
	 * It consulates the distance between two points in Km, using latitude longitude
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;

		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	// this method returns the object city with the max similarity.
	public City compare_cities(ArrayList<City> cityObj) {
		double max = calculate_similarity(cityObj.get(0));
		int maxPosition = 0;
		for (int i = 1; i < cityObj.size(); i++) {
			if (max < calculate_similarity(cityObj.get(i))) {
				max = calculate_similarity(cityObj.get(i));
				maxPosition = i;
			}
		}
		return cityObj.get(maxPosition);
	}

	// this method returns 2 to 5 cities in a raw according to the similarity.
	public ArrayList<City> compare_cities(ArrayList<City> cityObj, int number) throws OutOfBounds {
		String limit = "1 to 5";
		Collections.sort(cityObj, new SortbyMethod());
		ArrayList<City> newList = new ArrayList<City>();
		if (number > 1 && number < 6) {
			for (int i = 0; i < number; i++) {
				newList.add(cityObj.get(cityObj.size() - 1 - i));
			}
		} else
			throw new OutOfBounds(number, limit);

		return newList;
	}

	class SortbyMethod implements Comparator<City> {
		@Override
		public int compare(City a, City b) {
			return new Double(calculate_similarity(a)).compareTo(calculate_similarity(b));
		}
	}

}
