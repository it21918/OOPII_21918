
package hua.dit.oopii.gui;

import java.awt.Choice;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.sun.jersey.api.client.UniformInterfaceException;

import gr.hua.dit.oopii.db.OracleDBServiceCRUD;
import gr.hua.dit.oopii.json.Json;
import hua.dit.oopii.exception.CountryException;
import hua.dit.oopii.exception.OutOfBounds;
import hua.dit.oopii.exception.WikipediaNoArcticleException;
import hua.dit.oopii.gui.Gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hua.dit.oopii.it21918.City;
import hua.dit.oopii.it21918.Elder_traveller;
import hua.dit.oopii.it21918.Middle_traveller;
import hua.dit.oopii.it21918.Traveller;
import hua.dit.oopii.it21918.Young_traveller;
import hua.dit.oopii.streams.RecommendedCity;

public class Gui implements ActionListener, Runnable {
	private Thread t;
	private JButton submit;
	private JButton show_travellers;
	private JTextField vatNumber;
	private JTextField age;
	private JTextField cityName;
	private JTextField countryInitials;
	private Choice term1;
	private Choice term2;
	private Choice term3;
	private Choice term4;
	private Choice term5;
	private Choice term6;
	private Choice term7;
	private Choice term8;
	private Choice term9;
	private Choice term10;
	private JTextField visitCity;
	private JTextField visitCountry;
	private JTextField show;
	private JButton filtering;

	private ArrayList<Traveller> travellers;
	private HashMap<String, City> mapOfCities;
	private ArrayList<City> choosenCities;
	private ArrayList<String> searchCities;

