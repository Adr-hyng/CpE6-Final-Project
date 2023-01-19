package com.adrian.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.adrian.base.Global;

public enum Sound{
	INTRO("sounds\\BlueBoyAdventure"),
	COIN("sounds\\coin"),
	POWERUP("sounds\\\\powerup"),
	UNLOCK("sounds\\\\unlock"),
	HIT("sounds\\\\hitmonster"),
	PLAYER_HURT("sounds\\\\receivedamage"),
	SPEAK("sounds\\\\speak"),
	LEVELUP("sounds\\\\levelup"),
	GAMEOVER("sounds\\\\gameover"),
	ACHIEVE("sounds\\\\obtained_item"),
	CURSOR("sounds\\\\cursor"),
	CUT("sounds\\\\cuttree"),
	MAGIC("sounds\\\\burning");
	
	private Clip clip;
	private URL soundURL;
	
	Sound(String path) {
		try {
			soundURL = new File(Global.assets + path + ".wav").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void startPlay() {
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
			clip = AudioSystem.getClip();
			clip.open(audioInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		clip.start();
	}
	
	public void stop() {
		this.clip.stop();
	}
	
	public void playSE() {
		this.startPlay();
	}
	public void play() {
		this.startPlay();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}