package net.frcdb.robot.year;

import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;

/**
 *
 * @author tim
 */
public class DefaultRobotTemplate extends YearTemplate {

	public DefaultRobotTemplate(Robot robot) {
		super(robot);
	}

	@Override
	public void apply() {
		RobotComponent general = createComponent("General Properties");
		createProperty(general, "Control type? (normal, xbox, custom, etc)", "text");
		createProperty(general, "Drive type? (tank, arcade, etc)", "text");
		createProperty(general, "Uses CAN?", "text");
		createProperty(general, "Programming language? (C++, Java, LabVIEW)", "text");
		createProperty(general, "Width? (inches)", "number");
		createProperty(general, "Depth? (inches)", "number");
		createProperty(general, "Height? (inches)", "number");
		createProperty(general, "Weight? (pounds)", "number");
	}
	
}
