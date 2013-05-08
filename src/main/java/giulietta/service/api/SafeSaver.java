package giulietta.service.api;

import giulietta.model.LiveSession;

import java.io.IOException;

public interface SafeSaver {

	void save(LiveSession session) throws IOException;

}
