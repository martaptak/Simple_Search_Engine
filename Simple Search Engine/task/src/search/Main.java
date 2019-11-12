package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum StrategyType {

	ALL, ANY, NONE
}

public class Main {
	private static Scanner scanner = new Scanner(System.in);
	private static List<String> dataSet = new ArrayList<>();
	private static String fileName;
	private static Map<String, List<Integer>> invertedIndex = new HashMap<>();

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("--data")) {
				fileName = args[i + 1];
			}
		}

		enterData();
		mappingDataSet();
		menu();
	}

	private static void enterData() {

		try (Scanner input = new Scanner(new File(fileName))) {
			while (input.hasNextLine()) {
				dataSet.add(input.nextLine());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	private static void mappingDataSet() {
		for (int i = 0; i < dataSet.size(); i++) {
			String data = dataSet.get(i);
			String[] words = data.toLowerCase().split(" ");
			for (String word : words) {
				if (invertedIndex.containsKey(word)) {
					List<Integer> wordIndexes = invertedIndex.get(word);
					wordIndexes.add(i);
					invertedIndex.replace(word, wordIndexes);
				} else {
					List<Integer> wordIndexes = new ArrayList<>();
					wordIndexes.add(i);
					invertedIndex.put(word, wordIndexes);
				}
			}
		}
	}


	private static void menu() {

		boolean exit = false;

		while (!exit) {
			System.out.println("=== Menu ===\n" +
					"1. Find a person\n" +
					"2. Print all people\n" +
					"0. Exit");

			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
				case 0:
					exit = true;
					System.out.println("Bye!");
					break;
				case 1:
					search();
					break;
				case 2:
					print();
					break;
				default:
					System.out.println("Incorrect option! Try again");
					break;
			}
		}
	}

	private static void print() {

		for (String s : dataSet) {
			System.out.println(s);
		}
	}

	private static void search() {

		System.out.println("Select a matching strategy: ALL, ANY, NONE");
		String strategy = scanner.nextLine();

		StrategyType strategyType;
		try {
			strategyType = StrategyType.valueOf(strategy.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.out.println("No such strategy");
			return;
		}

		System.out.println("\nEnter a name or email to search all suitable people.");
		String data = scanner.nextLine().toLowerCase();
		List<Integer> result = searchQuery(data, strategyType);

		if (result != null) {
			System.out.println("\nFound people: ");
			result.forEach(integer -> System.out.println(dataSet.get(integer)));
		} else {
			System.out.println("No results");
		}
	}

	private static List<Integer> searchQuery(String data, StrategyType strategy) {

		List<String> words = new ArrayList<>(Arrays.asList(data.toLowerCase().split(" ")));

		Searcher searcher = create(strategy);

		return searcher.search(invertedIndex, words);

	}

	private static Searcher create(StrategyType strategyType) {

		Searcher searcher = null;

		switch (strategyType) {
			case ALL:
				searcher = new Searcher(new AllSearchingStrategy()); break;
			case ANY:
				searcher = new Searcher(new AnySearchingStrategy());  break;
			case NONE:
				searcher = new Searcher(new NoneSearchingStrategy());  break;

		}

		return searcher;
	}

	public static List<String> getDataSet() {
		return dataSet;
	}
}