package giulietta.config;

public enum Config {

	GIULIETTA_PROPERTIES_PATH("src/main/config/giulietta.properties"),
	GIULIETTA_SESSION_KEY("giulietta.scenario_file");
	
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
