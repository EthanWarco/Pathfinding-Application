package maze_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Cell;
import main.CellState;

public class GrowingTree extends Maze{
	
	//The following implementation picks the top cell from the list 50% of the time, and picks at random 50% of the time
	
	private final Cell[][] grid;
	private final Random random;
	private final List<Cell> cells;
	
	public GrowingTree(Cell[][] grid) {
		this.grid = grid;
		this.random = new Random();
		this.cells = new ArrayList<Cell>();
	}
	
	public void generateMaze() {
		running = true;
		constructCells();
		
		Cell curr = grid[random.nextInt(grid.length/2)*2][random.nextInt(grid[0].length/2)*2];
		while(curr != null && running) {
			randomWalk(curr);
			curr = nextCell(0.5);
		}
	}
	
	private Cell nextCell(double randomBias) {
		Cell curr;
		while(!cells.isEmpty() && running) {
			if(Math.random() < randomBias) curr = cells.get(random.nextInt(cells.size()));
			else curr = cells.get(cells.size()-1);
			
			Cell neighbor = chooseRandomNeighbor(curr);
			if(neighbor != null) return neighbor;
			
			cells.remove(curr);
		}
		
		return null;
	}
	
	private void randomWalk(Cell start) {
		Cell curr = start;
		while(curr != null && running) {
			cells.add(curr);
			curr.visited = true;
			curr = chooseRandomNeighbor(curr);
		}
	}
	
	private Cell chooseRandomNeighbor(Cell cell) {
		int[] order = orders[random.nextInt(orders.length)];
		for(int i = 0; i < 4 && running; i++) {
			int dx = order[i] == 1 ? 2 : (order[i] == 2 ? -2 : 0);
			int dy = order[i] == 3 ? 2 : (order[i] == 4 ? -2 : 0);
			int x = cell.x + dx, y = cell.y + dy;
			
			if(x < grid.length && x >= 0 && y < grid[0].length && y >= 0 && !grid[x][y].visited) {
				grid[cell.x + dx/2][cell.y + dy/2].setState(CellState.BLANK);
				return grid[x][y];
			}
		}
		return null;
	}
	
	private void constructCells() {
		for(int i = 0; i < grid.length && running; i++) {
			for(int j = 0; j < grid[i].length && running; j++) {
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
					grid[i][j].visited = false;
				} else {
					grid[i][j].setState(CellState.WALL);
				}
			}
		}
	}
	
}
