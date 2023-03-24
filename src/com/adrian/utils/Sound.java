package com.adrian.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.adrian.base.Global;

public enum Sound{
	INTRO("sounds\\BlueBoyAdventure", false),
	GAMEOVER("sounds\\\\gameover", false),
	COIN("sounds\\coin", true),
	POWERUP("sounds\\\\powerup", true),
	UNLOCK("sounds\\\\unlock", true),
	HIT("sounds\\\\hitmonster", true),
	PLAYER_HURT("sounds\\\\receivedamage", true),
	SPEAK("sounds\\\\speak", true),
	LEVELUP("sounds\\\\levelup", true),
	ACHIEVE("sounds\\\\obtained_item", true),
	CURSOR("sounds\\\\cursor", true),
	CUT("sounds\\\\cuttree", true),
	MAGIC("sounds\\\\burning", true);
	
	public Clip clip;
	private URL soundURL;
	
	public boolean isSFX;
	
	public FloatControl musicVolume = null;
	public int volumeScale = 3;
	private float volume;
	
	Sound(String path, boolean isSFX) {
		try {
			this.isSFX = isSFX;
			soundURL = new File(Global.assets + path + ".wav").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void startPlay() {
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
			this.clip = AudioSystem.getClip();
			this.clip.open(audioInput);
			musicVolume = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
			updateVolume();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.clip.start();
	}
	
	public void stop() {
		this.clip.stop();
	}
	
	public void playSE() {
		this.startPlay();
	}
	public void play() {
		this.startPlay();
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void updateVolume() {
		switch (volumeScale) {
		case 0: volume = -80f ; break;
		case 1: volume = -20f ; break;
		case 2: volume = -12f ; break;
		case 3: volume = -5f ; break;
		case 4: volume = 1f ; break;
		case 5: volume = 6f ; break;
		}
		if(musicVolume != null) musicVolume.setValue(volume);
	}
}