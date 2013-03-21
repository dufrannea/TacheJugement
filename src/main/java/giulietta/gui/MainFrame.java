package giulietta.gui;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.MailSenderImpl;
import giulietta.service.Player;
import giulietta.service.SafeSaver;
import giulietta.service.SafeSaverImpl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	private int index;
	private LiveSession session;
	private List<JCheckBox> checkBoxes;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024343263200792115L;
	private JPanel buttons;
	private JLabel topLabel;
	private Player player;
	private SafeSaver saver;
	private JButton next;

	public MainFrame(LiveSession session,Player player){
		super();
		this.player=player;
		if (session ==null) {
			index=0;
			this.session=new LiveSession("Paperina");
		} else{
			recoverSession(session);
		}
		preload();
		/*
		 * Layout and content
		 */
		setLookNFeel();
		this.setLayout(new BorderLayout());
		JPanel jpane = new JPanel();
		build(jpane);//On initialise notre fenêtre
		setContentPane(jpane);
		this.pack();
		this.repaint();
	}
	
	private void recoverSession(LiveSession toRecover) {
		this.session = toRecover;
		this.index = session.getAnswers().size() - 1;
	}
	
	private void answer(){
		ArrayList<Boolean> answers = new ArrayList<Boolean>();
		int npositive=0;
		for (JCheckBox c : checkBoxes){
			answers.add(c.isSelected());
			if (c.isSelected()){
				npositive +=1;
			}
		}

		boolean equal = false;

		if (session.getAnswers().size() -1 >= index ) {

			equal=true;
			
			for (int i : session.getAnswers().get(index).getAnswers()){
				System.out.println(i);
				System.out.println(answers.get(i-1));
				if (!answers.get(i-1)) {
					equal=false;
					System.out.println("break");
					break;
				}
			}
			
			if (session.getAnswers().get(index).getAnswers().size() != npositive){
				equal=false;
			}
		}
		if (equal) {
			return;
		}
		
		session.addAnswer(index, answers.toArray(new Boolean[0]));
		
		if (session.getAnswers().size() == scenario.getItems().size()){
			session.setFinished(true);
		}
		
		try {
			saver.save(session);
		} catch (IOException e) {
			System.err.println("Could not save session");
			e.printStackTrace();
		}
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
		scenario = player.loadStory();
		saver = new SafeSaverImpl(new MailSenderImpl());
	}

	private void build(JPanel truc){
		setTitle(Context.getProperty(Config.GIULIETTA_TITLE)); 
		setLocationRelativeTo(null); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		BoxLayout mainL = new BoxLayout(truc,BoxLayout.Y_AXIS);
		truc.setLayout(mainL);
		
		
		JPanel topPanel = new JPanel(new FlowLayout());
		topLabel = new JLabel();
		topPanel.add(topLabel);
		truc.add(topPanel);


		JPanel sentencePanel = new JPanel();
		sentencePanel.setLayout(new FlowLayout());
	
		label = new JLabel();
		label.setText(scenario.getItems().get(index).getPhrase());
		sentencePanel.add(label);
		rebuildLabels();
		sentencePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Context.getProperty(Config.GIULIETTA_SENTENCE_GROUP)),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		truc.add(sentencePanel);

		JPanel cb = new JPanel(new BorderLayout());

		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Context.getProperty(Config.GIULIETTA_SOUNDS_GROUP)),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		buildButtons(buttons);
		cb.add(buttons);

		truc.add(cb);



		JPanel bottom = new JPanel(new FlowLayout());

		JButton previous = new JButton(Context.getProperty(Config.GIULIETTA_PREVIOUS));
		previous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				previous();
				
			}

			
		});
		bottom.add(previous);

		next = new JButton(Context.getProperty(Config.GIULIETTA_NEXT));
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
				
			}

			
		});
		bottom.add(next);
		truc.add(bottom);

	}
	private void next() {
		answer();
		if (index < scenario.getItems().size() -1) {
			++index;
		} else {
			return ;
		}
		update();
	}
	private void previous() {
		answer();

		if (index > 0) {
			--index;
		} else {
			return ;
		}
		update();
	}
	
	private void rebuildLabels(){
		topLabel.setText(Context.getProperty(Config.GIULIETTA_TOP_LABEL)+ " " + (index+1) + " / "+scenario.getItems().size());
		label.setText(scenario.getItems().get(index).getPhrase());

	}
	private void update() {
		rebuildNextPrevious();
		rebuildButtons();
		rebuildLabels();
		reloadAnswer();
	}
	
	private void rebuildNextPrevious(){
		if (index == scenario.getItems().size() - 1 ){
			next.setText(Context.getProperty(Config.GIULIETTA_SAVE));
		} else {
			next.setText(Context.getProperty(Config.GIULIETTA_NEXT));
		}
	}

	private void reloadAnswer() {
		if (session.getAnswers().size()<index+1){
			return;
		}
		for (Integer ans : session.getAnswers().get(index).getAnswers()){
			checkBoxes.get(ans-1).setSelected(true);
		}
		
	}

	private void rebuildButtons() {
		for (Component c : buttons.getComponents()){
			buttons.remove(c);
		}
		buildButtons(buttons);
		
		pack();
		repaint();
	}

	private void buildButtons(JPanel buttons) {
		checkBoxes=new ArrayList<JCheckBox>();
		JPanel panel;
		BoxLayout layout;
		JCheckBox checkBox;
		PlayButton play;
		JPanel cbpanel;
		buttons.add(Box.createHorizontalGlue());

		for (String son: scenario.getItems().get(index).getSons()){
			panel=new JPanel();

			layout= new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(layout);

			play = getButton();
			play.setSoundPath(son);
			panel.add(play);


			checkBox= new JCheckBox();
			checkBoxes.add(checkBox);
			cbpanel=new JPanel();
			
			cbpanel.setLayout(new FlowLayout());
			cbpanel.add(checkBox);
			panel.add(cbpanel);


			buttons.add(panel);
			buttons.add(Box.createHorizontalGlue());


		}
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
