package giulietta.model;

import java.util.List;

public class Item {

	private String phrase;
	private List<String> sons;
	private List<Integer> reponses;

	/**
	 * @return the phrase
	 */
	public final String getPhrase() {
		return phrase;
	}

	/**
	 * @param phrase
	 *            the phrase to set
	 */
	public final void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	/**
	 * @return the sons
	 */
	public final List<String> getSons() {
		return sons;
	}

	/**
	 * @param sons
	 *            the sons to set
	 */
	public final void setSons(List<String> sons) {
		this.sons = sons;
	}

	/**
	 * @return the reponses
	 */
	public final List<Integer> getReponses() {
		return reponses;
	}

	/**
	 * @param reponses
	 *            the reponses to set
	 */
	public final void setReponses(List<Integer> reponses) {
		this.reponses = reponses;
	}

}
