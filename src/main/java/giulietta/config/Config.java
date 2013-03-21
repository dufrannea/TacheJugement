package giulietta.config;

public enum Config {

	GIULIETTA_PROPERTIES_PATH("giulietta.properties"),
	GIULIETTA_SESSIONS_DIR("giulietta.sessions_dir"),
	GIULIETTA_TEMP_DIR("giulietta.temp_dir"),
	GIULIETTA_NEXT("giulietta.next"),
	GIULIETTA_PREVIOUS("giulietta.previous"),
	GIULIETTA_SAVE("giulietta.save"),
	GIULIETTA_TITLE("giulietta.title"),
	GIULIETTA_SESSION_KEY("giulietta.scenario_file"),
	GIULIETTA_TOP_LABEL("giulietta.top_label"),
	GIULIETTA_SENTENCE_GROUP("giulietta.sentence.group"),
	GIULIETTA_SOUNDS_GROUP("giulietta.sounds.group");
	
	private String value;
	
	Config(String value){
		this.setValue(value);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
