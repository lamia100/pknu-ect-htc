package gui;

import java.awt.Dimension;

import javax.swing.WindowConstants;

public class Main {
	public static void main(String[] args) {
		GUI testGUI = new GUI();
		
		testGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension dim = testGUI.getToolkit().getScreenSize();
		testGUI.setLocation(dim.width / 2 - testGUI.getWidth() / 2, dim.height / 2 - testGUI.getHeight() / 2);
		
		testGUI.setVisible(true);
	}
}