package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import main.Cell;
import main.CellState;

public class AStar extends PathFinder {
	
	private final Cell[][] grid;
	private final int startX, startY;
	private final int endX, endY;
	private final boolean diagonal;
	private final PriorityQueue<Cell> open;
	private final List<Cell> closed;
	private final boolean showCheckedNodes;
	private final int delay;
	private final Cell end;
	private Cell current;
	
	public AStar(Cell[][] grid, Cell start, Cell end, boolean diagonal, boolean showCheckedNodes, int delay) {
		this.grid = grid;
		this.startX = start.graphX;
		this.startY = start.graphY;
		this.endX = end.graphX;
		this.endY = end.graphY;
		this.diagonal = diagonal;
		this.open = new PriorityQueue<Cell>();
		this.closed = new ArrayList<Cell>();
		this.current = grid[startX][startY];
		this.showCheckedNodes = showCheckedNodes;
		this.delay = delay;
		this.end = end;
	}
	
	public boolean findPath() {
		running = true;
		closed.add(current);
		addNeighborsToOpenList();
		while((current.graphX != endX || current.graphY != endY) && running) {
			if(open.isEmpty()) return false;
			current = open.poll();
			closed.add(current);
			
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
		while(current.graphX != startX || current.graphY != startY && running) {
			current = current.parent;
			current.fillSquare(Color.YELLOW, false);
		}
		
		return true;
	}
	
	private void addNeighborsToOpenList() {
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				int newX = current.graphX+x, newY = current.graphY+y;
				
				if(!diagonal && x != 0 && y != 0) continue;
				
				if((x != 0 || y != 0) && newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {
					if(grid[newX][newY].getState() != CellState.WALL && !open.contains(grid[newX][newY]) && !closed.contains(grid[newX][newY])) {
						Cell cell = grid[newX][newY];
						cell.parent = current;
						cell.heuristic = distance(x, y, end);
						
						cell.stepCost = cell.parent.stepCost + 1;
						if(x != 0 && y != 0) cell.stepCost += .414;
						
						open.add(cell);
						if(showCheckedNodes) cell.fillSquare(openColor, false);
					}
				}
			}
		}
	}
	
	private float distance(int dx, int dy, Cell cell) {
		float xdiff = cell.graphX - (current.graphX + dx);
		float ydiff = cell.graphY - (current.graphY + dy);
		return Math.abs(xdiff) + Math.abs(ydiff);
	}
	
}
