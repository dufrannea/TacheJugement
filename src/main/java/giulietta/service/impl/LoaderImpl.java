package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.Loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	public Scenario loadScenario(File scenarioFile){
		return loadYamlFile(scenarioFile, Scenario.class);
	}
	
	private  <D> D loadYamlFile(File file,Class<D> clazz){
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
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
