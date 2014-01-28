package main;

import giulietta.config.Config;
import giulietta.gui.MainFrame;
import giulietta.model.LiveSession;
import giulietta.service.Context;
import giulietta.service.api.Loader;
import giulietta.service.api.Player;
import giulietta.service.api.SafeSaver;
import giulietta.service.api.SoundPlayer;
import giulietta.service.impl.LoaderImpl;
import giulietta.service.impl.MailSenderImpl;
import giulietta.service.impl.PlayerImpl;
import giulietta.service.impl.SafeSaverImpl;
import giulietta.service.impl.SoundPlayerImpl;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApplication {

	final Player player;
	final Loader loader;
	final SafeSaver saver;
	final SoundPlayer soundPlayer;

	/**
	 * Start point.
	 */
	public MainApplication() {
		loader = new LoaderImpl();
		player = new PlayerImpl(loader);
		saver = new SafeSaverImpl(new MailSenderImpl());
		soundPlayer = new SoundPlayerImpl();

	}

	/**
	 * Try to use system default lnf.
	 */
	private void setLnf() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {
		setLnf();

		LiveSession previousSession = loader.loadUnfinishedSession();
		LiveSession currentSession = null;
		int yesno = 1;
		if (previousSession != null) {
			yesno = JOptionPane.showConfirmDialog(null, Context.getProperty(Config.GIULIETTA_RIPRISTINA_1) + " " + previousSession.getPerson() + Context.getProperty(Config.GIULIETTA_RIPRISTINA_2), Context.getProperty(Config.GIULIETTA_RIPRISTINA_TITLE), JOptionPane.YES_NO_OPTION);
		}
		if (yesno == 0) {
			currentSession = previousSession;
		} else {
			String answer = null;
			while (answer == null || answer.length() == 0) {
				answer = JOptionPane.showInputDialog(null, Context.getProperty(Config.GIULIETTA_INPUT_NAME), Context.getProperty(Config.GIULIETTA_INPUT_NAME_TITLE), JOptionPane.QUESTION_MESSAGE);
				if (answer == null) {
					return;
				}
			}
			currentSession = new LiveSession(answer);
		}
		startUI(currentSession);
	}

	private void startUI(final LiveSession session) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				MainFrame window = new MainFrame(session, player, saver, soundPlayer);
				window.pack();
				window.setMinimumSize(window.getSize());
				window.setVisible(true);
			}
		});
	}

}
