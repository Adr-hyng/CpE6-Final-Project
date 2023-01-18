package com.adrian.base;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.adrian.user_interfaces.GamePanel;

// Here's the Google Drive Link for the AssetS: https://drive.google.com/drive/folders/1mtz-dJuiuUXyb-aalhCXdpJPTCj-IjYM

public class Main {
	
	private static JFrame window = new JFrame();
	
	public static void main(String[] args) {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		window.setResizable(false);
		window.setAlwaysOnTop(true);
		window.setTitle("CpE6 Final Project");
		
		GamePanel gamePanel = new GamePanel();
		window.setBounds(new Rectangle(gamePanel.getPreferredSize()));
		window.add(gamePanel);
		WindowListener listener = new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				try {
					gamePanel.saveGame(gamePanel.player);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		};
		window.addWindowListener(listener);
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}
