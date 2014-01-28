package giulietta.service.api;

import java.util.List;

public class InvalidScenarioException extends Exception {

	private static final long serialVersionUID = -4241515847625347198L;
	private List<String> missingFiles;

	public InvalidScenarioException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidScenarioException(String message) {
		super(message);
	}

	public InvalidScenarioException(String message, List<String> missingFiles) {
		this(message);
		this.missingFiles = missingFiles;
	}

	public List<String> getMissingFiles() {
		return missingFiles;
	}

	public Object formatMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getMessage());

		if (missingFiles != null) {
			sb.append("\n");
			sb.append("Missing Files are : \n");
			for (String missingFile : missingFiles) {
				sb.append(missingFile);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}