package physics;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class TestGame implements Runnable {
	
	private Display display;
	public int width, height;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	private KeyManager keyManager;
	
	private Player player;
	
	public TestGame(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
		keyManager = new KeyManager();
		player = new Player(this, 100, 100);
		display = new Display(title, width, height, this);
		
		
	}
	
	private void tick(){
		keyManager.tick();
		player.tick();
	}
		
	public void run(){
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		
		while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				tick();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
	}
	
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public KeyManager getKeyManager(){
		return this.keyManager;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	
	public static void main(String[] args) {
		TestGame game = new TestGame("Movement test", 960, 540);
		game.start();
	}
}
