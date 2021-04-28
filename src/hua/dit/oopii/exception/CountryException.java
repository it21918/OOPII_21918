package hua.dit.oopii.exception;

public class CountryException extends Exception {
	private static final long serialVersionUID = 1L;
	static int numExcepetions = 0;
	private String country;

	public CountryException(String country) {
			numExcepetions++;
			this.country=country;
		}

	public String getMessage() {

		return "The country's initials : " + country + " are wrong.";
	}
}
