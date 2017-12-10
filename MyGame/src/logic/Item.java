package logic;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;
import window.SceneManager;

public class Item extends CollidableEntity{
	public int itemType;
	public int itemTick=0;
	public Item() {
		Random rand= new Random();
		this.x=rand.nextInt(SceneManager.SCENE_WIDTH);
		this.y=rand.nextInt(SceneManager.SCENE_HEIGHT);
		randomItem();
		this.setImage(new Image("Armor.png"));
	}
	private void randomItem() {
		Random rand= new Random();
		this.itemType=rand.nextInt(2);
	}
	public void update() {
		this.itemTick++;
		if(this.itemTick>=600) { 
			  this.destroyed=true;
			  this.itemTick=0;
			}
		else {
			//System.out.println(this.x+"    "+this.y);
			for (IRenderable i : RenderableHolder.getInstance().getEntities()) {
				if (this.collideWith((CollidableEntity) i)) {
					//System.out.println(this.x+"    "+this.y);
					if (i instanceof Tank) {
<<<<<<< HEAD
						System.out.println("keep");
||||||| merged common ancestors
=======
						//System.out.println("asd");
>>>>>>> 89831c4ce430819fe1f8d73278403ce42452bf40
						this.destroyed = true;
						this.itemTick=0;
						((Tank) i).increaseHp();
					}
				}
			}
		}
	}
	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(this.image, x, y);
	}

}
