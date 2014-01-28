package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.Scenario;
import giulietta.service.Context;
import giulietta.service.api.InvalidScenarioException;
import giulietta.service.api.Loader;
import giulietta.service.api.Player;

import java.io.File;

public class PlayerImpl implements Player {

	private final Loader loader;

	public PlayerImpl(Loader loader) {
		super();
		this.loader = loader;
	}

	@Override
	public Scenario loadStory() throws InvalidScenarioException {
		return genericLoadStory(true);
	}

	private Scenario genericLoadStory(boolean check) throws InvalidScenarioException {
		String scenarioFile = Context.getProperty(Config.GIULIETTA_SCENARIO_FILE);

		return loader.loadScenario(new File(scenarioFile), check);

	}

	@Override
	public Scenario loadStorySilently() {

		try {
			return genericLoadStory(false);
		} catch (InvalidScenarioException e) {
			throw new RuntimeException(e);
		}
	}
}
