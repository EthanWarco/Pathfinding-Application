package algorithms;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import main.Cell;
import main.CellState;

public class BFS extends PathFinder {
	
	/*
	 * Breadth First Search
	 */
	
	private final Cell[][] grid;
	private final boolean diagonal;
	private final boolean showCheckedNodes;
	private final int delay;
	private final Cell end;
	private final Cell start;
	
	public BFS(Cell[][] grid, Cell start, Cell end, boolean diagonal, boolean showCheckedNodes, int delay) {
		this.grid = grid;
		this.start = start;
		this.end = end;
		this.diagonal = diagonal;
		this.showCheckedNodes = showCheckedNodes;
		this.delay = delay;
	}
	
	public boolean findPath() {
		running = true;
		Queue<Cell> queue = new LinkedList<Cell>();
		boolean[] visited = new boolean[grid.length*grid[0].length];
		queue.add(start);
		
		while(!queue.isEmpty() && running) {
			Cell curr = queue.poll();
			if(showCheckedNodes) curr.fillSquare(checkColor, false);
			if(curr == end) {
				drawPath();
				return true;
			}
			
			for(int dx = -1; dx <= 1; dx++) {
				for(int dy = -1; dy <= 1; dy++) {
					if(!diagonal && dx != 0 && dy != 0) continue;
					int x = curr.x + dx, y = curr.y + dy;
					
					if((dx != 0 || dy != 0) && x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && !visited[x + grid.length*y] && grid[x][y].getState() != CellState.WALL) {
						visited[x + grid.length*y] = true;
						if(showCheckedNodes) grid[x][y].fillSquare(openColor, false);
						grid[x][y].parent = curr;
						queue.offer(grid[x][y]);
					}
				}
			}
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		return false;
	}
	
	private void drawPath() {
		Cell cell = end;
		while(cell != start) {
			cell.fillSquare(Color.YELLOW, false);
			cell = cell.parent;
		}
		cell.fillSquare(Color.YELLOW, false);
	}
	
}
