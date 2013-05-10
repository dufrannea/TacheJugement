package giulietta.service.api;

import java.io.File;
import java.util.List;

import giulietta.model.LiveSession;
import giulietta.model.Scenario;

public interface Loader {

	LiveSession loadUnfinishedSession();
	

	Scenario loadScenario(File scenarioFile);


	LiveSession loadSession(File file);


	List<LiveSession> loadAllSessions();

}
