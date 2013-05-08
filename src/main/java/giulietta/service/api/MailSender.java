package giulietta.service.api;

import java.io.File;

public interface MailSender {


	void sendMessage(String to, File filePath);

	

}
