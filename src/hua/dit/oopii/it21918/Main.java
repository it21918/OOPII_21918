package hua.dit.oopii.it21918;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import com.sun.jersey.api.client.UniformInterfaceException;
import hua.dit.oopii.exception.CountryException;
import hua.dit.oopii.exception.OutOfBounds;
import hua.dit.oopii.exception.WikipediaNoArcticleException;

public class Main {
	// City
	// calculates which traveler gets the free ticket.
	public static void 	calculateFreeTicket(ArrayList<Traveller> travellers, City obj) {
		double max = travellers.get(0).calculateSimilarity(obj);
		int maxPos = 0;
		for (int i = 1; i < travellers.size(); i++) {
			if (max < travellers.get(i).calculateSimilarity(obj)) {
				max = travellers.get(i).calculateSimilarity(obj);
				maxPos = i;
			}
		}
		System.out.println("The traveller with the VAT number: " + travellers.get(maxPos).getVatNumber()
				+ " gets the free ticket.");
	}

	public static void main(String[] args)
			throws IOException, WikipediaNoArcticleException, OutOfBounds, CountryException {

		// Creates 5 cities hard coding.
		ArrayList<City> city = new ArrayList<>();
		City athens = new City("Athens", "GR");
		City rome = new City("Rome", "it");
		City london = new City("London", "gb");
		City berlin = new City("Berlin", "de");
		City vienna = new City("vienna", "at");

		city.add(athens);
		city.add(rome);
		city.add(london);
		city.add(berlin);
		city.add(vienna);

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		int errorPos = 0;

		// Retrieves the lat and lot of every city from the arrayList while it checks if
		// the country and city name are correct.
		while (true) {
			try {
				for (int i = errorPos; i < city.size(); i++) {
					errorPos = i;
					city.get(i).retrieveLatLon();
				}

				break;
			} catch (CountryException v) {
				System.out.println(v.getMessage());
				System.out
						.println("Type a correct country name for the city " + city.get(errorPos).getCityName() + ":");
				String country = stdin.readLine();
				city.get(errorPos).setCountryName(country);
				continue;
			} catch (UniformInterfaceException u) {
				if (u.getMessage().contains("404")) {
					System.out.println("The name of the city " + city.get(errorPos).getCityName() + " is wrong.");
					System.out.println(
							"Type a correct city name for the country " + city.get(errorPos).getCountryName() + ":");
					String cityName = stdin.readLine();
					city.get(errorPos).setCityName(cityName);
					continue;
				} else {
					System.out.println("Something went wrong. Exiting from program");
					return;
				}
			}
		}

		// Calculates how many times the terms appeared to the article with the title
		// the name of each city while it checks if the article name exists.
		errorPos = 0;
		String[] terms = { "cafe", "sea", "museums", "restaurant", "stadium", "beach", "hotel", "club", "sidewalks",
				"bar" };

		while (true) {
			try {
				for (int i = errorPos; i < city.size(); i++) {
					errorPos = i;
					city.get(i).retrieveTermVectors(terms);
				}
				break;

			} catch (WikipediaNoArcticleException e) {
				System.out.println(e.getMessage());
				System.out
						.print("Type a correct city name for the country " + city.get(errorPos).getCountryName() + ":");
				String cityName = stdin.readLine();
				city.get(errorPos).setCityName(cityName);
				continue;
			} catch (Exception e) {
				System.out.println("Something went wrong. Exiting from program.");
				return;
			}

		}

		// Traveler
		ArrayList<Traveller> travellers = new ArrayList<>();
		java.util.Scanner input = new java.util.Scanner(System.in);

		// depending on the age we create the corresponding object and we add it to the
		// list of travelers while we check if the age input is correct.
		System.out.println("Enter your age");
		int[] age = new int[1];

		while (true) {
			try {
				age[0] = input.nextInt();

				if (age[0] >= 16 && age[0] <= 25) {
					Young_traveller young_traveller = new Young_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(young_traveller);

				} else if (age[0] <= 60) {
					Middle_traveller middle_traveller = new Middle_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(middle_traveller);

				} else {
					Elder_traveller elder_traveller = new Elder_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
					travellers.add(elder_traveller);

				}
				travellers.get(0).setAge(age[0]);
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

		// we retrieve the lat lot of the user city and give hard coding some infos
		// about him.
		travellers.get(0).setVatNumber("AE88888888");
		travellers.get(0).setCityName("Mykolaiv");
		travellers.get(0).setCountryName("ua");
		while (true) {
			try {
				travellers.get(0).retrieveLatLon();
				break;
			} catch (CountryException c) {
				System.out.println(c.getMessage());
				System.out.println("Type a correct country name ");
				String country = stdin.readLine();
				travellers.get(0).setCountryName(country);
				continue;
			} catch (UniformInterfaceException u) {
				if (u.getMessage().contains("404")) {
					System.out
							.println("Type a correct city name for the country: " + travellers.get(0).getCountryName());
					String cityName = stdin.readLine();
					travellers.get(0).setCityName(cityName);
					continue;
				} else {
					System.out.println("Something went wrong. Exiting from program");
					return;
				}
			}
		}

		// adds hard core some travelers.
		Young_traveller young_traveller2 = new Young_traveller(0, 1, 2, 3, 0, 1, 2, 1, 1, 0);
		young_traveller2.setVatNumber("AM13456789");
		young_traveller2.setAge(17);
		young_traveller2.setCityName("Athens");
		young_traveller2.setCountryName("gr");
		young_traveller2.retrieveLatLon();
		Middle_traveller middle_traveller2 = new Middle_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
		middle_traveller2.setVatNumber("AM1234563");
		middle_traveller2.setAge(30);
		middle_traveller2.setCityName("Madrid");
		middle_traveller2.setCountryName("es");
		middle_traveller2.retrieveLatLon();
		Elder_traveller elder_traveller2 = new Elder_traveller(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
		elder_traveller2.setVatNumber("AM1111111");
		elder_traveller2.setAge(90);
		elder_traveller2.setCityName("Paris");
		elder_traveller2.setCountryName("fr");
		elder_traveller2.retrieveLatLon();

		travellers.add(elder_traveller2);
		travellers.add(middle_traveller2);
		travellers.add(young_traveller2);

		ArrayList<City> cities = new ArrayList<>();
		System.out.println("how many cities that suit do you to see?");

		// for the user traveler we calculate which city/cities are the best for him.
		while (true) {
			try {
				int number = input.nextInt();
				if (number == 1) {
					System.out.println(travellers.get(0).compareCities(city).getCityName());
				} else {
					cities = travellers.get(0).compareCities(city, number);
					for (int i = 0; i < cities.size(); i++) {
						System.out.println(cities.get(i).getCityName());
					}
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
				System.out.println("You can enter an integer between 1 to " + city.size());
				input.nextLine();
				continue;
			} catch (Exception e) {
				System.out.println("Something went wrong. Exiting from program.");
				return;
			}
		}

		// for(int i=0 ; i<city.size(); i++)
		// System.out.println(travellers.get(0).calculate_similarity(city.get(i))+ " "+
		//city.get(i).getCityName());

		// We calculate which traveler will get the free ticket for he city in position
		// 0 (Athens)
		calculateFreeTicket(travellers, city.get(0));

	}

}
