package com.adrian.sounds;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.adrian.GlobalTool;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
			try {
				soundURL[0] = new File(GlobalTool.assetsDirectory + "sounds\\BlueBoyAdventure.wav").toURI().toURL();
				soundURL[1] = new File(GlobalTool.assetsDirectory + "sounds\\coin.wav").toURI().toURL();
				soundURL[2] = new File(GlobalTool.assetsDirectory + "sounds\\powerup.wav").toURI().toURL();
				soundURL[3] = new File(GlobalTool.assetsDirectory + "sounds\\unlock.wav").toURI().toURL();
				soundURL[4] = new File(GlobalTool.assetsDirectory + "sounds\\fanfare.wav").toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(audioInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
