package com.adrian.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import com.adrian.base.GameState;
import com.adrian.base.Global;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;

public class KeyHandler implements KeyListener{
	GamePanel gp;
	
	@SuppressWarnings("serial")
	public Map<String, Boolean> haveKeyPressed = new HashMap<String, Boolean>() {{
		put("W", false);
		put("A", false);
		put("S", false);
		put("D", false);
		put("F", false);
		put("ENTER", false);
		put("SPACE", false);
		put("ESC", false);
	}};
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (gp.gameState == GameState.Menu.state) {
			menuState(code);
		}
		
		else if (gp.gameState == GameState.Continue.state) {
			playState(code);
		}
		
		else if (gp.gameState == GameState.Pause.state) {
			pauseState(code);
		}
		
		else if (gp.gameState == GameState.Dialogue.state) {
			dialogueState(code);
		}
		
		else if (gp.gameState == GameState.ShowStat.state) {
			statstate(code);
		}
		
		else if (gp.gameState == GameState.ShowOption.state) {
			optionState(code);
		}
	}
	
	private void optionState(int code) {
		if(code == KeyEvent.VK_ESCAPE) {
			gp.gameState = GameState.Continue.state;
		}
		
		if(code == KeyEvent.VK_ENTER) {
			haveKeyPressed.replace("ENTER", true);
		}
	}
	
	private void menuState(int code) {
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
					Sound.INTRO.playSE();
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
				int totalSelection = Global.util.getStartingClass().size() + 1;
				String selectedClass = null;
				if(gp.ui.selectionY != totalSelection) {
					selectedClass = Global.util.getStartingClass().get(gp.ui.selectionY);
					gp.newGame(gp.player, 1, selectedClass);
					gp.gameState = GameState.Continue.state;
				}
				
				else if(gp.ui.selectionY == totalSelection) {
					gp.ui.titleScreenState = 0;
					gp.ui.selectionY = 0;
				}
			}
		}
	}
	
	private void playState(int code) {
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
			
			if(code == KeyEvent.VK_F && !gp.player.isMoving) {
				haveKeyPressed.replace("F", true);
			}
			
			if(code == KeyEvent.VK_ESCAPE) {
				gp.gameState = GameState.ShowOption.state;
			}
		}
		
		if(code == KeyEvent.VK_C) {
			gp.gameState = GameState.ShowStat.state;
		}
		
		if(code == KeyEvent.VK_SPACE) {
			haveKeyPressed.replace("SPACE", true);
		}
		
		if(code == KeyEvent.VK_P) {
			gp.gameState = GameState.Pause.state;
		}
	}
	
	private void pauseState(int code) {
		if(code == KeyEvent.VK_P) {
			gp.gameState = GameState.Continue.state;
		}
	}
	
	private void dialogueState(int code) {
		if(code == KeyEvent.VK_ESCAPE) {
			haveKeyPressed.replace("ESC", true);
			gp.ui.dialogueOffset.y = 0;
		}
	}
	
	private void statstate(int code) {
		if (code == KeyEvent.VK_C) {
			gp.gameState = GameState.Continue.state;
		}
		
		if (code == KeyEvent.VK_W) {
			if(gp.ui.inventorySlotRow != 0) {
				gp.ui.inventorySlotRow--;
				Sound.CURSOR.playSE();
//				gp.playSoundEffect(10);
			}
		}
		if (code == KeyEvent.VK_A) {
			if(gp.ui.inventorySlotCol != 0) {
				gp.ui.inventorySlotCol--;
				Sound.CURSOR.playSE();
			}
		}
		if (code == KeyEvent.VK_S) {
			if(gp.ui.inventorySlotRow != (gp.ui.inventoryMaxSlotY - 1) ) {
				gp.ui.inventorySlotRow++;
				Sound.CURSOR.playSE();
			}
		}
		if (code == KeyEvent.VK_D) {
			if(gp.ui.inventorySlotCol != (gp.ui.inventoryMaxSlotX - 1) ) {
				gp.ui.inventorySlotCol++;
				Sound.CURSOR.playSE();
			}
		}
		
		if (code == KeyEvent.VK_ENTER) {
			gp.player.selectedItem();
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
		
		if(code == KeyEvent.VK_F) {
			haveKeyPressed.replace("F", false);
		}
		
		if(code == KeyEvent.VK_SPACE) {
			haveKeyPressed.replace("SPACE", false);
		}
		
		if(code == KeyEvent.VK_ESCAPE) {
			haveKeyPressed.replace("ESC", false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
