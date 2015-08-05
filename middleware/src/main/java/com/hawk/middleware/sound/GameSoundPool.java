package com.hawk.middleware.sound;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @author gary
 *
 */
public class GameSoundPool {
	private Context mContext;

	public GameSoundPool(Context context) {
		mContext = context;
		initSounds();
	}

	int streamVolume;

	private SoundPool soundPool;

	private HashMap<Integer, Integer> soundPoolMap;

	/***************************************************************
	 * Function:initSounds(); Parameters:null Returns:None.
	 * Notes:none.
	 ***************************************************************/
	public void initSounds() {

		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);

		soundPoolMap = new HashMap<Integer, Integer>();

		AudioManager mgr = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	/***************************************************************
	 * Function:loadSfx(); Parameters:null Returns:None.
	 * Notes:none.
	 ***************************************************************/
	public void loadSfx(int raw, int ID) {

		soundPoolMap.put(ID, soundPool.load(mContext, raw, ID));
	}

	/***************************************************************

	 ***************************************************************/
	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
		
	}
}
