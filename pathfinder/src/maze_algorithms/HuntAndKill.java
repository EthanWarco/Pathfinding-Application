package maze_algorithms;

import java.util.Random;

import main.Cell;
import main.CellState;

public class HuntAndKill extends Maze {
	
	private final Cell[][] grid;
	private final Random random;
	
	public HuntAndKill(Cell[][] grid) {
		this.grid = grid;
		this.random = new Random();
	}

	public void generateMaze() {
		running = true;
		constructCells();
		
		Cell curr = grid[random.nextInt(grid.length/2)*2][random.nextInt(grid[0].length/2)*2];
		while(curr != null) {
			randomWalk(curr);
			curr = hunt();
		}
	}
	
	private void randomWalk(Cell start) {
		Cell curr = start;
		while(curr != null) {
			curr.visited = true;
			curr = chooseRandomNeighbor(curr, false);
		}
	}
	
	private Cell hunt() {
		for(int x = 0; x < grid.length; x += 2) {
			for(int y = 0; y < grid[x].length; y += 2) {
				if(grid[x][y].visited) continue;
				
				Cell visited = chooseRandomNeighbor(grid[x][y], true);
				if(visited != null) return visited;
			}
		}
		return null;
	}
	
	private Cell chooseRandomNeighbor(Cell cell, boolean visited) {
		int[] order = orders[random.nextInt(orders.length)];
		for(int i = 0; i < 4; i++) {
			int dx = order[i] == 1 ? 2 : (order[i] == 2 ? -2 : 0);
			int dy = order[i] == 3 ? 2 : (order[i] == 4 ? -2 : 0);
			int x = cell.x + dx, y = cell.y + dy;
			
			if(x < grid.length && x >= 0 && y < grid[0].length && y >= 0 && grid[x][y].visited == visited) {
				grid[cell.x + dx/2][cell.y + dy/2].setState(CellState.BLANK);
				return grid[x][y];
			}
		}
		return null;
	}
	
	private void constructCells() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
					grid[i][j].visited = false;
				} else grid[i][j].setState(CellState.WALL);
			}
		}
	}

}
