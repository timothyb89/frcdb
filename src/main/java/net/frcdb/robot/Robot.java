package net.frcdb.robot;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frcdb.api.team.Team;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class Robot implements ContentProvider {
	
	@Id
	private Long internalId;
	
	private String id;
	private Ref<Team> team;
	private int year;
	
	private List<Content> content;

	public Robot() {
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		
		content = new ArrayList<Content>();
	}
	
	public Robot(Team team, int year) {
		this.team = Ref.create(team);
		this.year = year;
		
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

	public Ref<Team> getTeam() {
		return team;
	}

	public void setTeam(Ref<Team> team) {
		this.team = team;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public List<RobotComponent> getComponents() {
		return Database.getInstance().getComponents(id);
	}
	
	public RobotComponent getComponent(String name) {
		return Database.getInstance().getComponent(id, name);
	}

	@Override
	public Object getRecordId() {
		return internalId;
	}
	
	@Override
	public String getContentType() {
		return "Robot";
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
