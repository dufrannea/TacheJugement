package giulietta.service.impl;

import giulietta.model.Answer;
import giulietta.model.LiveSession;
import giulietta.model.Question;
import giulietta.service.api.Statistics;

import java.util.ArrayList;
import java.util.List;

public class StatisticsImpl implements Statistics {

	@Override
	public List<Question> getQuestionsList(List<LiveSession> sessions) {
		
		List<Question> questions = new ArrayList<Question>();
		
		Question question;
		for (LiveSession session : sessions) {
			for ( Answer  answer : session.getAnswers()) {
				
				if (answer.getIndex() < questions.size() ) {
					question = questions.get(answer.getIndex());
				} else {
					question = new Question();
					questions.add(question);
				}
				for (int rep : answer.getAnswers()){
					if (question.getAnswers().containsKey(rep)){
						question.getAnswers().put(rep, question.getAnswers().get(rep)+1);
					} else {
						question.getAnswers().put(rep, 1);
					}
				}

			}
			
		}
		
		for (Question q : questions ){
			System.out.println(q.getAnswers());
		}
		return questions;
	}


}
