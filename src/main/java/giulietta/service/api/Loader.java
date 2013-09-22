package giulietta.service.api;

import java.io.File;
import java.util.List;

import giulietta.model.LiveSession;
import giulietta.model.Scenario;
import giulietta.service.impl.LoaderImpl.InvalidScenarioException;

public interface Loader {

	LiveSession loadUnfinishedSession();
	

	Scenario loadScenario(File scenarioFile,boolean check) throws InvalidScenarioException;


	LiveSession loadSession(File file);


	List<LiveSession> loadAllSessions();

}
