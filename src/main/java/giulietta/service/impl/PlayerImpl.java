package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.Loader;
import giulietta.service.api.Player;

import java.io.File;

public class PlayerImpl implements Player{

	private Loader loader;

	public PlayerImpl(Loader loader){
		super();
		this.loader=loader;
	}
	
	@Override
	public Scenario loadStory(){
		String scenarioFile= Context.getProperty(Config.GIULIETTA_SESSION_KEY);
		return loader.loadScenario(new File(scenarioFile));
	}
	
	
	
	

}
