package main;

import giulietta.gui.MainFrame;

import javax.swing.SwingUtilities;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame window = new MainFrame(); 
				window.pack();
				window.setMinimumSize(window.getSize());
				window.setVisible(true);
			}
		});
	}
	
	

}
