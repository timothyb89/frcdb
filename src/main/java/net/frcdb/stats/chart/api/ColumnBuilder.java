package net.frcdb.stats.chart.api;

/**
 *
 * @author tim
 */
public class ColumnBuilder {
	
	private ColumnDefinition column;
	
	public ColumnBuilder() {
		column = new ColumnDefinition();
	}
	
	public ColumnBuilder id(String id) {
		column.setId(id);
		return this;
	}
	
	public ColumnBuilder type(String type) {
		column.setType(type);
		return this;
	}
	
	public ColumnBuilder label(String label) {
		column.setLabel(label);
		return this;
	}
	
	public ColumnBuilder role(String role) {
		column.setRole(role);
		return this;
	}

	public ColumnDefinition getColumn() {
		return column;
	}
	
	public ColumnDefinition get() {
		return column;
	}
	
}
