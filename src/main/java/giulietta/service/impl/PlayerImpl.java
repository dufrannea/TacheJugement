package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.Loader;
import giulietta.service.api.Player;
import giulietta.service.impl.LoaderImpl.InvalidScenarioException;

import java.io.File;

public class PlayerImpl implements Player{

	private Loader loader;

	public PlayerImpl(Loader loader){
		super();
		this.loader=loader;
	}
	
	@Override
	public Scenario loadStory(){
		return genericLoadStory(true);
	}

	private Scenario genericLoadStory(boolean check) {
		String scenarioFile= Context.getProperty(Config.GIULIETTA_SCENARIO_FILE);
		try {
			return loader.loadScenario(new File(scenarioFile),check);
		} catch (InvalidScenarioException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Scenario loadStorySilently() {
		return genericLoadStory(false);
	}
	
	
	
	
	

}
