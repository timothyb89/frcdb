package net.frcdb.robot.year;

import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;

/**
 *
 * @author tim
 */
public class ReboundRobotTemplate extends YearTemplate {

	public ReboundRobotTemplate(Robot robot) {
		super(robot);
	}

	@Override
	public void apply() {
		RobotComponent rebound = createComponent("Rebound Rumble Specifics");
		createProperty(rebound, "Can balance on bridge?", "text");
		createProperty(rebound, "Can tip bridge?", "text");
		createProperty(rebound, "Which hoops? (top, all, etc)", "text");
		createProperty(rebound, "Maximum shooting distance? (feet)", "number");
	}
	
}
