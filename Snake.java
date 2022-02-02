import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake {

	static final int GRID_SCALE = 60;
	static ArrayList<Node> nodes = new ArrayList<Node>();
	static Point fruitPos;
	
	public static void main(String[] args) {
		Grid grid = new Grid();
		nodes.add(new Node());
		fruitPos = new Point(5 * GRID_SCALE, 5 * GRID_SCALE);
		PaintPanel panel = new PaintPanel(nodes, GRID_SCALE, fruitPos);

		ActionListener update = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getCurrentDir() == 0) {
						nodes.get(i).y -= GRID_SCALE;
					} else if (nodes.get(i).getCurrentDir() == 2) {
						nodes.get(i).y += GRID_SCALE;
					} else if (nodes.get(i).getCurrentDir() == 1) {
						nodes.get(i).x += GRID_SCALE;
					} else if (nodes.get(i).getCurrentDir() == 3) {
						nodes.get(i).x -= GRID_SCALE;
					}
				}

				if (nodes.size() > 1) {
					for (int i = 1; i < nodes.size(); i++) {
						if (nodes.get(i).x == nodes.get(0).x && nodes.get(i).y == nodes.get(0).y)
							System.exit(0);
					}
				}

				for (int i = 0; i < nodes.size(); i++) {
					nodes.get(i).setPrevDir(nodes.get(i).getCurrentDir());
					if (i > 0)
						nodes.get(i).setCurrentDir(nodes.get(i - 1).getPrevDir());
				}

				if (nodes.get(0).x < 0 || nodes.get(0).x > 600 || nodes.get(0).y < 0 || nodes.get(0).y > 600)
					System.exit(0);

				if (nodes.get(0).x == fruitPos.getX() && nodes.get(0).y == fruitPos.getY()) {
					Node newNode;
					boolean occupied;
					int x = 0;
					int y = 0;
					do {
						occupied = false;
						x = new Random().nextInt(10) * GRID_SCALE;
						y = new Random().nextInt(10) * GRID_SCALE;
						newNode = new Node();

						for (int i = 0; i < nodes.size(); i++) {

							if (x == nodes.get(i).x && y == nodes.get(i).y)
								occupied = true;

						}

						newNode.x = nodes.get(nodes.size() - 1).x;
						newNode.y = nodes.get(nodes.size() - 1).y;

					} while (occupied);
					nodes.add(newNode);

					panel.addScore();
					fruitPos = new Point(x, y);
					panel.setFruitPos(x, y);
				}

				panel.repaint();
			}

		};
		Timer gameLoop = new Timer(250, update);
		gameLoop.start();

		grid.add(panel);
		grid.addKeyListener(panel);
		grid.setContentPane(panel);
		panel.setPreferredSize(new Dimension(600, 600));
		grid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		grid.setVisible(true);
		grid.pack();

	}

}

class Node {

	int x = 0, y = 0;
	// 0 = up, 1 = right, 2 = down, 3 = left
	int currentDir = -1;
	int prevDir = -1;

	public void setCurrentDir(int direction) {
		currentDir = direction;
	}

	public int getCurrentDir() {
		return currentDir;
	}

	public void setPrevDir(int direction) {
		prevDir = direction;
	}

	public int getPrevDir() {
		return prevDir;
	}
}

class Grid extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int gridSize = 0;

	Grid() {
		gridSize = 10;
	}

	Grid(int size) {
		gridSize = size;
	}

}

class PaintPanel extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ArrayList<Node> nodes;
	static int gridScale;
	Point fruitPos;
	static int score = 0;

	PaintPanel(ArrayList<Node> nodeArrList, int gridScalar, Point fruitPos) {
		nodes = nodeArrList;
		gridScale = gridScalar;
		this.fruitPos = fruitPos;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < nodes.size(); i++) {
			g.fillRect(nodes.get(i).x + 5, nodes.get(i).y + 5, gridScale - 10, gridScale - 10);
			g2d.setStroke(new BasicStroke(50));
			if (i > 0)
				g2d.drawLine(nodes.get(i - 1).x + 30, nodes.get(i - 1).y + 30, nodes.get(i).x + 30,
						nodes.get(i).y + 30);
		}
		g.setColor(Color.red);
		g.fillRect((int) fruitPos.getX() + 5, (int) fruitPos.getY() + 5, gridScale - 10, gridScale - 10);
		g.setColor(Color.black);
		g.drawString(score + "", 10, 10);
	}

	public void addScore() {
		score++;
	}
	
	public void setFruitPos(int x, int y) {
		fruitPos = new Point(x, y);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W) {
			nodes.get(0).setCurrentDir(0);
		} else if (key == KeyEvent.VK_S) {
			nodes.get(0).setCurrentDir(2);
		} else if (key == KeyEvent.VK_D) {
			nodes.get(0).setCurrentDir(1);
		} else if (key == KeyEvent.VK_A) {
			nodes.get(0).setCurrentDir(3);
		} else if (key == KeyEvent.VK_UP) {
			nodes.get(0).setCurrentDir(0);
		} else if (key == KeyEvent.VK_DOWN) {
			nodes.get(0).setCurrentDir(2);
		} else if (key == KeyEvent.VK_RIGHT) {
			nodes.get(0).setCurrentDir(1);
		} else if (key == KeyEvent.VK_LEFT) {
			nodes.get(0).setCurrentDir(3);
		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}