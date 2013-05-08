package net.frcdb.eventmanager.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author tim
 */
public class Dialog {
	
	public static void error(Component parent, String title, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static void error(Component parent, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static void warning(Component parent, String title, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, title,
				JOptionPane.WARNING_MESSAGE);
	}
	
	public static void warning(Component parent, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}
	
	public static void info(Component parent, String title, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, title,
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void info(Component parent, String text) {
		JOptionPane.showMessageDialog(
				parent,
				text, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
}
