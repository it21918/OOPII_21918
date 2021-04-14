package hua.dit.oopii.exception;

public class OutOfBounds extends Exception{
	private static final long serialVersionUID = 1L;
	static int numExcepetions = 0;
	private int n;
	private String limit;

	public OutOfBounds(int n, String limit) {
			numExcepetions++;
			this.n=n;
			this.limit = limit;
		}

	public String getMessage() {

		return "Your input:" + n + " is out of bounds. You can enter a number between " + limit;
	}
	
}
