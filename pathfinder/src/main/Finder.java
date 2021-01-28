package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Finder {
	
	/*
	 * Uses A* Pathfinding Algorithm
	 */
	
	private final Cell[][] grid;
	private final int startX, startY;
	private final int endX, endY;
	private final boolean diagonal;
	private final List<Cell> path;
	private final List<Cell> open;
	private final List<Cell> closed;
	private final boolean showCheckedNodes;
	private final int delay;
	private final Cell end;
	private static boolean running = true;
	private Cell current;
	
	public Finder(Cell[][] grid, Cell start, Cell end, boolean diagonal, boolean showCheckedNodes, int delay) {
		this.grid = grid;
		this.startX = start.graphX;
		this.startY = start.graphY;
		this.endX = end.graphX;
		this.endY = end.graphY;
		this.diagonal = diagonal;
		this.path = new ArrayList<Cell>();
		this.open = new ArrayList<Cell>();
		this.closed = new ArrayList<Cell>();
		this.current = grid[startX][startY];
		this.showCheckedNodes = showCheckedNodes;
		this.delay = delay;
		this.end = end;
	}
	
	public List<Cell> findPath() {
		running = true;
		closed.add(current);
		addNeighborsToOpenList();
		while((current.graphX != endX || current.graphY != endY) && running) {
			if(open.isEmpty()) {
				return null;
			}
			current = open.get(0);
			open.remove(0);
			closed.add(current);
			
			if(showCheckedNodes) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				current.fillSquare(new Color(201, 62, 253), false);
			}
			addNeighborsToOpenList();
		}
		while(current.graphX != startX || current.graphY != startY && running) {
			current = current.parent;
			path.add(0, current);
		}
		return path;
	}
	
	private float distance(int dx, int dy, Cell cell) {
		float xdiff = cell.graphX - (current.graphX + dx);
		float ydiff = cell.graphY - (current.graphY + dy);
		return Math.abs(xdiff) + Math.abs(ydiff);
	}
	
	private static boolean neighborInList(List<Cell> array, Cell cell) {
		return array.stream().anyMatch((c) -> (c.graphX == cell.graphX && c.graphY == cell.graphY));
	}
	
	private void addNeighborsToOpenList() {
		Cell cell;
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				if(!diagonal && x != 0 && y != 0) {
					continue;
				}
				if((x != 0 || y != 0) && current.graphX + x >= 0 && current.graphX + x < grid.length && current.graphY + y >= 0 && current.graphY + y < grid[0].length) {
					cell = new Cell(grid[current.graphX + x][current.graphY + y], current);
					cell.heuristic = distance(x, y, end);
					if(grid[current.graphX + x][current.graphY + y].getState() != CellState.WALL && !neighborInList(open, cell) && !neighborInList(closed, cell)) {
						cell.stepCost = cell.parent.stepCost + 1;
						if(x != 0 && y != 0) {
							cell.stepCost += .414;
						}
						open.add(cell);
						
						if(showCheckedNodes) {
							cell.fillSquare(new Color(60, 157, 255), false);
						}
						
					}
				}
			}
		}
		Collections.sort(open);
	}
	
	public static void stopPathFinding() {
		running = false;
	}
	
}
