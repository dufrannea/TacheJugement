package giulietta.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class LiveSession {

	private String person;
	private List<Answer> answers;
	private Date startDate;
	private boolean finished=false;
	
	/**
	 * @return the finished
	 */
	public final boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public final void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * @return the startDate
	 */
	public final Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public final void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public LiveSession(String person){
		this.person=person;
		this.answers=new ArrayList<Answer>();
		this.startDate= new Date();
	}
	
	public Answer addAnswer(Integer index,Boolean... answers){
		Answer ans = new Answer(index, answers);
		if (this.answers.size()>=index+1){ 
			this.answers.set(index, ans);
		}else{
			this.answers.add(ans);
		}
		return ans;
	}
	/**
	 * @return the person
	 */
	public final String getPerson() {
		return person;
	}
	/**
	 * @param person the person to set
	 */
	public final void setPerson(String person) {
		this.person = person;
	}
	/**
	 * @return the answers
	 */
	public final List<Answer> getAnswers() {
		return answers;
	}
	/**
	 * @param answers the answers to set
	 */
	public final void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
}
