// Copyright Harry James Hocker
// Created on March 30th, 2022.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class poly {

	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	// all the important code goes here! scanner etc
	public static void main(String[] args) throws IOException {
		// calculate the size of the arrays coming in (2^n)
		int size = Integer.parseInt(br.readLine());
		int poly_size = (int) Math.pow(2, size);
		// premake the arrays
		long[] poly1 = new long[poly_size];
		long[] poly2 = new long[poly_size];
		// read in each line
		String[] split1 = br.readLine().split(" ");
		String[] split2 = br.readLine().split(" ");
		// build each array
		for (int i = 0; i < poly_size; i++) {
			poly1[i] = Integer.parseInt(split1[i]);
			poly2[i] = Integer.parseInt(split2[i]);
		}
		// create objects
		poly ply = new poly();
		polynomial poly_a = new polynomial(poly1);
		polynomial poly_b = new polynomial(poly2);
		// calculate the polynomial
		polynomial total = ply.karatsuba(poly_a, poly_b);
		// print out the answer using a string builder
		ply.print_all(total);
	}

	// builds a string from the final polynomial and prints it all out
	public void print_all(polynomial poly) {
		StringBuilder sb = new StringBuilder();
		// index 0 is a blank spot, so we skip it
		for (int i = 1; i < poly.length; i++) {
			sb.append(poly.coefficient[i]).append("\n");
		}
		System.out.println(sb);
	}

	// Both this and other must be of the same size and the
	// corresponding lengths must be powers of 2.
	// Returns the sum of a and b in a newly created poly.
	public polynomial add(polynomial a, polynomial b) {
		long[] array = new long[a.length];
		// we can only add values of the same coefficient
		for (int i = 0; i < a.length; i++) {
			array[i] = a.coefficient[i] + b.coefficient[i];
		}
		return new polynomial(array);
	}

	// Both this and other must be of the same size and the
	// corresponding lengths must be powers of 2.
	// Returns the difference of a and b in a new poly.
	public polynomial subtract(polynomial a, polynomial b) {
		long[] array = new long[a.length];
		// we can only subtract values of the same coefficient
		for (int i = 0; i < a.length; i++) {
			array[i] = a.coefficient[i] - b.coefficient[i];
		}
		return new polynomial(array);
	}

	// Both this and other must be of the same size and the
	// corresponding lengths must be powers of 2.
	// Returns the product of this and other in a newly created
	// poly, using the regular nested loop algorithm, with the
	// length being the next power of 2.
	public polynomial slowMultiply(polynomial a, polynomial b) {

		int length = a.length + b.length;
		long[] array = new long[length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				long temp = a.coefficient[i] * b.coefficient[j];
				// greatest exponent is at i = 0, j = 0; smallest at i = length & j = length
				array[i + j + 1] += temp;
				// NOTE: + 1 because values going into new array should never reach index 0
				// example: n = 4; x^3 * x^3 = x^6. Highest is x^7.
			}
		}
		return new polynomial(array);
	}

	// kind of a dirty way to add everything together
	public polynomial shift_add(polynomial z0, polynomial z1, polynomial z2) {
		int n = z0.length;
		long[] array = new long[n*2];
		// add each polynomial value at the appropriate index
		for (int i = 0; i < z0.length; i++) {
			array[i] += z0.coefficient[i];
			array[i+n/2] += z1.coefficient[i];
			array[i+n] += z2.coefficient[i];
		}
		return new polynomial(array);
	}

	// Both this and other must be of the same size and the
	// corresponding lengths must be powers of 2.
	// Returns the product of this and other in a newly created
	// poly, using Karatsuba’s algorithm, with the
	// length being the next power of 2.
	private polynomial karatsuba(polynomial a, polynomial b) {

		// base case of length of 32 (2^5)
		if (a.length <= 32) {
			return slowMultiply(a, b);
		}
		// split into halves
		polynomial left_a = a.getLeft();
		polynomial right_a = a.getRight();
		polynomial left_b = b.getLeft();
		polynomial right_b = b.getRight();
		// get the top and bottom
		polynomial z0 = karatsuba(left_a, left_b);
		polynomial z2 = karatsuba(right_a, right_b);
		// get the sum of z0 and z2 for calculating z1
		polynomial sum = add(z0, z2);
		// work towards getting z1
		polynomial aSum = add(left_a, right_a);
		polynomial bSum = add(left_b, right_b);
		polynomial mult = karatsuba(aSum, bSum);
		polynomial z1 = subtract(mult, sum);
		// returns the final result
		return shift_add(z0, z1, z2);
	}
}

// moved into its own class for easier construction
class polynomial {
	// values
	public int length;
	public long[] coefficient;
	// Creates a polynomial from the coefficients stored in vals.
	// The polynomial created must store exactly (1<<k) coefficients
	// for some integer k.
	public polynomial(long[] vals) {
		this.length = vals.length;
		this.coefficient = Arrays.copyOf(vals, this.length);
	}
	// Returns the left half of this poly in its own poly.
	public polynomial getLeft() {
		long[] left = Arrays.copyOf(this.coefficient, this.length/2);
		return new polynomial(left);
	}
	// Returns the right half of this poly in its own poly.
	public polynomial getRight() {
		long[] left = Arrays.copyOfRange(this.coefficient, this.length/2, this.length);
		return new polynomial(left);
	}
}

