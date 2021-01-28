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
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class DrawingArea extends JComponent {
	
	private Graphics2D g2d;
	private Image image;
	private Maze maze;
	private int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private Color background = new Color(211, 215, 239);
	private int widthRatio = 5;
	private boolean canDraw = true;
	private Cell[][] grid;
	public boolean hasFinish = false;
	public boolean hasStart = false;
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
					if(Main.status == CellState.FINISH) {
						Cell finish = findCellFromState(CellState.FINISH);
						if(finish != null) {
							finish.setState(CellState.BLANK);
						}
					} else if(Main.status == CellState.START) {
						Cell start = findCellFromState(CellState.START);
						if(start != null) {
							start.setState(CellState.BLANK);
						}
					}
					if(cell != null) {
						cell.setState(Main.status);
						repaint();
					}
				}
			}
			
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			
			public void mouseDragged(MouseEvent e) {
				if(canDraw) {
					Cell cell = findCell(new Point((int)e.getLocationOnScreen().getX() - (width/widthRatio), (int)e.getLocationOnScreen().getY()));
					if(Main.status != CellState.FINISH && Main.status != CellState.START) {
						if(cell != null) {
							cell.setState(Main.status);
							repaint();
						}
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
			clear(24);
		}
		
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear(int div) {
		grid = new Cell[getSize().width/div][getSize().height/div];
		g2d.setPaint(background);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		div--;
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				Cell cell = new Cell(i*div + i, j*div + j, i, j, div, div, g2d);
				cell.setState(CellState.BLANK);
				grid[i][j] = cell;
			}
		}
		Main.setPlayStatus(false);
		hasFinish = false;
		hasStart = false;
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
	
	private Cell findCell(Point point) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j].containsPoint(point)) {
					return grid[i][j];
				}
			}
		}
		return null;
	}
	
	private Cell findCellFromState(CellState state) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j].getState() == state) {
					return grid[i][j];
				}
			}
		}
		return null;
	}
	
	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}
	
	public boolean drawPath(boolean showNodes, boolean diagonals, int delay) {
		Finder finder = new Finder(grid, start, finish, diagonals, showNodes, delay);
		List<Cell> path = finder.findPath();
		
		if(path.isEmpty()) {
			return false;
		}
		
		finish.setState(CellState.FINISH);
		for(int i = 1; i < path.size(); i++) {
			try {
				Thread.sleep(delay/2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			path.get(i).fillSquare(Color.YELLOW, false);
		}
		repaint();
		
		return true;
	}
	
	public void clearPath() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j].getState() != CellState.WALL && grid[i][j].getState() != CellState.START && grid[i][j].getState() != CellState.FINISH) {
					grid[i][j].setState(CellState.BLANK);
				}
			}
		}
		repaint();
	}
	
	public void generateRandomMaze() {
		maze = new Maze(grid);
		maze.generateMaze();
		repaint();
	}
	
}
