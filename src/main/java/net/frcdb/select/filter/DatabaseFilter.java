package net.frcdb.select.filter;

import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Query;

/**
 *
 * @author tim
 */
public class DatabaseFilter {
	
	private String condition;
	private Object value;

	private String property;
	
	public DatabaseFilter() {
	}

	public DatabaseFilter(String operation, Object value) {
		this.condition = operation;
		this.value = value;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public <T> Query<T> apply(Query<T> l) {
		return l.filter(property + " " + condition, value);
	}
	
}
