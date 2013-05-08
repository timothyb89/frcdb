package net.frcdb.eventmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import net.frcdb.eventmanager.api.Event;
import net.frcdb.eventmanager.api.EventEntry;
import net.frcdb.eventmanager.util.Dialog;

/**
 *
 * @author tim
 */
public class MergeOptionsPanel extends JPanel {

	private Editor editor;
	private EventEntry entry;
	private EventTableModel table;
	
	private JScrollPane scroll;
	private JPanel wrapper;
	
	private JTextField shortNameField;
	private JTextField frcdbNameField;

	public MergeOptionsPanel(Editor editor, EventEntry entry) {
		this.editor = editor;
		this.entry = entry;
		
		table = (EventTableModel) editor.getTable().getModel();
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		
		wrapper = new JPanel(new GridLayout(0, 1));
		
		String initialShortName = entry.getShortName() == null ?
				"" : entry.getShortName();
		
		addLabel("Edit Short Name");
		shortNameField = addComponent(new JTextField(initialShortName));
		addButton("Modify", modifyShortNameListener, shortNameField);
		
		addSpacer();
		
		addLabel("Set FRC-DB Event (shortName)");
		frcdbNameField = addComponent(new JTextField(initialShortName));
		addButton("Set & Re-Merge", setFrcdbEventListener, frcdbNameField);
		
		addSpacer();
		
		addButton("Search Event", searchEventListener);
		
		addSpacer();
		
		addButton("Prefer FIRST", preferFirstListener);
		addButton("Prefer FRC-DB", preferFrcdbListener);
		
		addSpacer();
		
		addButton("Clear FRC-DB Link", clearFrcdbListener);
		addButton("Delete", deleteListener);
		
		addSpacer();
		
		addLabel("FRC-DB: " + entry.getFrcdbEvent());
		addLabel("FIRST:  " + entry.getFirstEvent());
		
		add(wrapper, BorderLayout.NORTH);
	}
	
	private JLabel addLabel(String text) {
		JLabel l = new JLabel(text);
		l.setToolTipText(text);
		wrapper.add(l);
		
		return l;
	}
	
	private void addSpacer(int height) {
		Dimension d = new Dimension(0, height);
		wrapper.add(new Box.Filler(d, d, d));
	}
	
	private void addSpacer() {
		addSpacer(10);
	}
	
	private JButton addButton(String text, ActionListener l) {
		JButton button = new JButton(text);
		button.addActionListener(l);
		wrapper.add(button);
		
		return button;
	}
	
	private JButton addButton(String text, ActionListener l, JTextField field) {
		final JButton ret = addButton(text, l);
		
		field.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ret.doClick();
				}
			}
			
		});
		
		return ret;
	}
	
	private <T extends JComponent> T addComponent(T c) {
		wrapper.add(c);
		
		return c;
	}
	
	private ActionListener modifyShortNameListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (entry.getFrcdbEvent() != null) {
				Dialog.error(MergeOptionsPanel.this,
						"You can't modify the shortName of an existing frc-db "
						+ "event.");
				shortNameField.setText(entry.getFrcdbEvent().getShortName());
			} else {
				entry.setShortName(shortNameField.getText());
				table.update(entry);
			}
		}
		
	};
	
	private ActionListener setFrcdbEventListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			Event frcdbEvent = editor.getFrcdbEvent(frcdbNameField.getText());
			
			if (frcdbEvent == null) {
				Dialog.error(MergeOptionsPanel.this,
						"No frcdb event found: " + frcdbNameField.getText());
				return;
			}
			
			entry.setFrcdbEvent(frcdbEvent);
			entry.merge();
			table.update(entry);
		}
		
	};
	
	private ActionListener searchEventListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			Event v = new SearchDialog(
					editor,
					editor.getFrcdbEvents(),
					entry.getCandidate()).getSelection();
			
			if (v != null) {
				entry.setFrcdbEvent(v);
				entry.merge();
				table.update(entry);
				
				// show a new options panel
				// just make a new one because we're lazy
				editor.showOptionsPanel(entry);
			}
		}
		
	};
	
	private ActionListener preferFirstListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			entry.mergeFromFirst();
			table.update(entry);
		}
		
	};
	
	private ActionListener preferFrcdbListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			entry.mergeFromFrcdb();
			table.update(entry);
		}
		
	};
	
	private ActionListener clearFrcdbListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			entry.setFrcdbEvent(null);
			entry.merge();
			table.update(entry);
			
			editor.showOptionsPanel(entry);
		}
		
	};
	
	private ActionListener deleteListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			table.delete(entry);
			editor.clearOptionsPanel();
		}
		
	};
	
	
}
