package net.frcdb.eventmanager.api;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * Basic information for games from the FIRST data
 * @author tim
 */
@Data
@ToString
public class Game {
	
	private int eid;
	private Date startDate;
	private Date endDate;
	
}
