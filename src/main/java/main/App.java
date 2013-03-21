package main;

import giulietta.gui.MainFrame;
import giulietta.service.Player;
import giulietta.service.PlayerImpl;

import javax.swing.SwingUtilities;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final Player player = new PlayerImpl();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame window = new MainFrame(null,player); 
				window.pack();
				window.setMinimumSize(window.getSize());
				window.setVisible(true);
			}
		});
	}
	
	

}
