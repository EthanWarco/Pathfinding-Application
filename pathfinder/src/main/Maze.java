package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {
	
	private final Cell[][] grid;
	private static boolean running = true;
	private Cell current;
	private int visits = 0;
	private int maxVisits = 0;
	
	public Maze(Cell[][] grid) {
		this.grid = grid;
	}
	
	//maze generation uses iterative backtracking
	public void generateMaze() {
		running = true;
		ArrayList<Cell> directions = new ArrayList<Cell>();
		constructCells();
		current = grid[0][0];
		Cell cell = pickNeighboringCell(findNeighboringCells(true));
		cell.visited = true;
		visits++;
		
		while(visits < maxVisits && running) {
			if(cell == null) {
				cell = directions.get(0);
				directions.remove(0);
			}
			removeWallBetween(cell);
			
			current = cell;
			directions.add(current);
			cell = pickNeighboringCell(findNeighboringCells(true));
			if(cell != null) {
				cell.visited = true;
				visits++;
			}
		}
	}
	
	private Cell pickNeighboringCell(List<Cell> neighbors) {
		Random random = new Random();
		
		Cell cell = null;
		if(!neighbors.isEmpty()) {
			cell = neighbors.get(random.nextInt(neighbors.size()));
			cell.setState(CellState.BLANK);
		}
		
		return cell;
	}
	
	private List<Cell> findNeighboringCells(boolean onlyUnvisitedCells) {
		List<Cell> neighbors = new ArrayList<Cell>();
		
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				if((x != 0 && y != 0)) {
					continue;
				}
				if((x != 0 || y != 0) && current.graphX + x*2 >= 0 && current.graphX + x*2 < grid.length && current.graphY + y*2 >= 0 && current.graphY + y*2 < grid[0].length) {
					if(onlyUnvisitedCells) {
						if(!grid[current.graphX + x*2][current.graphY + y*2].visited) {
							neighbors.add(grid[current.graphX + x*2][current.graphY + y*2]);
						}
					} else {
						if(grid[current.graphX + x*2][current.graphY + y*2].visited) {
							neighbors.add(grid[current.graphX + x*2][current.graphY + y*2]);
						}
					}
				}
			}
		}
		return neighbors;
	}
	
	private void removeWallBetween(Cell cell) {
		int xd = cell.graphX - current.graphX;
		int yd = cell.graphY - current.graphY;
		grid[cell.graphX - xd/2][cell.graphY - yd/2].setState(CellState.BLANK);
	}
	
	private void constructCells() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
					grid[i][j].visited = false;
					maxVisits++;
				} else {
					grid[i][j].setState(CellState.WALL);
					grid[i][j].visited = true;
				}
			}
		}
	}
	
	public static void stopMazeGeneration() {
		running = false;
	}

}
