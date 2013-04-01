package giulietta.gui;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.model.ManagedSound;
import giulietta.model.ManagedSoundListener;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.Player;
import giulietta.service.api.SafeSaver;
import giulietta.service.api.SoundPlayer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

	/*
	 * Serial.
	 */
	private static final long serialVersionUID = 1024343263200792115L;
	private int index;
	/*
	 * components.
	 */
	private JLabel label;
	private Scenario scenario;
	private LiveSession session;
	private List<JCheckBox> checkBoxes;	
	private List<JButton> playButtons;
	private JPanel buttonsJPanel;
	private JLabel topLabel;
	private JButton next;
	/*
	 * Services
	 */
	private final SafeSaver saver;
	private final SoundPlayer soundPlayer;

	/**
	 * Constructor. Inject deps.
	 * @param session start from existing of create new.
	 * @param player player service.
	 */
	public MainFrame(LiveSession session,Player player,SafeSaver saver,SoundPlayer soundPlayer){
		super();
		this.saver=saver;
		this.soundPlayer= soundPlayer;
		if (session ==null) {
			index=0;
			this.session=new LiveSession("Paperina");
		} else{
			recoverSession(session);
		}
		
		//load scenario.
		scenario = player.loadStory();

		/*
		 * Layout and content
		 */
		setLookNFeel();
		this.setLayout(new BorderLayout());
		JPanel jpane = new JPanel();
		build(jpane);//On initialise notre fenêtre
		setContentPane(jpane);
		reloadAnswer();
		this.pack();
		this.repaint();
	}
	
	/**
	 * Update state if already existing session.
	 * @param toRecover session.
	 */
	private void recoverSession(LiveSession toRecover) {
		this.session = toRecover;
		this.index = session.getAnswers().size() - 1;
	}

	
	/**
	 * Try to use system default lnf.
	 */
	private void setLookNFeel() {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * First build the window.
	 * @param truc the panel to build in.
	 */
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

		buttonsJPanel = new JPanel();
		buttonsJPanel.setLayout(new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS));
		buttonsJPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Context.getProperty(Config.GIULIETTA_SOUNDS_GROUP)),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		buildButtons(buttonsJPanel);
		cb.add(buttonsJPanel);

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
	
	/**
	 * Callback for Next button action listener.
	 */
	private void next() {
		answer();
		if (index < scenario.getItems().size() -1) {
			++index;
		} else {
			return ;
		}
		update();
	}
	
	/**
	 * Callback for Previous button action listener.
	 */
	private void previous() {
		answer();

		if (index > 0) {
			--index;
		} else {
			return ;
		}
		update();
	}

	/**
	 * Answer mechanism.
	 * 
	 */
	private void answer(){
		/*
		 * Build answer.
		 */
		ArrayList<Boolean> answers = new ArrayList<Boolean>();
		int npositive=0;
		for (JCheckBox c : checkBoxes){
			answers.add(c.isSelected());
			if (c.isSelected()){
				npositive +=1;
			}
		}

		/*
		 * Check if answer different from existing one (if existing).
		 */
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
		//answer existing and identical, return.
		if (equal) {
			return;
		}

		/*
		 * Save answer.
		 */
		session.addAnswer(index, answers.toArray(new Boolean[0]));

		//flag session as finished if necessary.
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

	
	/**
	 * Rebuild the whole state.
	 */
	private void update() {
		rebuildNextPrevious();
		rebuildButtons();
		rebuildLabels();
		reloadAnswer();
	}
	
	private void rebuildLabels(){
		topLabel.setText(Context.getProperty(Config.GIULIETTA_TOP_LABEL)+ " " + (index+1) + " / "+scenario.getItems().size());
		label.setText(scenario.getItems().get(index).getPhrase());
		
	}

	/**
	 * Update Next and Previous labels on buttons.
	 */
	private void rebuildNextPrevious(){
		if (index == scenario.getItems().size() - 1 ){
			next.setText(Context.getProperty(Config.GIULIETTA_SAVE));
		} else {
			next.setText(Context.getProperty(Config.GIULIETTA_NEXT));
		}
	}

	/**
	 * Set values of checkBoxes with respect to the current answer.
	 */
	private void reloadAnswer() {
		if (session.getAnswers().size()<index+1){
			return;
		}
		for (Integer ans : session.getAnswers().get(index).getAnswers()){
			checkBoxes.get(ans-1).setSelected(true);
		}

	}

	/**
	 * Destroy and instanciate buttons.
	 */
	private void rebuildButtons() {
		for (Component c : buttonsJPanel.getComponents()){
			buttonsJPanel.remove(c);
		}
		buildButtons(buttonsJPanel);

		pack();
		repaint();
	}

	/**
	 * Instanciate all needed buttons.
	 * @param buttons the Panel the buttons will be created in.
	 */
	private void buildButtons(JPanel buttons) {
		checkBoxes=new ArrayList<JCheckBox>();
		playButtons = new ArrayList<JButton>();
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
			playButtons.add(play);
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
	
	/**
	 * Get a new PlayButton.
	 * @return a new Button.
	 */
	private  PlayButton getButton(){
		final PlayButton button = new PlayButton();
		button.setText("sound1");
		button.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								setButtonsEnabled(false);
								
							}
						});
						button.click();
					}
				}
		);
		return button;
	}
	
	
	private void setButtonsEnabled(boolean enabled){
		for (JButton button : playButtons){
			button.setEnabled(enabled);
		}
	}
	/**
	 * Button able to play sound.
	 * @author arno
	 *
	 */
	private class PlayButton extends JButton{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4802658780958947524L;

		private String soundPath;

		/**
		 * contructor, private.
		 */
		private PlayButton() {
			super();

		}

		/**
		 * Set the path to the sound.
		 * @param soundPath the path.
		 */
		public void setSoundPath(String soundPath){
			this.soundPath=soundPath;
		}
		
		/**
		 * Callback when click on button, plays sound.
		 */
		private void click(){
			if (soundPath==null) return;
			InputStream inputStream;
			try {
				inputStream = new FileInputStream( new File(soundPath));
				ManagedSound sound = soundPlayer.playSound(inputStream);
				sound.setListener(new ManagedSoundListener() {
					
					@Override
					public void onStart() {
						
					}
					
					@Override
					public void onEnd() {
						System.err.println("finished");
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								setButtonsEnabled(true);						
								
							}
						});
					}
				});
				sound.play();

			} catch (Exception e) {
				e.printStackTrace();
			}
			this.setEnabled(true);

		}


	}

}
