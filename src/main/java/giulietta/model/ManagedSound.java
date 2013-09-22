package giulietta.model;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ManagedSound {


	private ManagedSoundListener listener;

	private InputStream s;

	public ManagedSound(InputStream s){
		this(s,null);
	}

	public ManagedSound(InputStream s, ManagedSoundListener listener){
		this.s= s;
		this.listener=listener;
	}
	public void play(){
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.
					getAudioInputStream(new BufferedInputStream(s));
			clip.open(ais);
			if (this.listener != null){
				addListeners(clip);
			}

			clip.loop(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param listener the listener to set
	 */
	public final void setListener(ManagedSoundListener listener) {
		this.listener = listener;
	}

	private void addListeners(final Clip clip) {
		clip.addLineListener(new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					listener.onEnd();
					clip.close();
				}
			}
		});
		clip.addLineListener(new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.START) {
					listener.onStart();
				}
			}
		});
	}
}