	public Gui() {

		JFrame frame = new JFrame("Travelling agency");

		JLabel label = new JLabel("Put your personal info.");
		label.setForeground(Color.blue);
		label.setBounds(0, 0, 200, 25);
		frame.add(label);

		String[] inputs = new String[16];

		vatNumber = new JTextField("20");
		vatNumber.setBounds(140, 30, 165, 25);
		frame.add(vatNumber);

		age = new JTextField("20");
		age.setBounds(140, 60, 165, 25);
		frame.add(age);

		cityName = new JTextField("athens");
		cityName.setBounds(140, 90, 165, 25);
		frame.add(cityName);

		countryInitials = new JTextField("gr");
		countryInitials.setBounds(140, 120, 165, 25);
		frame.add(countryInitials);

		term1 = new Choice();
		term1.setBounds(140, 200, 165, 25);
		term1.add("0");
		term1.add("1");
		term1.add("2");
		term1.add("3");
		term1.add("4");
		term1.add("5");
		term1.add("6");
		term1.add("7");
		term1.add("8");
		term1.add("9");
		frame.add(term1);

		term2 = new Choice();
		term2.setBounds(140, 230, 165, 25);
		term2.add("0");
		term2.add("1");
		term2.add("2");
		term2.add("3");
		term2.add("4");
		term2.add("5");
		term2.add("6");
		term2.add("7");
		term2.add("8");
		term2.add("9");
		frame.add(term2);

		term3 = new Choice();
		term3.setBounds(140, 260, 165, 25);
		term3.add("0");
		term3.add("1");
		term3.add("2");
		term3.add("3");
		term3.add("4");
		term3.add("5");
		term3.add("6");
		term3.add("7");
		term3.add("8");
		term3.add("9");
		frame.add(term3);

		term4 = new Choice();
		term4.setBounds(140, 290, 165, 25);
		term4.add("0");
		term4.add("1");
		term4.add("2");
		term4.add("3");
		term4.add("4");
		term4.add("5");
		term4.add("6");
		term4.add("7");
		term4.add("8");
		term4.add("9");
		frame.add(term4);

		term5 = new Choice();
		term5.setBounds(140, 320, 165, 25);
		term5.add("0");
		term5.add("1");
		term5.add("2");
		term5.add("3");
		term5.add("4");
		term5.add("5");
		term5.add("6");
		term5.add("7");
		term5.add("8");
		term5.add("9");
		frame.add(term5);

		term6 = new Choice();
		term6.setBounds(140, 350, 165, 25);
		term6.add("0");
		term6.add("1");
		term6.add("2");
		term6.add("3");
		term6.add("4");
		term6.add("5");
		term6.add("6");
		term6.add("7");
		term6.add("8");
		term6.add("9");
		frame.add(term6);

		term7 = new Choice();
		term7.setBounds(140, 380, 165, 25);
		term7.add("0");
		term7.add("1");
		term7.add("2");
		term7.add("3");
		term7.add("4");
		term7.add("5");
		term7.add("6");
		term7.add("7");
		term7.add("8");
		term7.add("9");
		frame.add(term7);

		term8 = new Choice();
		term8.setBounds(140, 410, 165, 25);
		term8.add("0");
		term8.add("1");
		term8.add("2");
		term8.add("3");
		term8.add("4");
		term8.add("5");
		term8.add("6");
		term8.add("7");
		term8.add("8");
		term8.add("9");
		frame.add(term8);

		term9 = new Choice();
		term9.setBounds(140, 440, 165, 25);
		term9.add("0");
		term9.add("1");
		term9.add("2");
		term9.add("3");
		term9.add("4");
		term9.add("5");
		term9.add("6");
		term9.add("7");
		term9.add("8");
		term9.add("9");
		frame.add(term9);

		term10 = new Choice();
		term10.setBounds(140, 470, 165, 25);
		term10.add("0");
		term10.add("1");
		term10.add("2");
		term10.add("3");
		term10.add("4");
		term10.add("5");
		term10.add("6");
		term10.add("7");
		term10.add("8");
		term10.add("9");
		frame.add(term10);

		inputs[0] = "VAT number :";
		inputs[1] = "Age: ";
		inputs[2] = "City :";
		inputs[3] = "Country initials :";
		inputs[4] = "Rate the terms.";
		inputs[5] = "Arts :";
		inputs[6] = "Sea :";
		inputs[7] = "Museums :";
		inputs[8] = "Restaurant :";
		inputs[9] = "Stadium :";
		inputs[10] = "Beach :";
		inputs[11] = "Hotel :";
		inputs[12] = "Club :";
		inputs[13] = "Sidewalks :";
		inputs[14] = "Bar :";

		submit = new JButton("Submit");
		submit.setBounds(10, 500, 165, 25);
		submit.addActionListener(this);
		frame.add(submit);

		show_travellers = new JButton("show_travellers");
		show_travellers.setBounds(10, 530, 165, 25);
		show_travellers.addActionListener(this);
		frame.add(show_travellers);

		filtering = new JButton("filtering");
		filtering.setBounds(10, 560, 165, 25);
		filtering.addActionListener(this);
		frame.add(filtering);

		label = new JLabel("Visit ");
		label.setForeground(Color.blue);
		label.setBounds(500, 180, 500, 25);
		frame.add(label);

		label = new JLabel("Cities :");
		label.setBounds(400, 210, 500, 25);
		frame.add(label);

		visitCity = new JTextField("athens,tokyo");
		visitCity.setBounds(500, 210, 500, 25);
		frame.add(visitCity);

		label = new JLabel("Countries's initials :");
		label.setBounds(390, 240, 500, 25);
		frame.add(label);

		visitCountry = new JTextField("gr,jp", 20);
		visitCountry.setBounds(500, 240, 500, 25);
		frame.add(visitCountry);

		label = new JLabel("Returned cities :");
		label.setBounds(390, 270, 500, 25);
		frame.add(label);

		show = new JTextField("2");
		show.setBounds(500, 270, 500, 25);
		frame.add(show);

		int step = 0;
		for (int i = 0; i < inputs.length; i++) {
			step = step + 30;

			if (i == 5) {
				label.setForeground(Color.blue);
				label.setBounds(0, step, 200, 25);
				step = step + 20;

			}
			label = new JLabel(inputs[i]);
			label.setBounds(10, step, 120, 25);
			frame.add(label);
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public String getVatNumber() {
		return vatNumber.getText();
	}

	public int getAge() {
		return Integer.parseInt(age.getText());
	}

	public String getCityName() {
		return cityName.getText();
	}

	public String getcountryInitials() {
		return countryInitials.getText();
	}

	public int getTerm1() {
		return term1.getSelectedIndex();

	}

	public int getTerm2() {
		return term2.getSelectedIndex();

	}

	public int getTerm3() {
		return term3.getSelectedIndex();

	}

	public int getTerm4() {
		return term4.getSelectedIndex();

	}

	public int getTerm5() {
		return term5.getSelectedIndex();

	}

	public int getTerm6() {
		return term6.getSelectedIndex();

	}

	public int getTerm7() {
		return term7.getSelectedIndex();

	}

	public int getTerm8() {
		return term8.getSelectedIndex();

	}

	public int getTerm9() {
		return term9.getSelectedIndex();

	}

	public int getTerm10() {
		return term10.getSelectedIndex();
	}

	public String getVisitCity() {
		return visitCity.getText();

	}

	public String getVisitCountry() {
		return visitCountry.getText();
	}

	public int show() {
		return Integer.parseInt(show.getText());
	}

	private int error;

	public static void main(String[] args) {
		new Gui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		error = 0;
		Json json = new Json();

		travellers = new ArrayList<Traveller>();
		try {
			travellers = json.readJSON();
		} catch (JsonParseException ex) {
			ex.printStackTrace();
		} catch (JsonMappingException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		
		if (e.getActionCommand().equals("Submit") || e.getActionCommand().equals("filtering")) {

			OracleDBServiceCRUD dataBase;
			dataBase = new OracleDBServiceCRUD();
			mapOfCities = new HashMap<String, City>();

			try {
				dataBase.ReadData(mapOfCities);
			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (OutOfBounds e1) {
				e1.printStackTrace();
			}

			show.setForeground(Color.black);
			age.setForeground(Color.black);
			cityName.setForeground(Color.black);
			countryInitials.setForeground(Color.black);
			visitCity.setForeground(Color.black);
			visitCountry.setForeground(Color.black);

			try {
				int age = getAge();

				if (age >= 16 && age <= 25) {
					Young_traveller young_traveller = new Young_traveller(getTerm1(), getTerm2(), getTerm3(),
							getTerm4(), getTerm5(), getTerm6(), getTerm7(), getTerm8(), getTerm9(), getTerm10());
					travellers.add(young_traveller);

				} else if (age <= 60) {
					Middle_traveller middle_traveller = new Middle_traveller(getTerm1(), getTerm2(), getTerm3(),
							getTerm4(), getTerm5(), getTerm6(), getTerm7(), getTerm8(), getTerm9(), getTerm10());
					travellers.add(middle_traveller);

				} else {
					Elder_traveller elder_traveller = new Elder_traveller(getTerm1(), getTerm2(), getTerm3(),
							getTerm4(), getTerm5(), getTerm6(), getTerm7(), getTerm8(), getTerm9(), getTerm10());
					travellers.add(elder_traveller);

				}
				travellers.get(travellers.size() - 1).setAge(age);
			} catch (InputMismatchException ex) {
				System.out.println("Your age should be an integer. Try again.");
				age.setForeground(Color.red);
				error++;
			} catch (OutOfBounds ex) {
				System.out.println("Try to enter your age again.");
				age.setForeground(Color.red);
				error++;
			} catch (Exception ex) {
				System.out.println("Something went wrong. Exiting from program.");
				age.setForeground(Color.red);
				error++;
			}

			travellers.get(travellers.size() - 1).setVatNumber(getVatNumber());
			travellers.get(travellers.size() - 1).setCityName(getCityName());
			travellers.get(travellers.size() - 1).setCountryName(getcountryInitials());

			try {
				travellers.get(travellers.size() - 1).retrieveLatLon();
			} catch (CountryException c) {
				System.out.println("Type the correct country's initials ");
				countryInitials.setForeground(Color.red);
				error++;
			} catch (UniformInterfaceException u) {
				if (u.getMessage().contains("404")) {
					System.out.println("Type a correct city name for the country's initials: "
							+ travellers.get(travellers.size() - 1).getCountryName());
					cityName.setForeground(Color.red);
					error++;
				} else {
					cityName.setForeground(Color.red);
					System.out.println("Something went wrong. Exiting from program");
					error++;
				}
			} catch (JsonParseException e1) {
				e1.printStackTrace();
				error++;
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
				error++;
			} catch (IOException e1) {
				e1.printStackTrace();
				error++;
			}

			if (e.getActionCommand().equals("filtering")) {
				if (error > 0)
					return;

				
				Traveller candidateTraveller = travellers.get(travellers.size() - 1);
				int[] candidateTravellerCriteria = candidateTraveller.getRatingsOfInterests();

				ArrayList<Traveller> newT = new ArrayList<Traveller>();
				
				travellers.remove(travellers.size()-1);
				newT = travellers;

				for (int i = 0; i < newT.size()-1; i++) {
					for (int y = newT.size()-1 ; y > i; y--) {
						if (newT.get(i).getVisit().equals(newT.get(y).getVisit())) {
							newT.remove(y);
						}
					}
				}

				Map<String, Integer> cityToRank = newT.stream().collect(Collectors.toMap(i -> i.getVisit(),
						i -> innerDot(i.getRatingsOfInterests(), candidateTravellerCriteria)));

				cityToRank.forEach((k, v) -> System.out.println("city:" + k + " rank: " + v));

				Optional<RecommendedCity> recommendedCity = newT.stream()
						.map(i -> new RecommendedCity(i.getVisit(),
								innerDot(i.getRatingsOfInterests(), candidateTravellerCriteria)))
						.max(Comparator.comparingInt(RecommendedCity::getRank));

				System.out.println("The Recommended City:" + recommendedCity.get().getCity());
								
				String[] options = new String[2];
				options[0] = new String("Save");
				options[1] = new String("Cancel");

				int reply = JOptionPane.showOptionDialog(null,"The city we chose for you is:"+recommendedCity.get().getCity(),"INFO", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
				if (reply == JOptionPane.YES_OPTION) { 
					travellers.add(candidateTraveller);
					Date date = new Date();
					travellers.get(travellers.size()-1).setTimestamp(date.getTime());
					 
					travellers.get(travellers.size()-1).setVisit(recommendedCity.get().getCity());
					for(int i = 0 ; i<travellers.size(); i++) {
						System.out.println(i+" "+travellers.get(i).getVisit()+" "+travellers.get(i).getVatNumber());
					}
					saveTraveller(travellers);

					if (calculateFreeTicket(travellers, mapOfCities.get(travellers.get(travellers.size()-1).getVisit()))
							.equals(travellers.get(travellers.size() - 1).getVatNumber()))
						JOptionPane.showMessageDialog(null,
								"You get the free ticket for the city:" + travellers.get(travellers.size()-1).getVisit(),
								"CONGRATS!", JOptionPane.PLAIN_MESSAGE);

				}
			  
			} else if(e.getActionCommand().equals("Submit")) {


			choosenCities = new ArrayList<City>();
			searchCities = new ArrayList<String>();
			Date date = new Date();

			// The user add to the array searchCities the cityName_countryName of the cities
			// the wants to compare.

			String visitCities = getVisitCity();
			String[] vCity = visitCities.split(",");

			String visitCountries = getVisitCountry();
			String[] vCountry = visitCountries.split(",");

			if (vCity.length != vCountry.length) {
				visitCity.setForeground(Color.red);
				visitCountry.setForeground(Color.red);
				return;
			} else {

				for (int i = 0; i < vCity.length; i++) {
					searchCities.add(vCity[i] + "_" + vCountry[i]);
				}

			}

			t = new Thread(this, "Admin Thread");
			t.start();

			for (int i = 0; i < searchCities.size(); i++) {
				if (!searchCity(searchCities.get(i), mapOfCities)) {
					// If the city doesn't exist in the hash map of cities we add it. We save the
					// city's name with lower case letters.
					City objCity = new City(searchCities.get(i).split("_")[0], searchCities.get(i).split("_")[1]);
					mapOfCities.put(searchCities.get(i), objCity);

					try {

						mapOfCities.get(searchCities.get(i)).retrieveLatLon();
					} catch (CountryException v) {
						visitCountry.setForeground(Color.red);
						System.out.println("Type the correct country's initial for the city "
								+ mapOfCities.get(searchCities.get(i)).getCityName() + ":");
						error++;
					} catch (UniformInterfaceException u) {
						if (u.getMessage().contains("404")) {
							visitCity.setForeground(Color.red);
							System.out.println("The name of the city "
									+ mapOfCities.get(searchCities.get(i)).getCityName() + " is wrong.");
							System.out.println("Type a correct city name for the country's initial "
									+ mapOfCities.get(searchCities.get(i)).getCountryName() + ":");
							error++;
						} else {
							error++;
							visitCountry.setForeground(Color.red);
							visitCity.setForeground(Color.red);
							System.out.println("Something went wrong. Exiting from program");
						}
					} catch (JsonParseException e1) {
						e1.printStackTrace();
						error++;
					} catch (JsonMappingException e1) {
						e1.printStackTrace();
						error++;
					} catch (IOException e1) {
						e1.printStackTrace();
						error++;
					}

					// if the city doesn't already exist we add it to the data base.
					dataBase.addDataToDB(searchCities.get(i), mapOfCities.get(searchCities.get(i)).getGeodesicLat(),
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
				// we renew the timeStamp every time e search a new city for the traveler.
				travellers.get(travellers.size() - 1).setTimestamp(date.getTime());
				// we add the object city to the array list.
				choosenCities.add(mapOfCities.get(searchCities.get(i)));
			}

				DefaultListModel<String> model = new DefaultListModel<String>();
				try {
					if (show() == 1) {
						model.addElement(travellers.get(travellers.size() - 1).compareCities(choosenCities)
								.getCityName() + "_"
								+ travellers.get(travellers.size() - 1).compareCities(choosenCities).getCountryName());
					} else {

						ArrayList<City> cities = travellers.get(travellers.size() - 1).compareCities(choosenCities,
								show());
						for (int i = 0; i < cities.size(); i++) {
							model.addElement(cities.get(i).getCityName() + "_" + cities.get(i).getCountryName());
						}
					}
				} catch (InputMismatchException ex) {
					show.setForeground(Color.red);
					System.out.println("You can't give a character for input. Try again.");
					error++;
				} catch (OutOfBounds ex) {
					show.setForeground(Color.red);
					System.out.println(ex.getMessage());
					error++;

				} catch (ArrayIndexOutOfBoundsException ex) {
					show.setForeground(Color.red);
					System.out.println("You can enter an integer between 1 to " + searchCities.size());
					error++;
				}

				if (error > 0)
					return;

				JList<String> list = new JList<String>(model);

				Object[] options = { "Save", "Cancel" };
				UIManager.put("OptionPane.minimumSize", new Dimension(600, 50));
				int n = JOptionPane.showOptionDialog(null, list,
						"Choose the city you want to visit. "
								+ "The cities are sorted depending of what suits you the best.",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (n == JOptionPane.YES_OPTION) {
					travellers.get(travellers.size() - 1).setVisit(list.getSelectedValue().toString());
					if (calculateFreeTicket(travellers, mapOfCities.get(list.getSelectedValue().toString()))
							.equals(travellers.get(travellers.size() - 1).getVatNumber()))
						JOptionPane.showMessageDialog(null,
								"You get the free ticket for the city:" + list.getSelectedValue().toString(),
								"CONGRATS!", JOptionPane.PLAIN_MESSAGE);

					saveTraveller(travellers);
				}

			}
		}if(e.getActionCommand().equals("show_travellers"))

	{
		JOptionPane.showMessageDialog(null, seeTravellersNoDuplicates(travellers));
	}
	}

	private static int innerDot(int[] currentTraveller, int[] candidateTraveller) {
		int sum = 0;
		for (int i = 0; i < 10; i++)
			sum += currentTraveller[i] * candidateTraveller[i];
		return sum;

	}

	public static String calculateFreeTicket(ArrayList<Traveller> travellers, City obj) {
		double max = travellers.get(0).calculateSimilarity(obj);
		int maxPos = 0;
		for (int i = 1; i < travellers.size(); i++) {
			if (max < travellers.get(i).calculateSimilarity(obj) && travellers.get(i).getTimestamp() != 0) {
				max = travellers.get(i).calculateSimilarity(obj);
				maxPos = i;
			}
		}
		// System.out.print(travellers.get(maxPos).getVatNumber());
		return travellers.get(maxPos).getVatNumber();

	}

	public static boolean searchCity(String searchCities, HashMap<String, City> mapOfCities) {
		for (String cities : mapOfCities.keySet()) {
			if (searchCities.equals(cities)) {
				return true;
			}
		}
		return false;
	}

	public static Set<String> seeTravellersNoDuplicates(ArrayList<Traveller> travellers) {

		ArrayList<String> newTravellersNoDuplicates = new ArrayList<String>();
		for (int i = 0; i < travellers.size(); i++) {
			newTravellersNoDuplicates.add(travellers.get(i).getVatNumber());
		}

		Set<String> noDuplicatesSet = new LinkedHashSet<>(newTravellersNoDuplicates);

		return (noDuplicatesSet);

	}

	public void saveTraveller(ArrayList<Traveller> travellers) {
		Json json = new Json();
		Collections.sort(travellers);

		Iterator<Traveller> itr = travellers.iterator();
		while (itr.hasNext()) {
			Traveller t = (Traveller) itr.next();
			if (t.getTimestamp() == 0) {
				itr.remove();
			}
		}
		for(int i = 0 ; i<travellers.size(); i++) {
			System.out.println(i+" "+travellers.get(i).getVisit()+" "+travellers.get(i).getVatNumber()+" "+travellers.get(i).getTimestamp());
		}

		try {
			json.writeJSON(travellers);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		String[] terms = { "arts", "sea", "museums", "restaurant", "stadium", "beach", "hotel", "club", "sidewalks",
				"bar" };

		for (int i = 0; i < searchCities.size(); i++) {
			if (!searchCity(searchCities.get(i), mapOfCities)) {
				try {
					mapOfCities.get(searchCities.get(i)).retrieveTermVectors(terms);
				} catch (WikipediaNoArcticleException ex) {
					System.out.println(ex.getMessage());
					System.out.print("Type a correct city name for the country "
							+ mapOfCities.get(searchCities.get(i)).getCountryName() + ":");
					cityName.setForeground(Color.red);
					error++;
				} catch (Exception ex) {
					System.out.println("Something went wrong. Exiting from program.");
					error++;
				}

			}

		}

	}

}