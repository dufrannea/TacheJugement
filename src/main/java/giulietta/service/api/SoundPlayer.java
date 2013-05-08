package giulietta.service.api;

import giulietta.model.ManagedSound;

import java.io.InputStream;

/**
 * Play sound.
 * @author arno
 *
 */
public interface SoundPlayer {

	/**
	 * Plays an inputstream as a wav file.
	 * 
	 * @param s
	 * @return 
	 */
	public ManagedSound playSound(InputStream s);

//	void playSound(InputStream s, Observer o);
}
