package hua.dit.oopii.it21918;

import hua.dit.oopii.exception.OutOfBounds;

//For ages between 16-25 
public class Young_traveller extends Traveller {
	private final double p = 0.95;

	public Young_traveller() {
	}

	public Young_traveller(int term1, int term2, int term3, int term4, int term5, int term6, int term7, int term8,
			int term9, int term10) throws OutOfBounds {
		addRatingOfterms(0,term1);
		addRatingOfterms(1,term2);
		addRatingOfterms(2,term3);
		addRatingOfterms(3,term4);
		addRatingOfterms(4,term5);
		addRatingOfterms(5,term6);
		addRatingOfterms(6,term7);
		addRatingOfterms(7,term8);
		addRatingOfterms(8,term9);
		addRatingOfterms(9,term10);
	}

	@Override
	double similarityTermsVector(City obj) {
		double sumInterseption = 0, sumUnion = 0;

		for (int i = 0; i < 10; i++) {
			if (obj.getTermsVector(i) > 1 && getRatingsOfInterest(i) > 1) {
				sumInterseption += 1;
			}
			if (obj.getTermsVector(i) > 1 || getRatingsOfInterest(i) > 1) {
				sumUnion += 1;
			}
		}

		if (sumUnion == 0) {
			return 0;
		}
		return sumInterseption / sumUnion;
	}

	@Override
	public double calculateSimilarity(City obj) {
		double distance = distance(obj.getGeodesicLat(), obj.getGeodesicLon(), getCoordinatesLat(),
				getCoordinatesLon());
		return p * similarityTermsVector(obj) + (1 - p) * similarityGeodesicVector(distance);
	}

}