package net.frcdb.stats.chart.api;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class Row {
	
	private List<CellValue> values;
	
	public Row() {
		values = new ArrayList<CellValue>();
	}
	
	public Row(int cols) {
		values = new ArrayList<CellValue>(cols);
	}
	
	public void addValue(CellValue v) {
		values.add(v);
	}
	
	public void addValue(Object value) {
		values.add(new CellValue(value));
	}
	
	public void addValue(Object value, String formattedValue) {
		values.add(new LiteralCellValue(value, formattedValue));
	}
	
	public void setValue(int index, CellValue v) {
		values.set(index, v);
	}
	
	public void setValue(int index, Object value) {
		values.set(index, new CellValue(value));
	}
	
	public void setValue(int index, Object value, String formattedValue) {
		values.set(index, new LiteralCellValue(value, formattedValue));
	}
	
	public void serialize(JsonGenerator g) throws IOException {
		g.writeStartArray();
		
		for (CellValue v : values) {
			v.serialize(g);
		}
		
		g.writeEndArray();
	}
	
}
