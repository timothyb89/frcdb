package net.frcdb.servlet.bean;

import java.util.List;
import net.frcdb.api.team.Team;
import net.frcdb.robot.Robot;

/**
 *
 * @author tim
 */
public class RobotData {
	
	private Team team;
	private Robot robot;
	private List<Integer> years;

	public RobotData() {
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}
	
}
