package net.frcdb.eventmanager;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.frcdb.eventmanager.api.EventEntry;

/**
 *
 * @author tim
 */
public class EventTableModel extends AbstractTableModel {

	private List<EventEntry> events;

	private String[] columnNames = {
		"Name",
		"Short Name",
		"Identifier",
		"Venue",
		"City",
		"State",
		"Country",
		"New Game"
	};
	
	private boolean[] columnsEditable = {
		true,
		false,
		true,
		true,
		true,
		true,
		true,
		true
	};
	
	private Class[] columnClasses = {
		String.class,
		String.class,
		String.class,
		String.class,
		String.class,
		String.class,
		String.class,
		Boolean.class
	};
	
	public EventTableModel(List<EventEntry> events) {
		this.events = events;
	}
	
	public int getRowCount() {
		return events.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnsEditable[columnIndex];
	}

	@Override
	public void setValueAt(Object v, int rowIndex, int columnIndex) {
		EventEntry e = events.get(rowIndex);
		String value = String.valueOf(v);
		
		switch (columnIndex) {
			case 0: e.setName(value); break;
			case 2: e.setIdentifier(value); break;
			case 3: e.setVenue(value); break;
			case 4: e.setCity(value); break;
			case 5: e.setState(value); break;
			case 6: e.setCountry(value); break;
			case 7: e.setNewGame((Boolean) v); break;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		EventEntry e = events.get(rowIndex);
		
		switch (columnIndex) {
			case 0: return e.getName();
			case 1: return e.getShortName();
			case 2: return e.getIdentifier();
			case 3: return e.getVenue();
			case 4: return e.getCity();
			case 5: return e.getState();
			case 6: return e.getCountry();
			case 7: return e.isNewGame();
			default: return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}
	
	public void update(EventEntry e) {
		int index = events.indexOf(e);
		fireTableRowsUpdated(index, index);
	}
	
	public void delete(EventEntry e) {
		int index = events.indexOf(e);
		events.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	public void add(EventEntry e) {
		int index = events.size();
		events.add(e);
		fireTableRowsInserted(index, index);
	}
	
}
