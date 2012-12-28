package net.frcdb.robot;

import com.googlecode.objectify.annotation.Id;
import java.util.UUID;

/**
 * Represents a property of a robot. For some reason we can't store the DataType
 * enum directly, so we fake it here.
 * @author tim
 */
public class RobotProperty {
	
	@Id
	private Object internalId;
	
	private String id;
	private String parentId;
	private String name;
	private String value;
	private String fieldType;

	public RobotProperty() {
	}

	public RobotProperty(String parentId, String name, String value) {
		this.parentId = parentId;
		this.name = name;
		this.value = value;
		
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
}
