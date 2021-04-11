package it21918;

//For ages between 16-25 
public class Young_traveller extends Traveller {
	private final double p = 0.95;

	public Young_traveller() {
	}

	public Young_traveller(int term1, int term2, int term3, int term4, int term5, int term6, int term7, int term8,
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
	double similarity_terms_vector(City obj) {
		double sumInterseption = 0, sumUnion = 0;

		for (int i = 0; i < 10; i++) {
			if (obj.getTermsVector(i) > 1 && getRatingsOfInterests(i) > 1) {
				sumInterseption += 1;
			}
			if (obj.getTermsVector(i) > 1 || getRatingsOfInterests(i) > 1) {
				sumUnion += 1;
			}
		}

		if (sumUnion == 0) {
			return 0;
		}
		return sumInterseption / sumUnion;
	}

	@Override
	double calculate_similarity(City obj) {
		double distance = distance(obj.getGeodesicLat(), obj.getGeodesicLon(), getCoordinatesLat(),
				getCoordinatesLon());
		return p * similarity_terms_vector(obj) + (1 - p) * similarity_geodesic_vector(distance);
	}

}