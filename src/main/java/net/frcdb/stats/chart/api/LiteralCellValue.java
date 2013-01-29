package net.frcdb.stats.chart.api;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 *
 * @author tim
 */
public class LiteralCellValue extends  CellValue {
	
	private String format;

	public LiteralCellValue() {
	}

	public LiteralCellValue(Object value, String format) {
		super(value);
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public void serialize(JsonGenerator g) throws IOException {
		g.writeStartObject();
		g.writeObjectField("v", getValue());
		g.writeStringField("f", getFormat());
		g.writeEndObject();
	}
	
}
