package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.Item;
import giulietta.model.LiveSession;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;


public class LoaderImpl implements Loader{

	@Override
	public LiveSession loadUnfinishedSession(){
		File file = new File(Context.getProperty(Config.GIULIETTA_TEMP_DIR));
		if (file.exists() && file.canRead()){
			for (String tempSession : file.list()){
				return loadSession(new File(file.getAbsolutePath()+File.separator+tempSession));
			}
		}
		return null;
	}
	
	@Override
	public LiveSession loadSession(File file){
		return loadYamlFile(file, LiveSession.class);
	}
	
	@Override
	public List<LiveSession> loadAllSessions() {
		File file = new File(Context.getProperty(Config.GIULIETTA_SESSIONS_DIR));
		List<LiveSession> sessions = new ArrayList<LiveSession>();
		if (file.exists() && file.canRead()){
			for (String tempSession : file.list()){
				LiveSession session = loadSession(new File(file.getAbsolutePath()+File.separator+tempSession));
				if (session.isFinished()){
					sessions.add(session);
				}
			}
		}
		return sessions;
	}
	
	@Override
	public Scenario loadScenario(File scenarioFile,boolean check) throws InvalidScenarioException{
		Scenario scenario =  loadYamlFile(scenarioFile, Scenario.class);
		if (!check){
			return scenario;
		}
		try {
			checkScenario(scenario);
			return scenario;
		} catch (InvalidScenarioException e) {
			throw new InvalidScenarioException("Cannot read "+scenarioFile.getName() + " because some sounds are missing.");
		}
		
	}
	public class InvalidScenarioException extends Exception{

		public InvalidScenarioException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public InvalidScenarioException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	private boolean checkScenario(Scenario scenario) throws InvalidScenarioException{
		File checkFile;
		for (Item item : scenario.getItems())
			for (String sound : item.getSons()){
				checkFile = new File(sound);
				if (!checkFile.exists() || !checkFile.isFile() || !checkFile.canRead()){
					throw new InvalidScenarioException(" File "+ checkFile + " cannot be read ");
				}
			}
		return true;
	}
	
	private  <D> D loadYamlFile(File file,Class<D> clazz){
		InputStreamReader reader = null;
		try {
			FileInputStream stream = new FileInputStream(file);
			String encoding = Context.getProperty(Config.GIULIETTA_SCENARIO_ENCODING,"ISO-8859-1");
			reader = new InputStreamReader(stream,encoding);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Yaml yaml=new Yaml(new Constructor(clazz));
		D load = (D) yaml.loadAs(reader, clazz);
		D a= load;
		try {
			reader.close();
		} catch (IOException e){
			
		}
		return a;
		
		
	}
	
}
