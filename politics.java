// Created by: Harry James Hocker
// Property of University of Central Florida
// Professor Guha

import java.util.*;

public class politics {

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in);

		int number_of_candidates = stdin.nextInt();
		int number_of_supporters = stdin.nextInt();

		// checks for 0 0 case
		while (number_of_candidates != 0 && number_of_supporters != 0) {

			// create a HashMap of candidate names with an associated list of supporters
			LinkedHashMap<String, List<String>> list_of_candidates = new LinkedHashMap<>();
			// NOTE: LINKED HashMap because it keeps the insertion order
			for (int i = 0; i < number_of_candidates; i++) {
				list_of_candidates.put(stdin.next(), new ArrayList<>());
			}
			stdin.nextLine();

			// get the person and candidate and place them into the candidate list or make a new candidate
			for (int j = 0; j < number_of_supporters; j++) {
				String person = stdin.next();
				String candidate = stdin.next();

				// if the candidate already exists, add the person
				if (list_of_candidates.containsKey(candidate)) {
					List<String> temp = list_of_candidates.get(candidate);
					temp.add(person);
				// if the candidate does NOT exist, add the candidate and person
				} else {
					list_of_candidates.put(candidate, new ArrayList<>());
					List<String> temp = list_of_candidates.get(candidate);
					temp.add(person);
				}
			}

			// for each candidate, print out the list of names
			list_of_candidates.forEach((key, value) -> {
				for (String person : value) {
					System.out.println(person);
				}
			});

			// repeat loop here
			number_of_candidates = stdin.nextInt();
			number_of_supporters = stdin.nextInt();
		}
	}
}
