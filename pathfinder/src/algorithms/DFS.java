package algorithms;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import main.Cell;
import main.CellState;

public class DFS extends PathFinder {
	
	/*
	 * Depth First Search
	 */
	
	private final Cell[][] grid;
	private final Set<Cell> visited;
	private final boolean diagonal;
	private final boolean showCheckedNodes;
	private final int delay;
	private final Cell end;
	private final Cell start;
	
	public DFS(Cell[][] grid, Cell start, Cell end, boolean diagonal, boolean showCheckedNodes, int delay) {
		this.grid = grid;
		this.visited = new HashSet<Cell>();
		this.start = start;
		this.end = end;
		this.diagonal = diagonal;
		this.showCheckedNodes = showCheckedNodes;
		this.delay = delay;
	}
	
	public boolean findPath() {
		running = true;
		boolean found = false;
		try {
			found = exploreNeighbors(start);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return found;
	}
	
	private boolean exploreNeighbors(Cell cell) throws InterruptedException {
		if(showCheckedNodes) cell.fillSquare(checkColor, false);
		if(cell == end) return true;
		visited.add(cell);
		
		int x = cell.x, y = cell.y;
		int w = grid.length, h = grid[0].length;
		
		for(int dx = -1; dx <= 1; dx++) {
			for(int dy = -1; dy <= 1; dy++) {
				int newX = x+dx, newY = y+dy;
				
				if(!diagonal && dx != 0 && dy != 0) continue;
				if((dx != 0 || dy != 0) && newX < w && newX >= 0 && newY < h && newY >= 0 && !visited.contains(grid[newX][newY]) && grid[newX][newY].getState() != CellState.WALL) {
					Thread.sleep(delay);
					
					if(exploreNeighbors(grid[newX][newY])) {
						if(!running) return false;
						cell.fillSquare(Color.YELLOW, false);
						return true;
					}
				}
			}
		}
		
		
		return false;
	}
	
}
