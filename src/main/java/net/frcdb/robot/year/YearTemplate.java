package net.frcdb.robot.year;

import java.util.List;
import net.frcdb.db.Database;
import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;
import net.frcdb.robot.RobotProperty;

/**
 *
 * @author tim
 */
public abstract class YearTemplate {

	private Robot robot;
	
	public YearTemplate(Robot robot) {
		this.robot = robot;
	}
	
	public abstract void apply();
	
	protected RobotComponent createComponent(String name) {
		RobotComponent ret = new RobotComponent();
		ret.setName(name);
		
		ret.setRobotId(robot.getId());
		
		Database.getInstance().store(ret);
		
		return ret;
	}
	
	protected void createProperty(RobotComponent component, String name, String type) {
		RobotProperty ret = new RobotProperty();
		ret.setParentId(component.getId());
		ret.setName(name);
		ret.setFieldType(type);
		
		Database.getInstance().store(ret);
	}
	
	public static YearTemplate getTemplate(int year, Robot robot) {
		switch (year) {
			case 2012:
				return new ReboundRobotTemplate(robot);
			default:
				return null;
		}
	}
	
}
