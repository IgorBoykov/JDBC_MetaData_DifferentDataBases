package dao;

import java.sql.SQLException;
import java.util.Map;

import domain.Index;

public class Dao {

	public static void map(Map<String, Index> m, String primaryTable, String indexName, String referenceColumn)
			throws SQLException {

		Index index = m.get(indexName);

		if (index == null) {
			index = new Index(indexName, primaryTable);
			m.put(indexName, index);
		}
		index.addField(referenceColumn);
	}

	public static boolean iterate(Map<String, Index> m, String columnName) {
		boolean found = false;
		for (Map.Entry<String, Index> entry : m.entrySet()) {
			found = entry.getValue().isValid(columnName);
			if (found)
				break;
		}
		return found;
	}

}