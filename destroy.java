// Copyright by Harry James Hocker
// Created February 4th, 2022

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class destroy {

	// optimized scanner
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


	public static void main(String[] args) throws IOException {
		// initialize the values
		String[] temp = br.readLine().split(" ");
		int num_computer = Integer.parseInt(temp[0]);
		int num_union = Integer.parseInt(temp[1]);
		int num_destroy = Integer.parseInt(temp[2]);

		// scan for the rest of the items
		int[][] unions = get_unions(num_union);
		int[] destroys = get_destroys(num_destroy);

		// fill a computer array
		int[] computers = new int[num_computer];
		boolean[] flag = new boolean[num_computer];
		int[] size = new int[num_computer];
		for (int i = 0; i < num_computer; i++) {
			computers[i] = i;
			flag[i] = true;
			size[i] = 1;
		}

		// set flags
		for (int j = 0; j < num_destroy; j++) {
			flag[destroys[j]] = false;
		}

		// perform the unions and path compress
		for (int k = 0; k < num_union; k++)
			if (flag[k]) {
				do_union(computers, size, unions[k][0], unions[k][1]);
			}
		path_compression(computers);

		// create array and calc the LAST total
		long[] calculations = new long[num_destroy + 1];
		calculations[0] = calc_total(size);

		// perform ALL calculations
		// NOTE: LAST-destroyed union goes FIRST in calculation
		for (int l = 0; l < num_destroy; l++) {
			// the reverse-index we want
			int index = destroys[num_destroy-l-1];
			// get the unions
			int a = unions[index][0];
			int b = unions[index][1];
			// return roots
			int root_a = return_root(computers, a);
			int root_b = return_root(computers, b);
			// if they have the same root, copy previous calculation and continue
			if (root_a == root_b) {
				calculations[l + 1] = calculations[l];
			} else {
				// otherwise, do these calculations
				long group1 = size[root_a];
				long group2 = size[root_b];
				long both = group1 + group2;
				long calc = calculations[l];
				calc -= (group1 * group1) + (group2 * group2);
				calc += both * both;
				calculations[l + 1] = calc;
			}
			// perform union (this can be last because it affects nothing before it)
			do_union(computers, size, a, b);
		}

		// print out calculations in reverse order
		for (int m = num_destroy; m >= 0; m--) {
			System.out.println(calculations[m]);
		}
	}

	// may need to fix this to make it run quicker
	private static long calc_total(int[] size) {
		long total = 0;
		for (int i: size) {
			if (i != 0) total += (long) i * i;
		}
		return total;
	}

	// compress the tree in a simple way
	public static void path_compression(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != i) {
				array[i] = return_root(array, i);
			}
		}
	}

	// single union statement for two connections
	private static void do_union(int[] array, int[] size, int a, int b) {
		// initialize
		int root_a = return_root(array, a);
		int root_b = return_root(array, b);

		// don't bother if they have the same root
		if (root_a == root_b) return;

		// otherwise: set the root of b to the root of a
		array[root_b] = root_a;
		int temp = size[root_b];
		size[root_b] = 0;
		size[root_a] += temp;
	}

	// finds the root of any given index
	public static int return_root(int[] array, int i) {
		int root = i;
		while (root != array[root]) root = array[root];
		return root;
	}

	// scan for all the unions
	private static int[][] get_unions(int amount) throws IOException {
		int[][] unions = new int[amount][2];
		for (int i = 0; i < amount; i++) {
			String[] temp = br.readLine().split(" ");
			int a = Integer.parseInt(temp[0]);
			int b = Integer.parseInt(temp[1]);
			unions[i][0] = a-1;
			unions[i][1] = b-1;
		}
		return unions;
	}

	// scan for all the destroys
	private static int[] get_destroys(int amount) throws IOException {
		int[] destroys = new int[amount];
		for (int i = 0; i < amount; i++) {
			String temp = br.readLine();
			int val = Integer.parseInt(temp);
			destroys[i] = val-1;
		}
		return destroys;
	}
}




