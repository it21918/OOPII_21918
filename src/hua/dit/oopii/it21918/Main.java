package hua.dit.oopii.it21918;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.UniformInterfaceException;

import gr.hua.dit.oopii.db.OracleDBServiceCRUD;
import gr.hua.dit.oopii.json.Json;
import hua.dit.oopii.exception.CountryException;
import hua.dit.oopii.exception.OutOfBounds;
import hua.dit.oopii.exception.WikipediaNoArcticleException;

public class Main {
	// City
	// calculates which traveler gets the free ticket.
	
	
	public static void calculateFreeTicket(ArrayList<Traveller> travellers, City obj) {
		double max = travellers.get(0).calculateSimilarity(obj);
		int maxPos = 0;
		for (int i = 1; i < travellers.size(); i++) {
			if (max < travellers.get(i).calculateSimilarity(obj) && travellers.get(i).getTimestamp() != 0) {
				max = travellers.get(i).calculateSimilarity(obj);
				maxPos = i;
			}
		}
		System.out.println("The traveller with the VAT number: " + travellers.get(maxPos).getVatNumber()
				+ " gets the free ticket.");
	}

	public static boolean searchCity(String name, HashMap<String, City> mapOfCities) {
		for (String city : mapOfCities.keySet()) {
			if (city.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args)
			throws IOException, WikipediaNoArcticleException, OutOfBounds, CountryException {

		/* Creates cities hard coding.
		OracleDBServiceCRUD dataBase = new OracleDBServiceCRUD();
		City city = new City("Tokyo", "Jp");
		city.retrieveTermVectors(terms);
		city.retrieveLatLon();
		dataBase.addDataToDB(city.getCityName(), city.getCountryName(), city.getGeodesicLat(), city.getGeodesicLon(),
				city.getTermsVector(0), city.getTermsVector(1), city.getTermsVector(2), city.getTermsVector(3),
				city.getTermsVector(4), city.getTermsVector(5), city.getTermsVector(6), city.getTermsVector(7),
				city.getTermsVector(8), city.getTermsVector(9));

         */
		
		// enters cities from database to mapOfCities
		OracleDBServiceCRUD dataBase = new OracleDBServiceCRUD();
		HashMap<String, City> mapOfCities = new HashMap<String, City>();
		try {
			dataBase.ReadData(mapOfCities);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// TRAVELLER
		Json json = new Json();
		ArrayList<Traveller> travellers = new ArrayList<Traveller>();

		try {
			travellers = json.readJSON();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 //System.out.println(travellers.get(0).getCityName());
        
		java.util.Scanner input = new java.util.Scanner(System.in);
		System.out.println("Enter your age.");

		while (true) {
			try {
				int age = input.nextInt();

				if (age >= 16 && age <= 25) {
					Young_traveller young_traveller = new Young_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(young_traveller);

				} else if (age <= 60) {
					Middle_traveller middle_traveller = new Middle_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(middle_traveller);

				} else {
					Elder_traveller elder_traveller = new Elder_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(elder_traveller);

				}
				travellers.get(travellers.size() - 1).setAge(age);
				break;
			} catch (InputMismatchException e) {
				System.out.println("Your age should be an integer. Try again.");
				input.nextLine();
				continue;
			} catch (OutOfBounds ex) {
				System.out.println(ex.getMessage());
				System.out.println("Try to enter your age again.");
				input.nextLine();
				continue;
			} catch (Exception e) {
				System.out.println("Something went wrong. Exiting from program.");
				return;
			}
		}

		// we retrieve the lat lot of the user city and give hard coding some info
		// about him.
		travellers.get(travellers.size() - 1).setVatNumber("AE000056789");
		travellers.get(travellers.size() - 1).setCityName("Athens");
		travellers.get(travellers.size() - 1).setCountryName("Gr");

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				travellers.get(travellers.size() - 1).retrieveLatLon();
				break;
			} catch (CountryException c) {
				System.out.println(c.getMessage());
				System.out.println("Type the correct country's initials ");
				String country = stdin.readLine();
				travellers.get(travellers.size() - 1).setCountryName(country);
				continue;
			} catch (UniformInterfaceException u) {
				if (u.getMessage().contains("404")) {
					System.out.println("Type a correct city name for the country's initials: "
							+ travellers.get(travellers.size() - 1).getCountryName());
					String cityName = stdin.readLine();
					travellers.get(travellers.size() - 1).setCityName(cityName);
					continue;
				} else {
					System.out.println("Something went wrong. Exiting from program");
					return;
				}
			}
		}

		ArrayList<City> choosenCities = new ArrayList<City>();
		ArrayList<String> searchCities = new ArrayList<String>();
		Date date = new Date();

		// The user add to arrayList searchCities the name of the cities the wants to
		// compare.
		searchCities.add("Paris");
		searchCities.add("Athens");
		searchCities.add("Berlin");
		searchCities.add("London");

		for (int i = 0; i < searchCities.size(); i++) {
			if (!searchCity(searchCities.get(i), mapOfCities)) {
				System.out.println("Whats the country of the city " + searchCities.get(i));
				String countryName = input.next();
				City objCity = new City(searchCities.get(i), countryName);
				mapOfCities.put(searchCities.get(i), objCity);

				while (true) {
					try {
						mapOfCities.get(searchCities.get(i)).retrieveLatLon();
						break;
					} catch (CountryException v) {
						System.out.println(v.getMessage());
						System.out.println("Type the correct country's initial for the city "
								+ mapOfCities.get(searchCities.get(i)).getCityName() + ":");
						String country = stdin.readLine();
						mapOfCities.get(searchCities.get(i)).setCountryName(country);
						continue;
					} catch (UniformInterfaceException u) {
						if (u.getMessage().contains("404")) {
							System.out.println("The name of the city "
									+ mapOfCities.get(searchCities.get(i)).getCityName() + " is wrong.");
							System.out.println("Type a correct city name for the country's initial "
									+ mapOfCities.get(searchCities.get(i)).getCountryName() + ":");
							String cityName = stdin.readLine();
							searchCities.set(i, cityName);
							mapOfCities.get(searchCities.get(i)).setCityName(cityName);
							continue;
						} else {
							System.out.println("Something went wrong. Exiting from program");
							return;
						}
					}
				}

				dataBase.addDataToDB(mapOfCities.get(searchCities.get(i)).getCityName(),
						mapOfCities.get(searchCities.get(i)).getCountryName(),
						mapOfCities.get(searchCities.get(i)).getGeodesicLat(),
						mapOfCities.get(searchCities.get(i)).getGeodesicLon(),
						mapOfCities.get(searchCities.get(i)).getTermsVector(0),
						mapOfCities.get(searchCities.get(i)).getTermsVector(1),
						mapOfCities.get(searchCities.get(i)).getTermsVector(2),
						mapOfCities.get(searchCities.get(i)).getTermsVector(3),
						mapOfCities.get(searchCities.get(i)).getTermsVector(4),
						mapOfCities.get(searchCities.get(i)).getTermsVector(5),
						mapOfCities.get(searchCities.get(i)).getTermsVector(6),
						mapOfCities.get(searchCities.get(i)).getTermsVector(7),
						mapOfCities.get(searchCities.get(i)).getTermsVector(8),
						mapOfCities.get(searchCities.get(i)).getTermsVector(9));

			}
			String[] terms = { "arts", "sea", "museums", "restaurant", "stadium", "beach", "hotel", "club", "sidewalks","bar" };
			// System.out.println("Enter 10 terms that you want your city objCity.getCityName()+ " to have");
			while (true) {
				try {
					mapOfCities.get(searchCities.get(i)).removeTermsVector();
					mapOfCities.get(searchCities.get(i)).retrieveTermVectors(terms);
					break;
				} catch (WikipediaNoArcticleException e) {
					System.out.println(e.getMessage());
					System.out.print("Type a correct city name for the country "
							+ mapOfCities.get(searchCities.get(i)).getCountryName() + ":");
					String cityName = stdin.readLine();
					mapOfCities.get(searchCities.get(i)).setCityName(cityName);
					continue;
				} catch (Exception e) {
					System.out.println("Something went wrong. Exiting from program.");
					return;
				}

			}
			travellers.get(travellers.size() -1).setTimestamp(date.getTime());
			choosenCities.add(mapOfCities.get(searchCities.get(i)));
		}

		System.out.println("how many cities that suit do you to see?");
		// for the user traveler we calculate which city/cities are the best for him.
		while (true) {
			try {
				int number = input.nextInt();
				if (number == 1) {
					travellers.get(travellers.size() - 1).setVisit(travellers.get(travellers.size() - 1).compareCities(choosenCities).getCityName());
					System.out.println(travellers.get(travellers.size() -1).compareCities(choosenCities).getCityName());
				} else {
					ArrayList<City> cities = travellers.get(travellers.size() - 1).compareCities(choosenCities, number);
					for (int i = 0; i < cities.size(); i++) {
						System.out.println(cities.get(i).getCityName());
					}
					travellers.get(travellers.size() - 1).setVisit(cities.get(0).getCityName());
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("You can't give a character for input. Try again.");
				input.nextLine();
				continue;
			} catch (OutOfBounds e) {
				System.out.println(e.getMessage());
				input.nextLine();
				continue;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("You can enter an integer between 1 to " + searchCities.size());
				input.nextLine();
				continue;
			}
		}

		Collections.sort(travellers);
		for (int i = 0; i < travellers.size(); i++) {
			if (travellers.get(i).getTimestamp() == 0) {
				System.out.println(travellers.get(i).getTimestamp() + " " + travellers.get(i).getVatNumber()+" "+travellers.get(i).getRatingsOfInterests());
				travellers.remove(i);
			}
		}
		json.writeJSON(travellers);

		// We calculate which traveler will get the free ticket for he city in position
		// calculateFreeTicket(travellers, mapOfCities.get("Athens"));

	}

}
