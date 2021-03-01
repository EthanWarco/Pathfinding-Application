package maze_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Cell;
import main.CellState;

public class RandomizedKruskal extends Maze {
	
	/*
	 * The way I implemented the union-find algorithm for this was I used heuristic variable of the cell as rank and
	 * and the parent variable of the cell as which set it belongs to.
	 */

	private final Cell[][] grid;
	private final List<Cell> walls;
	
	public RandomizedKruskal(Cell[][] grid) {
		this.grid = grid;
		this.walls = new ArrayList<Cell>();
	}

	public void generateMaze() {
		running = true;
		constructCells();
		for(int i = 0; i < walls.size() && running; i++) {
			Cell cell = walls.get(i);
			
			Cell side1 = null, side2 = null;
			if(cell.y % 2 == 0) {
				if(cell.x-1 >= 0) side1 = grid[cell.x-1][cell.y];
				if(cell.x+1 < grid.length) side2 = grid[cell.x+1][cell.y];
			} else {
				if(cell.y-1 >= 0) side1 = grid[cell.x][cell.y-1];
				if(cell.y+1 < grid[0].length) side2 = grid[cell.x][cell.y+1];
			}
			
			Cell root1 = find(side1), root2 = find(side2);
			if(root1 != root2) {
				cell.setState(CellState.BLANK);
				union(root1, root2);
			}
		}
	}
	
	private void constructCells() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j].heuristic = 0;
				grid[i][j].parent = grid[i][j];
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
				} else {
					grid[i][j].setState(CellState.WALL);
				}
				
				if(i%2 == 0 ^ j%2 == 0) walls.add(grid[i][j]);
			}
		}
		Collections.shuffle(walls);
	}
	
	
	/*
	 * Union-find algorithms
	 */
	
	private Cell find(Cell cell) {
		if(cell == null) return null;
		
		Cell curr = cell;
		while(curr.parent != curr) curr = curr.parent;
		Cell root = curr;
		
		//Path Compression
		while(curr != root) {
			Cell next = curr.parent;
			curr.parent = root;
			curr = next;
		}
		
		return root;
	}
	
	private void union(Cell root1, Cell root2) {
		if(root1 == null || root2 == null) return;
		
		if(root1.heuristic < root2.heuristic) root1.parent = root2;
		else if(root1.heuristic > root2.heuristic) root2.parent = root1;
		else {
			root1.parent = root2;
			root1.heuristic++;
		}
	}

}
