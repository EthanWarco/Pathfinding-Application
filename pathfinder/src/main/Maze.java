package main;

import java.util.Random;

public class Maze {
	
	private static final int[][] orders = {{4,3,2,1},
										   {4,3,1,2},
										   {4,2,3,1},
										   {4,2,1,3},
										   {4,1,3,2},
										   {4,1,2,3},
										   {3,4,2,1},
										   {3,4,1,2},
										   {3,2,4,1},
										   {3,2,1,4},
										   {3,1,4,2},
										   {3,1,2,4},
										   {2,4,3,1},
										   {2,4,1,3},
										   {2,3,4,1},
										   {2,3,1,4},
										   {2,1,4,3},
										   {2,1,3,4},
										   {1,4,3,2},
										   {1,4,2,3},
										   {1,3,4,2},
										   {1,3,2,4},
										   {1,2,4,3},
										   {1,2,3,4}};
	
	private final Cell[][] grid;
	private final Random random;
	private static boolean running = true;
	
	public Maze(Cell[][] grid) {
		this.grid = grid;
		this.random = new Random();
	}
	
	//maze generation using depth first search
	public void generateMaze() {
		running = true;
		constructCells();
		exploreNeighbors(grid[0][0]);
	}
	
	private void exploreNeighbors(Cell cell) {
		cell.visited = true;
		if(!running) return;
		
		int[] order = orders[random.nextInt(orders.length)];
		for(int i = 0; i < 4; i++) {
			int dx = order[i] == 1 ? 2 : (order[i] == 2 ? -2 : 0);
			int dy = order[i] == 3 ? 2 : (order[i] == 4 ? -2 : 0);
			int x = cell.x + dx, y = cell.y + dy;
			
			if(x < grid.length && x >= 0 && y < grid[0].length && y >= 0 && !grid[x][y].visited) {
				grid[cell.x + dx/2][cell.y + dy/2].setState(CellState.BLANK);
				exploreNeighbors(grid[x][y]);
			}
		}
	}
	
	
	/*
	 * Helper Functions
	 */
	private void constructCells() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(i%2 == 0 && j%2 == 0) {
					grid[i][j].setState(CellState.BLANK);
					grid[i][j].visited = false;
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