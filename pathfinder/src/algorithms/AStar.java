package algorithms;

import java.awt.Color;
import java.util.PriorityQueue;

import main.Cell;
import main.CellState;

public class AStar extends PathFinder {
	
	/*
	 * A*
	 */
	
	private final Cell[][] grid;
	private final boolean[] visited;
	private final int startX, startY;
	private final int endX, endY;
	private final boolean diagonal;
	private final PriorityQueue<Cell> open;
	private final boolean showCheckedNodes;
	private final int delay;
	private final Cell end;
	private Cell current;
	
	public AStar(Cell[][] grid, Cell start, Cell end, boolean diagonal, boolean showCheckedNodes, int delay) {
		this.grid = grid;
		this.startX = start.x;
		this.startY = start.y;
		this.endX = end.x;
		this.endY = end.y;
		this.diagonal = diagonal;
		this.open = new PriorityQueue<Cell>();
		this.visited = new boolean[grid.length*grid[0].length];
		this.current = grid[startX][startY];
		this.showCheckedNodes = showCheckedNodes;
		this.delay = delay;
		this.end = end;
	}
	
	public boolean findPath() {
		running = true;
		visited[current.x + current.y*grid.length] = true;
		addNeighborsToOpenList();
		while((current.x != endX || current.y != endY) && running) {
			if(open.isEmpty()) return false;
			current = open.poll();
			visited[current.x + current.y*grid.length] = true;
			
			if(showCheckedNodes) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				current.fillSquare(checkColor, false);
			}
			addNeighborsToOpenList();
		}
		while(current.x != startX || current.y != startY && running) {
			current = current.parent;
			current.fillSquare(Color.YELLOW, false);
		}
		
		return true;
	}
	
	private void addNeighborsToOpenList() {
		for(int dx = -1; dx <= 1; dx++) {
			for(int dy = -1; dy <= 1; dy++) {
				int x = current.x+dx, y = current.y+dy;
				
				if(!diagonal && dx != 0 && dy != 0) continue;
				
				if(eligible(x, y, dx, dy)) {
					visited[x + y*grid.length] = true;
					Cell cell = grid[x][y];
					cell.parent = current;
					cell.heuristic = distance(x, y, end);
					
					cell.stepCost = cell.parent.stepCost + 1;
					if(dx != 0 && dy != 0) cell.stepCost += 0.41421356237;
					
					open.add(cell);
					if(showCheckedNodes) cell.fillSquare(openColor, false);
				}
			}
		}
	}
	
	private float distance(int x, int y, Cell cell) {
		float xdiff = Math.abs(cell.x - x);
		float ydiff = Math.abs(cell.y - y);
		return xdiff + ydiff;
	}
	
	private boolean eligible(int x, int y, int dx, int dy) {
		return (dx != 0 || dy != 0) && x >= 0 && x < grid.length && y >= 0
				&& y < grid[0].length && grid[x][y].getState() != CellState.WALL && !visited[x + y*grid.length];
	}
	
}
