package maze_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Cell;
import main.CellState;

public class Wilsons extends Maze {
	
	private final Cell[][] grid;
	private final Random random;
	private final List<Cell> cells;
	
	public Wilsons(Cell[][] grid) {
		this.grid = grid;
		this.random = new Random();
		this.cells = new ArrayList<Cell>();
	}

	public void generateMaze() {
		running = true;
		constructCells();
		
		Cell first = cells.get(random.nextInt(cells.size()));
		first.visited = true;
		cells.remove(first);
		while(!cells.isEmpty() && running) randomWalk(cells.get(random.nextInt(cells.size())));
	}
	
	private void randomWalk(Cell cell) {
		Cell curr = cell;
		while(!curr.visited && running) {
			int x = curr.x, y = curr.y;
			if(random.nextBoolean()) x = (random.nextBoolean() && curr.x - 2 >= 0) || curr.x + 2 >= grid.length ? curr.x - 2 : curr.x + 2;
			else y = (random.nextBoolean() && curr.y - 2 >= 0) || curr.y + 2 >= grid[0].length ? curr.y - 2 : curr.y + 2;
			
			curr.parent = grid[x][y];
			curr = grid[x][y];
		}
		
		curr = cell;
		while(!curr.visited && running) {
			int xdiff = curr.parent.x - curr.x, ydiff = curr.parent.y - curr.y;
			curr.visited = true;
			cells.remove(curr);
			curr = curr.parent;
			grid[curr.x - xdiff/2][curr.y - ydiff/2].setState(CellState.BLANK);
		}
	}
	
	private void constructCells() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
					grid[i][j].visited = false;
					cells.add(grid[i][j]);
				} else grid[i][j].setState(CellState.WALL);
			}
		}
	}
	
}
