package com.adrian.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import com.adrian.entity.Entity;
import com.adrian.entity.Player;
import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;

public class KeyHandler implements KeyListener{
	GamePanel gp;
	
	@SuppressWarnings("serial")
	public Map<String, Boolean> haveKeyPressed = new HashMap<String, Boolean>() {{
		put("W", false);
		put("A", false);
		put("S", false);
		put("D", false);
		put("ENTER", false);
		put("SPACE", false);
		put("T", false); // Test
		put("ESC", false);
	}};
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (gp.gameState == GameState.Menu.state) {
			if(gp.ui.titleScreenState == 0) {
				if(code == KeyEvent.VK_W) {
					if(gp.ui.selectionY > 0) gp.ui. selectionY--;
				}
				if(code == KeyEvent.VK_S) {
					if(gp.ui.selectionY < gp.ui.menuOption.size() - 1) gp.ui.selectionY++;
				}
				
				if(code == KeyEvent.VK_ENTER) {
					if(gp.ui.selectionY + 1 == GameState.Continue.state && gp.canPlay) {
						try{
							gp.loadGame(gp.player, 1);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						gp.playMusic(0);
						gp.gameState = GameState.Continue.state;
					}
					
					else if(gp.ui.selectionY + 1 == GameState.NewGame.state) {
						gp.ui.titleScreenState = 1;
						gp.ui.selectionY = 0;
					}
					
					else if(gp.ui.selectionY == gp.ui.menuOption.size() - 1) {
						gp.gameThread = null;
						System.exit(0);
						System.out.println("End");
					}
				}
			}
			
			else if (gp.ui.titleScreenState == 1) {
				if(code == KeyEvent.VK_W) {
					if(gp.ui.selectionY > 0) gp.ui.selectionY--;
					if(gp.ui.selectionY >= gp.ui.classOption.size() - 1) gp.ui.selectionY--;
				}
				if(code == KeyEvent.VK_S) {
					if(gp.ui.selectionY < gp.ui.classOption.size() - 2) gp.ui.selectionY++;
					else if(gp.ui.selectionY == gp.ui.classOption.size() - 2) gp.ui.selectionY = gp.ui.classOption.size();
				}
				
				if(code == KeyEvent.VK_ENTER) {
					if(gp.ui.selectionY == 0) {
//						gp.playMusic(0);
						gp.player.maxLife = 12;
						gp.player.currentLife = gp.player.maxLife;
						gp.newGame(gp.player, 1);
						gp.gameState = GameState.Continue.state;
					}
					
					else if(gp.ui.selectionY == 1) {
//						gp.playMusic(0);
						gp.player.maxLife = 6;
						gp.player.currentLife = gp.player.maxLife;
						gp.newGame(gp.player, 1);
						gp.gameState = GameState.Continue.state;
					}
					
					else if(gp.ui.selectionY == 3) {
						gp.ui.titleScreenState = 0;
						gp.ui.selectionY = 0;
					}
				}
			}
		}
		
		else if (gp.gameState == GameState.Continue.state) {
			if(!gp.player.attacking) {
				if(code == KeyEvent.VK_W) {
					haveKeyPressed.replace("W", true);
				}
				if(code == KeyEvent.VK_S) {
					haveKeyPressed.replace("S", true);
				}
				if(code == KeyEvent.VK_A) {
					haveKeyPressed.replace("A", true);
				}
				if(code == KeyEvent.VK_D) {
					haveKeyPressed.replace("D", true);
				}
				
				if(code == KeyEvent.VK_ENTER && !gp.player.isMoving) {
					haveKeyPressed.replace("ENTER", true);
				}
			}
			
			if(code == KeyEvent.VK_SPACE) {
				haveKeyPressed.replace("SPACE", true);
			}
			
			if(code == KeyEvent.VK_P) {
				gp.gameState = GameState.Pause.state;
			}
			
			// DEBUG
			if(code == KeyEvent.VK_T) {
				haveKeyPressed.replace("T", true);
			}
		}
		
		else if (gp.gameState == GameState.Pause.state) {
			if(code == KeyEvent.VK_P) {
				gp.gameState = GameState.Continue.state;
			}
		}
		
		else if (gp.gameState == GameState.Dialogue.state) {
			if(code == KeyEvent.VK_ESCAPE) {
				haveKeyPressed.replace("ESC", true);
				gp.ui.dialogueOffset.y = 0;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) {
			haveKeyPressed.replace("W", false);
		}
		if(code == KeyEvent.VK_S) {
			haveKeyPressed.replace("S", false);
		}
		if(code == KeyEvent.VK_A) {
			haveKeyPressed.replace("A", false);
		}
		if(code == KeyEvent.VK_D) {
			haveKeyPressed.replace("D", false);
		}
		
//		if(code == KeyEvent.VK_ENTER) {
//			haveKeyPressed.replace("ENTER", false);
//		}

		if(code == KeyEvent.VK_SPACE) {
			haveKeyPressed.replace("SPACE", false);
		}
		
		if(code == KeyEvent.VK_ESCAPE) {
			haveKeyPressed.replace("ESC", false);
		}
		
		if(code == KeyEvent.VK_T) {
			haveKeyPressed.replace("T", false);
		}
	}
}
