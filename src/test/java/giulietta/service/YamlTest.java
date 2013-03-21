package giulietta.service;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.model.Scenario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;



public class YamlTest {
	static String USER_TEST = "testPaperina";

	@Test
	public void loadStory() throws IOException {
		Player player=new PlayerImpl();
		Scenario scenario = player.loadStory();
		System.out.println(scenario.getItems().get(0).getPhrase());
	}

	@Test
	public void testYAML() {
		FileReader configFile= Context.getFileReader(Config.GIULIETTA_SESSION_KEY.getValue());
		Yaml yaml=new Yaml(new Constructor(Scenario.class));
		yaml.load(configFile);
	}

	@Test
	public void loadScenario() {
		FileReader configFile= Context.getFileReader(Config.GIULIETTA_SESSION_KEY.getValue());
		Yaml yaml=new Yaml(new Constructor(Scenario.class));
		yaml.load(configFile);
	}

	@Test
	public void saveYAMLSession() throws IOException {
		String path = Context.getProperty(Config.GIULIETTA_SESSIONS_DIR);
		FileWriter writer = new FileWriter(new File(path+File.separator+USER_TEST));

		Yaml yaml=new Yaml(new Constructor(LiveSession.class));

		LiveSession session = getLiveSession(USER_TEST);

		System.out.println(yaml.dump(session));
		yaml.dump(session, writer);

	}

	private LiveSession getLiveSession(String USER_TEST) {
		LiveSession session= new LiveSession(USER_TEST);
		session.addAnswer(0, true,false,false);
		session.addAnswer(1, true,true,false);
		session.addAnswer(2, true,true,true);
		return session;
	}
	@Test
	public void saveTempSession() throws IOException {
		SafeSaver saver = new SafeSaverImpl(null);
		
		LiveSession session = getLiveSession(USER_TEST);
		
		saver.save(session);
	}
	
	@Test
	public void saveFinishedSession() throws IOException {
		SafeSaver saver = new SafeSaverImpl(null);
		
		LiveSession session = getLiveSession(USER_TEST);
		session.addAnswer(3, false,false,false);
		session.setFinished(true);
		
		saver.save(session);
	}
	
	
	
	@Test
	public void loadSessions() throws FileNotFoundException {
		
		String sessionsDirPath= Context.getProperty(Config.GIULIETTA_SESSIONS_DIR);
		
		File sessionsDir = new File(sessionsDirPath);
		Yaml yaml = new Yaml();
		
		if (sessionsDir.exists() && sessionsDir.isDirectory()) {
			for (String file : sessionsDir.list()){
				File zeFile = new File(file);
				if (zeFile.isFile()){
					yaml.load(new FileReader(zeFile));
				}
			}
		} else {
			throw new RuntimeException("Non existing session dir " + sessionsDir);

		}
	}

}
