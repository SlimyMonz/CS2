// created by Harry James Hocker
// January 21, 2022

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class tentaizu {

	final static int n = 7;
	final static int stars = 10;
	final static String STAR = "*";
	final static String EMPTY = ".";
	// this looks stupid as hell, but it honestly makes sense for one-dimension arrays
	final static int[] DR = {-1-n, -n, 1-n, -1, 1, n-1, n, n+1};
	final static int[] DR_LEFT = {-n, 1-n, 1, n, n+1};
	final static int[] DR_RIGHT = {-1-n, -n, -1, n-1, n};

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int numCases = Integer.parseInt(br.readLine());

		// case loop where code will go inside
		for (int loop = 0; loop < numCases; loop++) {
			// needed for the map data
			String[][] map = new String[n][n];
			String[] map_2 = new String[n*n];

			// read data into the map array
			for (int i = 0; i < n; i++) {
				String line = br.readLine();
				String[] split = line.split("");
				System.arraycopy(split, 0, map[i], 0, n);
			}

			// copy data into second map
			int j = 0;
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					map_2[j] = map[x][y];
					j++;
				}
			}
			System.out.println("Tentaizu Board #" + (loop+1) + ":");
			// run tentaizu function
			find_tentaizu_2(map_2, 0, 0);
			if (loop+1 != numCases) br.readLine();
		}
	}


	// new solution for backtracking
	private static boolean find_tentaizu_2(String[] map, int k, int spot) {

		// if we got all the stars, then we print the solution
		if (k == stars) {
			// create function that checks that each numbered spot is satisfied
			if (all_numbers_satisfied_2(map)) {
				// print map and return true
				print_map_2(map);
				// we found it! now to end all the recursions
				return true;
			}
			return false;
		}
		// keeping track of spot means we do not have to go over each spot again and again
		for (int i = spot; i < n*n; i++) {
				// check if placeable
				boolean available = can_place_2(map, i);
				if (available) {
					map[i] = STAR;
					boolean solved = find_tentaizu_2(map, k+1, i+1);
					if (solved) return true;
					map[i] = EMPTY;
				}
			}
		return false;
	}


	// checks every spot on the map and makes sure the number-spots are all satisfied
	private static boolean all_numbers_satisfied_2(String[] map) {
		// check every spot for a number value
		for (int i = 0; i < n*n; i++) {
				// if it's not a number value, skip it
				if (!is_number_2(map, i)) continue;
				// get the number value from the spot
				int value = Integer.parseInt(map[i]);
				// get the surrounding star count
				int surrounding = surrounding_stars_2(map, i);
				// if the spot value and the surrounding stars don't match, return false
				if (value != surrounding) return false;
			}
		return true;
	}

	// checks surrounding number_spot objects and makes sure none return true/false
	private static boolean can_place_2(String[] map, int spot) {

		// if it's already something else, don't place it. Duh!
		if (!map[spot].equals(EMPTY)) return false;
		// initialize
		int[] delta = left_middle_right(spot);
			// for each coordinate in the delta arrays
			for (int dr : delta) {
				// check if the coordinate is within boundaries
				boolean in_bounds = inBounds_2(spot + dr);
				if (in_bounds) {
					// check if there is a number spot nearby
					boolean number_spot = is_number_2(map, spot + dr);
					if (number_spot) {
						// count the amount of stars around the number value
						int nearby = surrounding_stars_2(map, spot + dr);
						// get the Integer value of the max surrounding stars
						int max = Integer.parseInt(map[spot + dr]);
						// if there are already enough stars in one of the number spots, return false
						if (nearby >= max) return false;
					}
				}
			}
		// able to be placed if no numbers are nearby or the spots aren't totally filled
		return true;
	}

	// this returns the stars surrounding any given spot
	private static int surrounding_stars_2(String[] map, int spot) {
		// initialize
		int nearby_stars = 0;
		int[] delta = left_middle_right(spot);
			// for each coordinate in the delta arrays
			for (int dr : delta) {
				// check if the coordinate is within boundaries
				if (inBounds_2(spot + dr)) {
					// if there is a star, increment total
					if (map[spot + dr].equals(STAR)) {
						nearby_stars++;
					}
				}
			}
		return nearby_stars;
	}

	// checks to see if the given spot is a number
	private static boolean is_number_2(String[] map, int spot) {
		return (!map[spot].equals(EMPTY) && !map[spot].equals(STAR));
	}

	//checks if the coordinates are legal for a 1-D array
	private static boolean inBounds_2(int spot) {
		return (spot > -1 && spot < n*n);
	}

	// prints out the map for one-dimension map array
	private static void print_map_2(String[] map) {

		// for every spot
		for (int i = 0; i < n*n; i++) {
			// print out a spot
			System.out.print(map[i]);
			// print out new line after 7 characters are printed
			if ((i + 1) % n == 0) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
	}

	// returns which DR array should be used based on the spot position
	private static int[] left_middle_right(int spot) {
		if (spot % n == 0) return DR_LEFT;
		if ((spot+1) % n == 0) return DR_RIGHT;
		return DR;
	}

}
