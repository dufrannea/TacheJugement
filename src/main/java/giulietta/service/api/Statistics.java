package giulietta.service.api;

import giulietta.model.LiveSession;
import giulietta.model.Question;

import java.util.List;

public interface Statistics {

	public List<Question> getQuestionsList( List<LiveSession> sessions);
}
