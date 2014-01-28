package giulietta.service.api;

import giulietta.model.LiveSession;
import giulietta.model.Scenario;

import java.io.File;
import java.util.List;

public interface Loader {

	LiveSession loadUnfinishedSession();

	Scenario loadScenario(File scenarioFile, boolean check) throws InvalidScenarioException;

	LiveSession loadSession(File file);

	List<LiveSession> loadAllSessions();

}
