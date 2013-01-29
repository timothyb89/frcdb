package net.frcdb.stats.chart.api;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 *
 * @author tim
 */
public class CellValue {
	
	private Object value;

	public CellValue() {
	}

	public CellValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public void serialize(JsonGenerator g) throws IOException {
		g.writeObject(getValue());
	}
	
}
