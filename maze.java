// Copyright Harry James Hocker
// created on March 10th, 2022

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

// a single spot
class Spot {

	char value;
	int x;
	int y;
	int distance;
	boolean portal;

	Spot(char value, int x, int y, boolean portal) {
		this.value = value;
		this.x = x;
		this.y = y;
		this.portal = portal;
		this.distance = -1;
	}
}

// a list of portals
class Portals
{
	// if this list has already been added, we want to skip it
	boolean used;
	// a linked list of portals to easily combine with the queue
	LinkedList<Spot> list;
	// when creating a new portal list, add the first new portal and set the list to unused
	public Portals(Spot first) {
		this.list = new LinkedList<>();
		this.list.add(first);
		this.used = false;
	}
}

// the entire map and all of its info
class Map
{
	// size of the map
	int rows;
	int cols;
	// the starting and ending points
	Spot start;
	Spot end;
	// the map is made out of 2D array of "Spot" objects
	Spot[][] map;
	// This will be used to track all the portals of similar character together.
	HashMap<Character, Portals> portals;
	// The queue for the BFS operation
	LinkedList<Spot> queue;

	Map(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		this.map = new Spot[rows][cols];
		this.portals = new HashMap<>();
		this.queue = new LinkedList<>();
	}
}

// the main class
public class maze {

	// this is faster than scanner btw
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {

		// get the rows and columns first
		String[] rowcol = br.readLine().split(" ");
		int rows = Integer.parseInt(rowcol[0]);
		int cols = Integer.parseInt(rowcol[1]);

		// read in the map as a single-line string
		String[] string_map = read_map(rows);
		// transform the string into objects
		Map map = create_map(string_map, rows, cols);

		// this is the man BFS loop in O(n) for n = spots.
		while (!map.queue.isEmpty()) {
			// get first item from queue
			Spot spot = map.queue.getFirst();
			// add all surrounding items
			add_surrounding(map, spot);
			// add portals
			add_portals(map, spot);
			// remove first value until the queue is empty
			map.queue.removeFirst();
		}

		// print out the distance
		System.out.println(map.end.distance);
	}

	// method to add surrounding spots and portals to the queue of a map object
	public static void add_surrounding(Map map, Spot spot) {
		// only call the spot once
		int x = spot.x;
		int y = spot.y;
		// coordinates for surrounding spots
		int[] DX = {-1,  0, 0, 1};
		int[] DY = { 0, -1, 1, 0};
		// enqueue all surrounding spots
		for (int i = 0; i < 4; i++) {
			int dx = x + DX[i];
			int dy = y + DY[i];
			// if it's inbounds, attempt to add to queue
			if (inbounds(dx, dy, map.rows, map.cols)) {
				Spot d_spot = map.map[dy][dx];
				enqueue(map, d_spot, spot.distance);
			}
		}
	}

	// adds entire linked list of portals to queue if the spot is a portal
	private static void add_portals(Map map, Spot spot) {
		// if the spot is a portal
		if (spot.portal) {
			// set temp variables for Portals class and its list
			Portals portals = map.portals.get(spot.value);
			LinkedList<Spot> list = portals.list;

			// if the list isn't already used:
			if (!portals.used) {
				// append entire list to queue
				map.queue.addAll(portals.list);
				// set the distance for each portal spot
				while (!list.isEmpty()) {
					Spot temp = list.getFirst();
					// don't update distance of origin portal
					if (!temp.equals(spot)) {
						temp.distance = spot.distance + 1;
					}
					// remove each portal
					list.removeFirst();
				}
				// set the list as used
				portals.used = true;
			}
		}
	}

	// simple method to add to the queue
	public static void enqueue(Map map, Spot spot, int distance) {
		// skip if bad, queue if good and update distance
		if (spot.distance == -1) {
			spot.distance = distance + 1;
			map.queue.add(spot);
		}
	}

	// read in array of strings and create a map object filled with spot objects
	public static Map create_map(String[] map, int rows, int cols)
	{
		Map new_map = new Map(rows, cols);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// create a new spot
				Spot new_spot = new Spot(map[i].charAt(j),j, i, false);
				// add spot to the object map
				new_map.map[i][j] = new_spot;
				// switch-case for tagging spots in the map
				tag_spot(new_map, new_spot);
			}
		}
		return new_map;
	}

	// this helps to keep track of the start, end, and portals of the map.
	// Basically the powerhouse of the program.
	private static void tag_spot(Map map, Spot spot)
	{
		switch (spot.value)
		{
				// do nothing
			case '.' : break;
				// set to 0 to skip it
			case '!' :
				spot.distance = 0;
				break;
			case '*' :
				// save starting point and add to queue, set to 0
				map.start = spot;
				map.queue.add(spot);
				spot.distance = 0;
				break;
			case '$' :
				// save ending point
				map.end = spot;
				break;
			// DEFAULT: if it's not one of the other cases, it's a portal
			default :
			{
				spot.portal = true;
				// if the linked list already exists, append the spot
				if (map.portals.containsKey(spot.value)) {
					map.portals.get(spot.value).list.add(spot);
				} else {
					// if the list does not yet exist, make one and add the spot
					map.portals.put(spot.value, new Portals(spot));
					map.portals.get(spot.value).list.add(spot);
				}
			}
		}
	}

	// read in each row as a string and return an array of strings to represent a map
	public static String[] read_map(int rows) throws IOException {

		String[] map = new String[rows];

		for (int i = 0; i < rows; i++) {
			map[i] = br.readLine();
		}
		return map;
	}

	public static boolean inbounds(int x, int y, int r, int c) {
		return (x >= 0 && x < c) && (y >= 0 && y < r);
	}

}

