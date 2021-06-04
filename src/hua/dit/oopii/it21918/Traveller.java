package hua.dit.oopii.it21918;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import hua.dit.oopii.exception.CountryException;
import hua.dit.oopii.exception.OutOfBounds;
import gr.hua.dit.oopii.weather.OpenWeatherMap;

public abstract class Traveller implements  Comparable<Traveller> {

	/* This array contains the ratings of the user from 0-10 for the nouns */
	private int[] termsVector = new int[10];

	/* This array contains the latitude and longitude (lat, lon) of his/her City */
	private double[] geodesicVector = new double[2];
	private String vatNumber;
	private int age;
	private String cityName;
	private String countryName;
	private  long  timestamp;
	private String visit;
	private static final String appid = "6adefb3191788a7b0ffefbff19ba0448";


	public Traveller() {
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
		if(cityName=="") throw new  NullPointerException();
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		if(countryName=="") throw new  NullPointerException();
		this.countryName = countryName;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;

	}

	/* This method adds an integer from 0 to 10 to the terms_vector */
	public void addRatingOfterms(int pos,int term) throws OutOfBounds {
		if(pos >=11 || pos<=-1) {
			throw new OutOfBounds(pos,"0 to 10");
		}else {
			this.termsVector[pos]=term;	
		}
	}


	/* This method returns all the ratings from the terms_vector */
	public int[] getRatingsOfInterests() {
		return termsVector;
	} 

	public int getRatingsOfInterests(int pos) {
		return termsVector[pos];
	}

	/* This method adds the lat and lon of the City the user is currently */
	public void addLocation(Double lat, Double lon) {
		 this.geodesicVector[0]=lat;
		 this.geodesicVector[1]=lon;
	}

	/* This method returns the lat lon from the geodesic_vector */
	public double[] getLocation() {
		return geodesicVector;
	}

	/* This method returns the Lat of the City */
	public Double getCoordinatesLat() {
		return geodesicVector[0];
	}

	/* This method return the Lon of the City */
	public Double getCoordinatesLon() {
		return geodesicVector[1];
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
	abstract double similarityTermsVector(City obj);

	public abstract double calculateSimilarity(City obj);

	/*
	 * It consulates the similarity_geodesic_vector between user city and the city
	 * he wants to go
	 */
	public static double similarityGeodesicVector(double distance) {
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
	public City compareCities(ArrayList<City> cityObj) {
		double max = calculateSimilarity(cityObj.get(0));
		int maxPosition = 0;
		for (int i = 1; i < cityObj.size(); i++) {
			if (max < calculateSimilarity(cityObj.get(i))) {
				max = calculateSimilarity(cityObj.get(i));
				maxPosition = i;
			}
		}
		return cityObj.get(maxPosition);
	}

	// this method returns 2 to 5 cities in a raw according to the similarity.
	public ArrayList<City> compareCities(ArrayList<City> cityObj, int number) throws OutOfBounds {
		String limit = "1 to 5";
		Collections.sort(cityObj, new SortBySimilarity());
		ArrayList<City> newList = new ArrayList<City>();
		if (number > 1 && number < 6) {
			for (int i = 0; i < number; i++) {
				newList.add(cityObj.get(cityObj.size() - 1 - i));
			}
		} else
			throw new OutOfBounds(number, limit);

		return newList;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long l) {
		this.timestamp = l;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	class SortBySimilarity implements Comparator<City> {
		@Override
		public int compare(City a, City b) {
			return new Double(calculateSimilarity(a)).compareTo(calculateSimilarity(b));
		}
	}
	
	//we sort the travelers depending in their timeStamp
	//if they have the same VAT number and terms we change the timeStamp
	//of the newest traveler to 0 .
	public int compareTo(Traveller traveller)
    {
		if(vatNumber.equals(traveller.vatNumber)) {
			if(Arrays.equals(this.termsVector, traveller.termsVector))
			this.timestamp = 0;
		}
        return  (int) (this.timestamp - traveller.timestamp);
    }

	


}


