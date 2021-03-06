
public class OutilsTactiques {
	//// ***********************************

	public final static int[][] casesSchemas = { { 1, 5 }, { 1, 8 }, { 1, 11 }, { 1, 12 }, { 1, 15 }, { 1, 23 },
			{ 2, 10 }, { 2, 22 }, { 2, 15 }, { 2, 24 }, { 2, 4 }, { 2, 6 }, { 2, 9 }, { 2, 7 }, { 3, 5 },
			{ 3, 8 }, { 3, 14 }, { 3, 13 }, { 3, 10 }, { 3, 23 }, { 4, 10 }, { 4, 12 }, { 4, 8 }, { 4, 20 },
			{ 4, 14 }, { 5, 11 },

			{ 5, 19 }, { 5, 14 }, { 5, 21 }, { 5, 7 }, { 5, 9 }, { 6, 8 }, { 6, 13 }, { 6, 15 }, { 6, 11 },
			{ 6, 20 }, { 7, 13 }, { 7, 11 }, { 7, 10 }, { 7, 17 }, { 8, 12 }, { 8, 16 }, { 8, 13 },
			{ 8, 18 }, { 9, 14 }, { 9, 15 }, { 9, 17 }, { 9, 12 }, { 10, 16 }, { 10, 19 }, { 10, 23 },
			{ 10, 24 }, { 11, 16 }, { 11, 20 }, { 11, 21 }, { 12, 22 }, //

			{ 12, 22 }, { 12, 19 }, { 12, 17 }, { 12, 18 }, { 13, 24 }, { 13, 21 }, { 13, 17 }, { 13, 16 },
			{ 14, 24 }, { 14, 18 }, { 14, 20 }, { 14, 19 }, { 15, 18 }, { 15, 21 }, { 15, 23 }, { 15, 22 },
			{ 16, 20 }, { 16, 23 }, { 17, 19 }, { 17, 21 }, { 17, 22 }, { 17, 24 }, { 18, 20 }, { 18, 23 },
			{ 19, 23 }, { 20, 22 }, { 20, 24 }, { 21, 23 } };

	////
	public final static int[][] casesNecessairesSchemas = { { 2, 3, 8 }, { 2, 3, 5 }, { 10, 12, 22 },
			{ 10, 11, 22 }, { 3, 2, 24 }, { 22, 10, 24 }, { 1, 3, 22 }, { 1, 3, 10 }, { 3, 1, 24 },
			{ 3, 2, 15 }, { 5, 6, 8 }, { 5, 4, 8 }, { 8, 5, 7 }, { 8, 5, 9 }, { 2, 1, 8 }, { 2, 1, 5 },
			{ 15, 13, 24 }, { 15, 14, 24 }, { 1, 2, 22 }, { 24, 15, 22 }, { 11, 12, 19 }, { 11, 10, 19 },
			{ 5, 2, 6 }, { 19, 11, 21 }, { 6, 5, 21 }, { 4, 6, 19 },

			{ 4, 6, 11 }, { 6, 4, 21 }, { 6, 4, 4 }, { 8, 2, 9 }, { 8, 2, 7 }, { 5, 2, 4 }, { 14, 15, 21 },
			{ 14, 13, 21 }, { 4, 5, 19 }, { 21, 14, 19 }, { 9, 8, 18 }, { 12, 10, 16 }, { 12, 11, 16 },
			{ 16, 12, 18 }, { 7, 9, 16 }, { 7, 9, 12 }, { 9, 7, 18 }, { 9, 7, 13 }, { 13, 15, 18 },
			{ 13, 14, 18 }, { 18, 13, 16 }, { 7, 8, 16 }, { 12, 7, 11 }, { 11, 4, 12 }, { 22, 1, 24 },
			{ 22, 1, 23 }, { 12, 7, 10 }, { 19, 4, 21 }, { 19, 4, 20 }, { 10, 1, 12 }, { 10, 1, 11 }, //

			{ 11, 4, 10 }, { 16, 7, 18 }, { 16, 7, 17 }, { 15, 3, 14 }, { 14, 6, 15 }, { 18, 9, 16 },
			{ 18, 9, 17 }, { 15, 3, 13 }, { 13, 9, 15 }, { 21, 6, 19 }, { 21, 6, 20 }, { 13, 9, 14 },
			{ 14, 6, 13 }, { 24, 3, 22 }, { 24, 3, 23 }, { 17, 18, 23 }, { 17, 18, 20 }, { 20, 21, 23 },
			{ 20, 18, 19 }, { 23, 20, 24 }, { 23, 20, 22 }, { 17, 16, 23 }, { 17, 16, 20 }, { 20, 17, 21 },
			{ 23, 17, 24 }, { 23, 17, 22 }, { 20, 17, 19 } };

	/// *************************************************
	/// contient les cases de placements stratégiques
	
	public final static int[][] placementsStrategiques = {{1,24},{3,22},{4,21},{6,19},{7,18},{9,16}};
	
	///pour chaque groupe ci-dessus correspondent les groupe 2n et 2n + 1 ci-dessous
	public final static int[][] casesNecessairesPS = {{22,10,23},{3,2,15},{1,2,10},{24,15,23},{19,11,20},{6,5,14},{4,5,11},{21,14,20},{16,12,17},{9,8,13},{7,8,12},{18,13,17}};

	///**************************************************
	/// cases stratégiques, en fonction du nombre d'intersections
	public final static int[] cases4intersections = {5,11,20,14};
	public final static int[] cases3intersections = {2,10,23,15,8,12,17,13};
	
	

}