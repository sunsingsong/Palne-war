package game;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import window.SceneManager;

public class WinnerGame extends Canvas{
	    public  GraphicsContext gc;
		public  WinnerGame() {
			super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
			gc = this.getGraphicsContext2D();
			gc.fillRect(0, 0,1000, 1000);
			gc.setFill(Color.CORNFLOWERBLUE);
			gc.setFont(new Font("Cooper Black", 100));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("WINNER",SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT/2-50);
			gc.setFont(new Font("Cooper Black", 50));
			gc.fillText("Play Again?",SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT/2+150);
			addKeyEventHandler();
			
		}
		
		private void addKeyEventHandler() {
			//TODO Fill Code
			this.setOnKeyPressed((KeyEvent event)->{
				if(event.getCode()==KeyCode.ESCAPE) {
					Platform.exit();
				}
				if(event.getCode()==KeyCode.ENTER) {
					SceneManager.gotoMainMenu();
				}
			});
		}
}
