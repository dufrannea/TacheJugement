package giulietta.service.api;

import giulietta.model.Scenario;

public interface Player {

	public Scenario loadStory() throws InvalidScenarioException;

	public Scenario loadStorySilently();

}
