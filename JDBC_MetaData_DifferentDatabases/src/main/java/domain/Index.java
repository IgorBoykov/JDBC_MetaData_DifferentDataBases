package domain;

import java.util.ArrayList;
import java.util.List;

public class Index {

	private String primaryTable;
	private String indexName;
	private List<String> fields;

	public Index(String indexName, String tableName) {
		super();
		this.primaryTable = tableName;
		this.indexName = indexName;
		this.fields = new ArrayList<>();
	}

	public String getPrimaryTable() {
		return primaryTable;
	}

	public void setPrimaryTable(String primaryTable) {
		this.primaryTable = primaryTable;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public List<String> getField() {
		return fields;
	}

	public void setField(List<String> fields) {
		this.fields = fields;
	}

	public void addField(String field) {
		fields.add(field);

	}

	public boolean containsField(String field) {
		return fields.contains(field);

	}

	public boolean isValid(String fieldName) {
		return fields.contains(fieldName) && fields.size() == 1;
	}

	@Override
	public String toString() {
		return "Index [primaryTable=" + primaryTable + ", indexName=" + indexName + ", fields=" + fields + "]";
	}

}
