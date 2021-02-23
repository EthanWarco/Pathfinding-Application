package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Cell implements Comparable<Cell> {
	
	public int screenX, screenY;
	public int graphX, graphY;
	public int width, height;
	public float heuristic;
	public float stepCost;
	public Cell parent;
	public boolean visited = false;
	private CellState state = CellState.BLANK;
	private CellState previousState = state;
	private Polygon cell = new Polygon();
	private Color background = new Color(211, 215, 239);
	private Graphics2D g2d;
	private Color gridColor = new Color(168, 173, 223);
	private DrawingArea canvas = Main.mazePanel;
	
	public Cell(int screenX, int screenY, int graphX, int graphY, int width, int height, Graphics2D g2d) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.graphX = graphX;
		this.graphY = graphY;
		this.g2d = g2d;
		cell.addPoint(screenX, screenY);
		cell.addPoint(screenX + width, screenY);
		cell.addPoint(screenX + width, screenY + height);
		cell.addPoint(screenX, screenY + height);
	}
	
	public Cell(Cell cell, Cell parent) {
		this.screenX = cell.screenX;
		this.screenY = cell.screenY;
		this.graphX = cell.graphX;
		this.graphY = cell.graphY;
		this.g2d = cell.g2d;
		this.cell = cell.cell;
		this.parent = parent;
	}
	
	public void setState(CellState state) {
		previousState = this.state;
		this.state = state;
		switch(state) {
			case BLANK:		fillSquare(background, true);
							break;
			case FINISH:	fillSquare(Color.GREEN, false);
							canvas.hasFinish = true;
							canvas.finish = this;
							break;
			case START:		fillSquare(Color.RED, false);
							canvas.hasStart = true;
							canvas.start = this;
							break;
			case WALL:		fillSquare(Color.BLACK, false);
							break;
		}
		if(previousState == CellState.FINISH && state != CellState.FINISH) {
			canvas.hasFinish = false;
		} else if(previousState == CellState.START && state != CellState.START) {
			canvas.hasStart = false;
		}
		if(canvas.hasStart && canvas.hasFinish) {
			Main.setPlayStatus(true);
		} else {
			Main.setPlayStatus(false);
		}
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
		if(drawGrid) {
			g2d.setPaint(gridColor);
			g2d.draw(cell);
		} else {
			g2d.draw(cell);
		}
		
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
