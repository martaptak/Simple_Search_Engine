package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface SearchingStrategy {

	List<Integer> search(Map<String, List<Integer>> invertedIndex, List<String> data);

}

class NoneSearchingStrategy implements SearchingStrategy {

	@Override
	public List<Integer> search(Map<String, List<Integer>> invertedIndex, List<String> data) {
		List<Integer> results = new ArrayList<>();
		List<Integer> anyResult = new AnySearchingStrategy().search(invertedIndex, data);

		for (int i = 0; i < Main.getDataSet().size(); i++) {
			results.add(i);
		}

		results.removeAll(Objects.requireNonNull(anyResult));

		return results.isEmpty() ? null : results;
	}
}

class AnySearchingStrategy implements SearchingStrategy {
	@Override
	public List<Integer> search(Map<String, List<Integer>> invertedIndex, List<String> data) {

		List<Integer> result = new ArrayList<>();

		for (String word : data) {
			if (invertedIndex.get(word) != null) {
				result.addAll(invertedIndex.get(word));
			}
		}


		return result.isEmpty() ? null : result;
	}
}

class AllSearchingStrategy implements SearchingStrategy {
	@Override
	public List<Integer> search(Map<String, List<Integer>> invertedIndex, List<String> data) {
		List<Integer> result = new ArrayList<>();

		for (String word : data) {
			if (invertedIndex.get(word) != null) {
				if (result.isEmpty()) {
					result.addAll(invertedIndex.get(word));
				} else {
					result.retainAll(invertedIndex.get(word));
				}
			}
		}

		return result.isEmpty() ? null : result;
	}
}

