package giulietta.service.impl;

import giulietta.model.ManagedSound;
import giulietta.service.api.SoundPlayer;

import java.io.InputStream;

/**
 * Play sounds.
 * 
 * @author arno
 *
 */
public class SoundPlayerImpl implements SoundPlayer {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedSound playSound(InputStream s) {
		return new ManagedSound(s);
	}

}
