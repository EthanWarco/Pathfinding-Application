package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.swing.JComponent;

import maze_algorithms.HuntAndKill;
import maze_algorithms.Maze;
import maze_algorithms.RandomizedDFS;
import maze_algorithms.RandomizedKruskal;
import maze_algorithms.Wilsons;
import pathfinding_algorithms.AStar;
import pathfinding_algorithms.BFS;
import pathfinding_algorithms.DFS;
import pathfinding_algorithms.PathFinder;

@SuppressWarnings("serial")
public class DrawingArea extends JComponent {
	
	private Graphics2D g2d;
	private Image image;
	private int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private Color background = new Color(211, 215, 239);
	private int widthRatio = 5;
	private boolean canDraw = true;
	private Cell[][] grid;
	public Cell start;
	public Cell finish;
	
	public DrawingArea() {
		setOpaque(true);
		setDoubleBuffered(false);
		setBounds(width/widthRatio, 0, (widthRatio - 1)*width/widthRatio, height);
		setPreferredSize(new Dimension((widthRatio - 1)*width/widthRatio, height));
		
		addMouseListener(new MouseAdapter() {
			
			
			public void mousePressed(MouseEvent e) {
				if(canDraw) {
					Cell cell = findCell(new Point((int)e.getLocationOnScreen().getX() - (width/widthRatio), (int)e.getLocationOnScreen().getY()));
					if(Main.status == CellState.FINISH && finish != null) finish.setState(CellState.BLANK);
					else if(Main.status == CellState.START && start != null) start.setState(CellState.BLANK);
					cell.setState(Main.status);
					repaint();
				}
			}
			
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			
			public void mouseDragged(MouseEvent e) {
				if(canDraw) {
					Cell cell = findCell(new Point((int)e.getLocationOnScreen().getX() - (width/widthRatio), (int)e.getLocationOnScreen().getY()));
					if(Main.status != CellState.FINISH && Main.status != CellState.START) {
						cell.setState(Main.status);
						repaint();
					}
				}
			}
			
			
		});
		
	}
	
	protected void paintComponent(Graphics g) {
		if(image == null) {
			image = createImage(getSize().width, getSize().height);
			g2d = (Graphics2D) image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear() {
		finish = start = null;
		grid = new Cell[getSize().width/Main.div][getSize().height/Main.div];
		
		g2d.setPaint(background);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		
		int div = Main.div-1;
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				Cell cell = new Cell(i*div + i, j*div + j, i, j, div, g2d);
				cell.setState(CellState.BLANK);
				grid[i][j] = cell;
			}
		}
		
		g2d.setPaint(Color.BLACK);
		repaint();
	}
	
	public void randomize(int density) {
		Random random = new Random();
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(random.nextInt(density) == 1) {
					grid[i][j].setState(CellState.WALL);
				}
			}
		}
		repaint();
	}
	
	public boolean drawPath(String algorithm, boolean showNodes, boolean diagonals, int delay) {
		PathFinder finder = null;
		
		switch(algorithm) {
			case "A*": finder = new AStar(grid, start, finish, diagonals, showNodes, delay);
			break;
			case "Depth First Search": finder = new DFS(grid, start, finish, diagonals, showNodes, delay);
			break;
			case "Breadth First Search": finder = new BFS(grid, start, finish, diagonals, showNodes, delay);
			break;
		}
		
		return finder.findPath();
	}
	
	public void generateRandomMaze(String algorithm) {
		Maze maze = null;
		switch(algorithm) {
			case "Recursive Backtracking": maze = new RandomizedDFS(grid);
			break;
			case "Kruskal's Algorithm": maze = new RandomizedKruskal(grid);
			break;
			case "Wilson's Algorithm": maze = new Wilsons(grid);
			break;
			case "Hunt And Kill": maze = new HuntAndKill(grid);
			break;
		}
		
		maze.generateMaze();
		repaint();
	}
	
	
	/*
	 * Helper Functions
	 */
	
	private Cell findCell(Point point) {
		return grid[point.x/Main.div][point.y/Main.div];
	}
	
	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}
	
	public void clearPath() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j].setState(grid[i][j].getState());
			}
		}
		repaint();
	}
	
}
