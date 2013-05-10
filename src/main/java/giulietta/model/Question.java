package giulietta.model;

import java.util.HashMap;
import java.util.Map;

public class Question {

	private int index;
	
	private Map<Integer,Integer> answers;

	
	public Question() {
		super();
		answers=new HashMap<Integer, Integer>();
	}

	/**
	 * @return the index
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public final void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the answers
	 */
	public final Map<Integer, Integer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public final void setAnswers(Map<Integer, Integer> answers) {
		this.answers = answers;
	}
	
}
