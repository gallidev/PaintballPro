package physics;

/*
 * If you get an input error when running this, you need to add the resources folder to your build path
 * In Eclipse this is done by selecting the project folder  "c1", clicking on the Project menu at the top,
 * Project -> Properties -> Java Build Path -> Libraries -> Add Class Folder -> 
 * Under c1, tick the box next to the resources folder -> Press ok then Apply
 */

public class TestGame {
	
	private Player player;
	public boolean up, down, left, right, shoot;
	public double mx, my;
	
	public TestGame(){
		player = new Player(this, 100, 100);
		up = false;
		down = false;
		left = false;
		right = false;
		shoot = false;
	}
	
	public Player getPlayer(){
		return this.player;
	}
}
