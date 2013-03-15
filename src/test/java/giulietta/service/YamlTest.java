package giulietta.service;

import giulietta.config.Config;
import giulietta.model.Scenario;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;



public class YamlTest {

	@Test
	public void loadStory() throws IOException{

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
	public void loadSessions(){
		FileReader configFile= Context.getFileReader(Config.GIULIETTA_SESSION_KEY.getValue());
		Yaml yaml=new Yaml(new Constructor(Scenario.class));
		yaml.load(configFile);
	}

}
