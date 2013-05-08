package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.service.Context;
import giulietta.service.api.MailSender;
import giulietta.service.api.SafeSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.yaml.snakeyaml.Yaml;

public class SafeSaverImpl implements SafeSaver {

	private MailSender sender;
	
	public SafeSaverImpl(MailSender sender) {
		super();
		this.sender= sender;
	}
	
	private static String TEMP_SUFFIX = "_TEMP";
	
	@Override
	public void save(LiveSession session) throws IOException {
		if (session == null ){
			throw new IllegalArgumentException("Cannot save null session");
		}
		if (!session.isFinished()){
			saveTempSession(session);
			return;
		} 
		saveFinishedSession(session);
		
	}

	private void saveTempSession(LiveSession session) throws IOException {
		File file = new File(Context.getProperty(Config.GIULIETTA_TEMP_DIR)+File.separator+session.getPerson()+TEMP_SUFFIX);
		File file2 = new File(Context.getProperty(Config.GIULIETTA_TEMP_DIR)+File.separator+session.getPerson()+TEMP_SUFFIX+"_2");

		saveSession(session,file2);
		file.delete();
		file2.renameTo(file);
	}
	
	private void notifyViaMail(LiveSession session, File file) {
		if (sender != null) {
			sender.sendMessage("dufrannea@gmail.com", file);
		}
	}
	private void saveFinishedSession(final LiveSession session) throws IOException {
		final File file = new File(Context.getProperty(Config.GIULIETTA_SESSIONS_DIR)+File.separator+session.getPerson());
		try {
			saveSession(session,file);
		} catch (IOException e) {
			throw new IOException("Could not save session, keeping temp files");
		}
		deleteTempSession(session);
		try {
			//TODO : make copy of file in temp for safe upload.
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					notifyViaMail(session, file);
				}
			});
			t.start();
		} catch (Exception e) {
			System.err.println("could not send mail");
		}
	}
	
	private void deleteTempSession(LiveSession session) {
		File file = new File(Context.getProperty(Config.GIULIETTA_TEMP_DIR)+File.separator+session.getPerson()+TEMP_SUFFIX);
		if (file.exists() && file.canWrite()) {
			file.delete();
		}
	}


	private void saveSession(LiveSession session, File file) throws IOException  {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			//TODO change YAML to generic saver.
			Yaml yaml = new Yaml();
			writer.write(yaml.dump(session));
			writer.close();
		} catch (IOException e) {
			if (writer != null) {
				writer.close();
			}
			throw e;
		}
		
		
		
	}
	
	
		
		
	
	

}
