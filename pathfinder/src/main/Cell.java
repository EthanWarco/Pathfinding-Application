package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Cell implements Comparable<Cell> {
	
	public int x, y;
	public int size;
	public float heuristic;
	public float stepCost;
	public Cell parent;
	public boolean visited = false;
	private CellState state = CellState.BLANK;
	private Color background = new Color(211, 215, 239);
	private Color gridColor = new Color(168, 173, 223);
	private Polygon cell = new Polygon();
	private Graphics2D g2d;
	private static DrawingArea canvas = Main.mazePanel;
	
	public Cell(int screenX, int screenY, int graphX, int graphY, int size, Graphics2D g2d) {
		this.x = graphX;
		this.y = graphY;
		this.g2d = g2d;
		cell.addPoint(screenX, screenY);
		cell.addPoint(screenX + size, screenY);
		cell.addPoint(screenX + size, screenY + size);
		cell.addPoint(screenX, screenY + size);
	}
	
	public Cell(Cell cell, Cell parent) {
		this.x = cell.x;
		this.y = cell.y;
		this.g2d = cell.g2d;
		this.cell = cell.cell;
		this.parent = parent;
	}
	
	public void setState(CellState state) {
		switch(state) {
			case BLANK:		fillSquare(background, true);
							break;
			case FINISH:	fillSquare(Color.GREEN, false);
							canvas.finish = this;
							break;
			case START:		fillSquare(Color.RED, false);
							canvas.start = this;
							break;
			case WALL:		fillSquare(Color.BLACK, false);
							break;
		}
		if(this.state == CellState.FINISH && state != CellState.FINISH) canvas.finish = null;
		else if(this.state == CellState.START && state != CellState.START) canvas.start = null;
		
		Main.setPlayStatus(canvas.start != null && canvas.finish != null);
		this.state = state;
	}
	
	public CellState getState() {
		return state;
	}
	
	public boolean containsPoint(Point p) {
		return cell.contains(p);
	}
	
	public void fillSquare(Color color, boolean drawGrid) {
		g2d.setPaint(color);
		
		g2d.fill(cell);
		if(drawGrid) g2d.setPaint(gridColor);
		
		g2d.draw(cell);
		g2d.setPaint(Color.BLACK);
		canvas.repaint();
	}

	@Override
	public int compareTo(Cell that) {
		if(heuristic + stepCost == that.heuristic + that.stepCost) {
			if(stepCost == that.stepCost) {
				return Float.compare(stepCost, that.stepCost);
			} else {
				return Float.compare(heuristic, that.heuristic);
			}
		} else {
			return Float.compare(heuristic + stepCost, that.heuristic + that.stepCost);
		}
	}
	
}
