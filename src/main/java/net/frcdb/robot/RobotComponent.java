package net.frcdb.robot;

import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class RobotComponent implements ContentProvider {
	
	@Id
	private Long internalId;
	
	private String id;
	private String robotId;
	
	private String name;
	
	private List<Content> content;
	
	public RobotComponent() {
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		
		content = new ArrayList<Content>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRobotId() {
		return robotId;
	}

	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RobotProperty> getProperties() {
		return Database.getInstance().getRobotProperties(this);
	}

	public RobotProperty getProperty(String name) {
		return Database.getInstance().getRobotProperty(id, name);
	}
	
	@Override
	public Object getRecordId() {
		return internalId;
	}
	
	@Override
	public String getContentType() {
		return "RobotComponent";
	}

	@Override
	public List<Content> getContent() {
		return content;
	}

	@Override
	public void addContent(Content c) {
		content.add(c);
	}

	@Override
	public void removeContent(Content c) {
		content.remove(c);
	}
	
}
