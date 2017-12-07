package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;
import window.SceneManager;

public class Boss extends CollidableEntity {
	private static final Font TEXT_FONT = new Font("Monospace", 80);
	private static final Font SCORE_TIME_FONT = new Font("Monospace", 30);
	public int direction = 4;
	private int hp = 15;
	private int speed = 3;
	private int tick = 0;
	private int lastTick = 0;
	private int playerX;
	private int playerY;
	private int mX = (SceneManager.SCENE_WIDTH / 2 - this.width / 2);
	private int mY = (SceneManager.SCENE_HEIGHT / 2 - this.height / 2);
	private int division=10;
	private int bossImage=1;
	private int lastBossImage;

	boolean barrier = true;
	boolean spawn = true;
	boolean b1;
	boolean b2;
	boolean b3;
	boolean b4;

	boolean phase1 = false;
	boolean sp1;
	boolean phase2 = false;
	boolean sp2;
	boolean phase3 = false;
	boolean sp3;

	boolean flashing = false;
	int flashCounter = 10;
	int flashDurationCounter = 10;

	public Boss(int x, int y) {
		this.sp1 = true;
		this.radius = 26;
		this.width = 96;
		this.height = 96;
		this.x = x;
		this.y = y;
		this.z = 2;
		this.setImage(new Image("file:res/boss1.png"));
	}

	public void update() {
		if (sp1) {
			startPhase1();
		}
		if (sp2) {
			startPhase2();
		}
		if (phase1) {
			phase1();
		}

		if (phase2) {
			phase2();
		}
	}

	public void draw(GraphicsContext gc) {
		if(phase1) {
			gc.setGlobalAlpha(0.5);
			gc.setFill(Color.GHOSTWHITE);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(SCORE_TIME_FONT);
			gc.fillText(""+this.hp, mX, mY);
			gc.setGlobalAlpha(1.0);
			gc.drawImage(this.image, this.x, this.y);
		}
		if(phase2) {
			gc.setGlobalAlpha(0.8);
			gc.setFill(Color.GREEN);
			gc.fillRect(SceneManager.SCENE_WIDTH-170, 10, 150*(this.hp/6.0), 20);
			gc.setFill(Color.RED);
			gc.fillRect(SceneManager.SCENE_WIDTH-170+(150*(this.hp/6.0)), 10, 150*(1-this.hp/6.0), 20);
			
		}
		gc.setGlobalAlpha(1.0);
		gc.drawImage(this.image, this.x, this.y);
	}

	public void flashState() {
		if (flashing) {
			if (flashCounter == 0) {
				flashDurationCounter = 10;
				flashCounter = 10;
				this.visible = true;
				flashing = false;
			} else {
				if (flashDurationCounter > 0) {
					this.visible = flashCounter <= 5;
					flashDurationCounter--;
				} else {
					this.visible = true;
					flashDurationCounter = 10;
					flashCounter--;
				}
			}
		}
	}

	public void phase1() {
		b1 = false;
		if (hp <= 10) {
			this.speed = 5;
		}
		if (hp <= 6) {
			this.speed = 8;
		}
		if (hp <= 3) {
			this.speed = 9;
		}
		if (hp <= 0) {
			// this.visible=false;
			this.phase1 = false;
			// this.phase2=true;
			this.hp = 6;
			this.tick = 1;
			this.sp2 = true;
			return;
		}

		// System.out.println(this.hp);
		for (IRenderable i : RenderableHolder.getInstance().getEntities()) {
			if (this.collideWith((CollidableEntity) i)) {
				if (i instanceof Bullet && !((Bullet) i).isEnemy && !flashing) {
					((Bullet) i).destroyed = true;
					this.hp -= ((Bullet) i).getDamage();
					this.flashing = true;
				}
			}
		}
		if (this.hp > 0) {
			flashState();
		}

		if (hp > 0) {
			if (this.direction == 4) {
				this.x -= speed;
				if (x <= 0) {
					this.direction = 1;
				}
			}
			if (this.direction == 1) {
				this.x += speed;
				if (this.x >= SceneManager.SCENE_WIDTH - this.width) {
					this.direction = 2;
				}
			}
			if (this.direction == 2) {
				this.y += speed;
				if (this.y >= SceneManager.SCENE_HEIGHT - this.height) {
					this.direction = 0;
				}
			}
			if (this.direction == 0) {
				this.x -= speed;
				if (this.x <= 0) {
					this.direction = 3;
				}
			}
			if (this.direction == 3) {
				this.y -= speed;
				if (this.y <= 0) {
					this.direction = 1;
				}
			}
			if (tick >= lastTick) {
				tick = lastTick;
				lastTick += 50;
			}
			if (tick % ((10 - this.speed) * 6) == 0) {
				b1 = true;
				// fire(this.direction);
			}
		}
		tick++;
	}

