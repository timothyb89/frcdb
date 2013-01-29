package net.frcdb.stats.chart.api;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 *
 * @author tim
 */
public class ColumnDefinition {
	
	private String id;
	private String type;
	private String label;
	private String role;

	public ColumnDefinition() {
	}

	public ColumnDefinition(String id, String type, String label, String role) {
		this.type = type;
		this.label = label;
		this.role = role;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void serialize(JsonGenerator g) throws IOException {
		g.writeStartObject();
		
		if (label != null) {
			g.writeStringField("label", label);
		}
		
		if (type != null) {
			g.writeStringField("type", type);
		}
		
		if (role != null) {
			g.writeStringField("role", role);
		}
		
		g.writeEndObject();
	}
	
}
