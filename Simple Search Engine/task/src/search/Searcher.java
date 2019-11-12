package search;

import java.util.List;
import java.util.Map;

class Searcher {

	private SearchingStrategy strategy;

	Searcher(SearchingStrategy strategy) {
		this.strategy = strategy;
	}


	List<Integer> search(Map<String, List<Integer>> invertedIndex, List<String> data) {
		return strategy.search(invertedIndex, data);
	}
}
