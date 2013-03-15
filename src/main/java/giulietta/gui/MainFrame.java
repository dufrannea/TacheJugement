package giulietta.gui;

import giulietta.model.Scenario;
import giulietta.service.Player;
import giulietta.service.PlayerImpl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import sun.audio.AudioStream;

public class MainFrame extends JFrame {


	AudioStream currentStream;
	JLabel label;
	private Scenario scenario;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024343263200792115L;

	public MainFrame(){
		super();
		setLookNFeel();
		preload();
		build();//On initialise notre fenêtre
	}

	private void setLookNFeel() {
		try {
			UIManager.setLookAndFeel(
			            UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void preload(){
		Player player=new PlayerImpl();
		scenario = player.loadStory();
	}

	private void build(){
		setTitle("Tache 3"); //On donne un titre à l'application
		setSize(320,240); //On donne une taille à notre fenêtre
		setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
		//		setResizable(false); //On interdit la redimensionnement de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix

		BoxLayout mainL = new BoxLayout(getContentPane(),BoxLayout.Y_AXIS);
		this.setLayout(mainL);
		this.add(Box.createVerticalGlue());

		label = new JLabel();

		label.setText("sqlut");
		this.add(label);
		this.add(Box.createVerticalGlue());

		
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		
		buttons.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("sounds"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
		JPanel panel;
		
		BoxLayout layout;
		
		JCheckBox a ;
		PlayButton play ;
		for (String son: scenario.getItems().get(0).getSons()){
			panel=new JPanel();
			
			layout= new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(layout);
			
			buttons.add(Box.createRigidArea(new Dimension(10,0)));
			play = getButton();
			play.setSoundPath(son);
			a= new JCheckBox();

			panel.add(play);
			panel.add(a,Box.CENTER_ALIGNMENT);
			buttons.add(panel);	
		}
		buttons.add(Box.createVerticalGlue());

		this.add(buttons);
		this.add(Box.createVerticalGlue());
		
		JPanel bottom = new JPanel();
		JButton previous = new JButton();
		previous.setText("Previous");
		bottom.add(previous);
		bottom.add(Box.createHorizontalGlue());
		JButton next = new JButton();
		next.setText("next");
		bottom.add(next);
		this.add(bottom);
		

	}

	private static PlayButton getButton(){
		final PlayButton button = new PlayButton();
		button.setText("sound1");
		button.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						button.click();
					}
				}
				);
		return button;
	}
	private static class PlayButton extends JButton{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4802658780958947524L;

		private String soundPath;

		public PlayButton() {
			super();

		}

		public void setSoundPath(String soundPath){
			this.soundPath=soundPath;
		}

		private void click(){
			if (soundPath==null) return;
			this.setEnabled(false);
			URL url;
			try {
				url = new File(soundPath).toURI().toURL();
				Clip clip = AudioSystem.getClip();
				AudioInputStream ais = AudioSystem.
						getAudioInputStream( url );
				clip.open(ais);
				clip.loop(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.setEnabled(true);

		}


	}

}
