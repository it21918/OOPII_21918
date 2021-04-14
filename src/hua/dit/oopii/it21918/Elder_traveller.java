package hua.dit.oopii.it21918;

//For ages between 60-115 
public class Elder_traveller extends Traveller {
	private final double p = 0.05;

	public Elder_traveller() {}

	public Elder_traveller(int term1, int term2, int term3, int term4, int term5, int term6, int term7, int term8,
			int term9, int term10) {
		addRatingOfterms(term1);
		addRatingOfterms(term2);
		addRatingOfterms(term3);
		addRatingOfterms(term4);
		addRatingOfterms(term5);
		addRatingOfterms(term6);
		addRatingOfterms(term7);
		addRatingOfterms(term8);
		addRatingOfterms(term9);
		addRatingOfterms(term10);
	}

	@Override
	public double similarityTermsVector(City obj) {
		double sum = 0;
		for (int i = 0; i < 10; i++) {
			sum = sum + Math.pow(obj.getTermsVector(i) - getRatingsOfInterests(i), 2);
		}
		return 1 / (1 + Math.sqrt(sum));
	}

	@Override
	public double calculateSimilarity(City obj) {
		double distance = distance(obj.getGeodesicLat(), obj.getGeodesicLon(), getCoordinatesLat(),
				getCoordinatesLon());
		return p * similarityTermsVector(obj) + (1 - p) * similarityGeodesicVector(distance);
	}

}
