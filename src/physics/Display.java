package physics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel{
	
	private JFrame frame;
	private Image playerImage;
	private Player player;
	private double lastAngle = 0.0;
	
	public Display(String title, int width, int height, TestGame game){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(this);
		this.player = game.getPlayer();
		this.playerImage = player.getImage();
		addMouseMotionListener(player);
		addMouseListener(player);
		frame.addKeyListener(game.getKeyManager());
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		AffineTransform at = AffineTransform.getTranslateInstance(player.x, player.y);
		double deltax = (player.mx - 10) - player.x;
		double deltay = player.y - (player.my - 10) ;
		double deltaz = Math.sqrt(deltax * deltax + deltay * deltay);
		double angle = lastAngle;
		if (deltaz > 5){
			angle = Math.atan2(deltax, deltay);
		}
		
		player.setAngle(angle);
		lastAngle = angle;
		at.rotate(angle, playerImage.getWidth(null)/2, playerImage.getHeight(null)/2);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(playerImage, at, null);
		ArrayList<Bullet> bullets = (ArrayList<Bullet>) player.getBullets();
		for(int i=0; i<bullets.size(); i++){
			g2.setColor(Color.RED);
			g2.fillOval((int)bullets.get(i).x, (int)bullets.get(i).y, 4, 4);
		}
		repaint();
	}
		
}
