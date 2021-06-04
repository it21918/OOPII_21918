package hua.dit.oopii.it21918;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import gr.hua.dit.oopii.weather.OpenWeatherMap;
import gr.hua.dit.oopii.wikipedia.MediaWiki;
import hua.dit.oopii.exception.CountryException;
import hua.dit.oopii.exception.OutOfBounds;
import hua.dit.oopii.exception.WikipediaNoArcticleException;

public class City {

	/*
	 * The vector terms_vector contains 10 integers that represent how many times
	 * the words cafe, sea, museums, restaurant, stadium, beach, hotel, club,
	 * sidewalks, mountains appeared in the wikipedia article
	 */
	private int[] terms_vector = new int[10];

	/*
	 * geodesic_vector contains 2 double numbers that represent the latitude and
	 * longitude of the City
	 */
	private Double[] geodesic_vector = new Double[2];

	private String cityName;
	private String countryName;
	private static final String appid = "6adefb3191788a7b0ffefbff19ba0448";

	public City() {
		this.cityName = "";
		this.countryName = "";
	}

	/*
	 * This constructor initializes the terms_vector Vector when we make the object
	 * City with the name of the city and country
	 */
	public City(String name, String countryName) {
		this.cityName = name;
		this.countryName = countryName;
	}

	/*
	 * This constructor initializes the terms_vector Vector when we make the object
	 * City with 10 integers
	 */
	public City(String name, String countryName, int term1, int term2, int term3, int term4, int term5, int term6,
			int term7, int term8, int term9, int term10) {
		terms_vector[0]=term1;
		terms_vector[1]=term2;
		terms_vector[3]=term3;
		terms_vector[4]=term4;
		terms_vector[5]=term5;
		terms_vector[6]=term6;
		terms_vector[7]=term7;
		terms_vector[8]=term8;
		terms_vector[9]=term9;
		terms_vector[10]=term10;
		this.cityName = name;
		this.countryName = countryName;
	}

	/*
	 * Does the same as the above constructor plus it adds 2 double numbers to the
	 * vector geodesic_vector
	 */
	public City(int term1, int term2, int term3, int term4, int term5, int term6, int term7, int term8, int term9,
			int term10, double lat, double lon) {
		terms_vector[0]=term1;
		terms_vector[1]=term2;
		terms_vector[3]=term3;
		terms_vector[4]=term4;
		terms_vector[5]=term5;
		terms_vector[6]=term6;
		terms_vector[7]=term7;
		terms_vector[8]=term8;
		terms_vector[9]=term9;
		terms_vector[10]=term10;
		geodesic_vector[0]=lat;
		geodesic_vector[1]=lon;
		this.cityName = "";
		this.countryName = "";
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) throws NullPointerException {		if(countryName=="") throw new  NullPointerException();
	    if(countryName=="") throw new  NullPointerException();
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		if(cityName=="") throw new NullPointerException();
		this.cityName = cityName;
	}

	/* This method adds an integer to the vector terms_vector */
	public void setTermVector(int pos,int term) throws OutOfBounds {
		if(pos>10 || pos<0) throw new OutOfBounds(pos,"0 to 10");
		terms_vector[pos]=term;
	}

	/* This method returns all the elements from the terms_vector */
	public int[] getTermsVector() {
		return terms_vector;
	}

	/*
	 * This method returns a element from the terms_vector which is in position pos
	 */
	public int getTermsVector(int pos) {
		return terms_vector[pos];
	}

	/* This method allows to add two double (lat,lon) in the gedesic_vector */
	public void addGeodesicVector(Double lat, Double lon) {
		geodesic_vector[0]=lat;
		geodesic_vector[1]=lon;
	}

	/* This method returns the lat lon of the vector geodesic_vector */
	public Double[] getGeodesicVector() {
		return geodesic_vector;
	}

	/* This method returns the Lat of the City */
	public Double getGeodesicLat() {
		return geodesic_vector[0];
	}

	/* This method return the Lon of the City */
	public Double getGeodesicLon() {
		return geodesic_vector[1];
	}

	// This method retrieves the lat and lon of the city and adds them to the
	// geodesic vector.
	public void retrieveLatLon() throws JsonParseException, JsonMappingException, IOException, CountryException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		if (getCountryName().length() == 1)
			throw new CountryException(getCountryName());
		WebResource service = client.resource(UriBuilder.fromUri("http://api.openweathermap.org/data/2.5/weather?q="
				+ getCityName() + "," + getCountryName() + "&APPID=" + appid + "").build());
		ObjectMapper mapper = new ObjectMapper();
		String json = service.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (json.contains('"' + "country" + '"' + ":" + getCountryName().toLowerCase() + '"')
				|| json.contains('"' + "country" + '"' + ":" + '"' + getCountryName().toUpperCase() + '"')) {
			OpenWeatherMap weather_obj = mapper.readValue(json, OpenWeatherMap.class);
			addGeodesicVector(weather_obj.getCoord().getLat(), weather_obj.getCoord().getLon());
		} else {
			throw new CountryException(getCountryName());
		}
	}
	
	
	// This method fills all the 10 positions of the terms_vector with how many
	// times every word from the String array appeared from the article.
	public void retrieveTermVectors(String[] word) throws IOException, WikipediaNoArcticleException, OutOfBounds {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client
				.resource(UriBuilder.fromUri("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles="
						+ getCityName() + "&format=json&formatversion=2").build());
		ObjectMapper mapper = new ObjectMapper();
		String json = service.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (json.contains("pageid")) {
			MediaWiki mediaWiki_obj = mapper.readValue(json, MediaWiki.class);
			for (int i = 0; i < 10; i++) {
				setTermVector(i,countCriterionfCity(mediaWiki_obj.getQuery().getPages().get(0).getExtract(), word[i]));
			}
		} else
			throw new WikipediaNoArcticleException(getCityName());
	}
	

	// This method adds to the terms_vector the number of times the word appeared
	// from the article
	public void retrieveTermVectors(String word) throws IOException, WikipediaNoArcticleException, OutOfBounds {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client
				.resource(UriBuilder.fromUri("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles="
						+ getCityName() + "&format=json&formatversion=2").build());
		ObjectMapper mapper = new ObjectMapper();
		String json = service.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (json.contains("pageid")) {
			MediaWiki mediaWiki_obj = mapper.readValue(json, MediaWiki.class);
			for (int i = 0; i < 10; i++) {
				setTermVector(i,countCriterionfCity(mediaWiki_obj.getQuery().getPages().get(0).getExtract(), word));
			}
		} else
			throw new WikipediaNoArcticleException(getCityName());
	}


	/* It counts how many times the string criterion is in the string cityArticle */
	public int countCriterionfCity(String cityArticle, String criterion) {
		cityArticle = cityArticle.toLowerCase();
		int index = cityArticle.indexOf(criterion);
		int count = 0;
		while (index != -1) {
			count++;
			cityArticle = cityArticle.substring(index + 1);
			index = cityArticle.indexOf(criterion);
		}
		return count;
	}




}
