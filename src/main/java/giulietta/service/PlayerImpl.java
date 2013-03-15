package giulietta.service;

import giulietta.config.Config;
import giulietta.model.Scenario;

import java.io.FileReader;
import java.io.IOException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class PlayerImpl implements Player{


	@Override
	public Scenario loadStory(){
		FileReader configFile= Context.getFileReader(Config.GIULIETTA_SESSION_KEY.getValue());
		Yaml yaml=new Yaml(new Constructor(Scenario.class));
		Scenario a= (Scenario) yaml.load(configFile);

		try {
			configFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("done");
		return a;
	}
	
	

}
