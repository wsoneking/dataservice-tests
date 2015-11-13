package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelComparisonHelper {

	public static List<Difference> getDifferencesBetweenLinkCollections(
			Map<String, LinkObject> links, Map<String, LinkObject> compareLinks) {
		List<Difference> differences = new LinkedList<Difference>();

		// if the counts aren't the same stop
		if (links.values().size() != compareLinks.values().size()) {
			differences.add(new Difference(
					"Link Collection Counts Do Not Match", links.values().size(),
					compareLinks.values().size()));

			return differences;
		}
		
		LinkObject[] sourceArray = new LinkObject[links.values().size()];
		sourceArray = links.values().toArray(sourceArray);
		LinkObject[] compareArray = new LinkObject[compareLinks.size()];
		compareArray = compareLinks.values().toArray(compareArray);
		
		for (int i = 0; i < links.values().size(); i++) {
			differences.addAll(sourceArray[i]
					.getDifferenceBetweenObjects(compareArray[i]));
		}

		return differences;

	}

}
