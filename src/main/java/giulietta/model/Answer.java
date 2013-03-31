package giulietta.model;

import java.util.ArrayList;
import java.util.List;

public class Answer {

	private Integer index;
	private List<Integer> answers;
	
	public Answer(Integer index, Boolean... answers){
		this.index=index;
		this.answers=new ArrayList<Integer>();
		int i = 1;
		for (Boolean bool : answers){
			if (bool){
				this.answers.add(i);
			}
			++i;
		}
	}
	
	public Answer() {
		super();
	}

	/**
	 * @return the index
	 */
	public final Integer getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public final void setIndex(Integer index) {
		this.index = index;
	}
	/**
	 * @return the answers
	 */
	public final List<Integer> getAnswers() {
		return answers;
	}
	/**
	 * @param answers the answers to set
	 */
	public final void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}

}