	public void startPhase1() {
		this.phase1 = true;
		this.sp1 = false;
	}

	public void phase2() {
		flashState();
		if ((this.hp >= 5 && this.hp <= 6)||this.hp >=1 && this.hp<=2) {
			if (this.x > playerX && this.x >= (mX) - 100)
				x-=1;
			if (this.x < playerX && this.x <= (mX) + 100 - this.width)
				x+=1;
			if (this.y > playerY && this.y >= (mY) - 100)
				y-=1;
			if (this.y < playerY && this.y <= (mY) + 100 - this.height)
				y+=1;
		}
		// if(this.x>playerX)x--;
		// if(this.y>playerY)y--;
		// if(this.x<playerX)x++;
		// if(this.y<playerY)y++;
		if (hp > 0) {
			b4=false;
			for (IRenderable i : RenderableHolder.getInstance().getEntities()) {
				if (this.collideWith((CollidableEntity) i)) {
					if (i instanceof Bullet && !((Bullet) i).isEnemy) {
						((Bullet) i).destroyed = true;
						this.hp -= 1;
						this.flashing = true;
						this.barrier = true;
						tick = 0;
						if(this.hp==4) {
							this.sp2=true;
						}
						if(this.division==10) {
							this.division=5;
						}else {
							this.division=10;
						}
					}
				}
			}
			if(this.hp>=1&&this.hp<=2) {
				
			}
			if(this.hp>=3&&this.hp<=6&&tick%50==0) {
				if(bossImage==1) {
					this.setImage(new Image("file:res/boss"+this.bossImage+".png"));
					this.lastBossImage=bossImage;
					bossImage++;
				}else if(bossImage==2) {
					if(this.lastBossImage==1) {
						this.setImage(new Image("file:res/boss"+this.bossImage+".png"));
						this.lastBossImage=bossImage;
						bossImage++;
					}else {
						this.setImage(new Image("file:res/boss"+this.bossImage+".png"));
						this.lastBossImage=bossImage;
						bossImage--;
					}
				}else if(bossImage==3) {
					this.setImage(new Image("file:res/boss"+this.bossImage+".png"));
					this.lastBossImage=bossImage;
					bossImage--;
				}
			}
			if (tick % 500 == 0) {
				breakBarrier();
				//tick = 1;
			}
			if(!b4 && this.hp >= 1 && this.hp <= 2 && tick % division == 0 ) {
				this.setImage(new Image("file:res/boss4.png"));
				b2=true;
				b3=true;
				b4=true;
			}else if(!b4){
				b2=false;
				b3=false;
			}
			if (!b4 && this.hp >= 5 && this.hp <= 6 && tick % division == 0 ) {
				// this.x++;
				// this.y++;
				b2 = true;
				// lastTick=tick;
			} else if(!b4){
				b2 = false;
			}
			if (!b4 && this.hp >= 3 && this.hp <= 4 && tick % division == 0 ) {
				// lastTick=tick;
				b3 = true;
			} else if(!b4){
				b3 = false;
			}
		}
		tick++;
	}

	public void playerPos(int x, int y) {
		this.playerX = x;
		this.playerY = y;
	}

	public int getHp() {
		return this.hp;
	}

	public void breakBarrier() {
		this.barrier = false;
	}

	public void startPhase2() {
		if (this.x > mX-50)
			x -= 2;
		if (this.y > mY-50)
			y -= 2;
		if (this.x < mX-50)
			x += 2;
		if (this.y < mY-50)
			y += 2;
//		System.out.println(this.x+" "+this.y);
//		this.flashing = true;
//		flashState();
		if ((this.x == (mX-50)||this.x == (mX-51)||this.x == (mX-49)) && (this.y == (mY-50)||this.y == (mY-51)||this.y == (mY-49))) {
			//System.out.println("asd");
			this.phase2 = true;
			this.sp2 = false;
			this.barrier = true;
		}
	}

}