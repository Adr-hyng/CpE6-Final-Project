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
				
				soundURL[4] = new File(GlobalTool.assetsDirectory + "sounds\\hitmonster.wav").toURI().toURL();
				soundURL[5] = new File(GlobalTool.assetsDirectory + "sounds\\receivedamage.wav").toURI().toURL();
				soundURL[6] = new File(GlobalTool.assetsDirectory + "sounds\\speak.wav").toURI().toURL();
				soundURL[7] = new File(GlobalTool.assetsDirectory + "sounds\\levelup.wav").toURI().toURL();
				soundURL[8] = new File(GlobalTool.assetsDirectory + "sounds\\gameover.wav").toURI().toURL();
				soundURL[9] = new File(GlobalTool.assetsDirectory + "sounds\\obtained_item.wav").toURI().toURL();
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
