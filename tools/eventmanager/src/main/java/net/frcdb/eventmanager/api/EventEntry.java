package net.frcdb.eventmanager.api;

import lombok.Data;
import lombok.Delegate;
import net.frcdb.eventmanager.Editor;

/**
 *
 * @author tim
 */
@Data
public class EventEntry {
	
	@Delegate
	private Event candidate;
	
	private Event frcdbEvent;
	private Event firstEvent;
	
	private boolean newGame;

	public EventEntry(Event frcdbEvent, Event firstEvent) {
		this.frcdbEvent = frcdbEvent;
		this.firstEvent = firstEvent;
		
		if (frcdbEvent != null) {
			if (frcdbEvent.getYears().contains(Editor.getSelectedYear())) {
				// this year has already been imported, so skip by default
				newGame = false;
			} else {
				newGame = true;
			}
		} else {
			newGame = true;
		}
		
		merge();
	}
	
	/**
	 * Merges the data between the frcdb and FIRST events somewhat
	 * intelligently.
	 * @return the merged event 
	 */
	public Event merge() {
		return mergeFromFirst();
	}
	
	/**
	 * Merge the two events, but prefer FIRST data.
	 * @return 
	 */
	public Event mergeFromFirst() {
		// fresh event
		candidate = new Event();
		
		// start with frcdb data
		if (frcdbEvent != null) {
			candidate.copyFrom(frcdbEvent);
		}
		
		// override it with first values
		if (firstEvent != null) {
			candidate.softCopyFrom(firstEvent);
		}
		
		return candidate;
	}
	
	/**
	 * Merge the events, preferring FRC-DB data.
	 * @return 
	 */
	public Event mergeFromFrcdb() {
		// fresh event
		candidate = new Event();
		
		// start with FIRST data
		candidate.copyFrom(firstEvent);
		
		// override it with FRC-DB values
		if (frcdbEvent != null) {
			candidate.softCopyFrom(frcdbEvent);
		}
		
		return candidate;
	}
	
	public boolean isModified() {
		if (frcdbEvent == null) {
			return true;
		}
		
		return candidate.isDifferent(frcdbEvent);
	}
	
}
