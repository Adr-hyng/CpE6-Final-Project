package com.adrian;

import javax.swing.JFrame;

import com.adrian.user_interface.GamePanel;

import java.awt.Rectangle;

// Here's the Google Drive Link for the AssetS: https://drive.google.com/drive/folders/1mtz-dJuiuUXyb-aalhCXdpJPTCj-IjYM

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("CpE6 Final Project");
		
		GamePanel gamePanel = new GamePanel();
		window.setBounds(new Rectangle(gamePanel.getPreferredSize()));
		window.add(gamePanel);
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.worldSetup();
		gamePanel.startGameThread();
	}

}
