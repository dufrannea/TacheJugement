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
	GIULIETTA_SOUNDS_GROUP("giulietta.sounds.group"),
	GIULIETTA_RIPRISTINA_1("giulietta.startup.ripristina1"),
	GIULIETTA_RIPRISTINA_2("giulietta.startup.ripristina2"),
	GIULIETTA_INPUT_NAME("giulietta.startup.inputname"), 
	GIULIETTA_RIPRISTINA_TITLE("giulietta.startup.ripristina_title"),
	GIULIETTA_INPUT_NAME_TITLE("giulietta.startup.inputname.title"),
	GIULIETTA_PULSANTE_SUONO("giulietta.sounds.pulsante_suono"), 
	ENABLE_PREVIOUS_ACTION("giulietta.enable.go.back"),
	GIULIETTA_EXPORT_DIR("giulietta.export.dir");


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
