package maze_algorithms;

public abstract class Maze {
	
	protected static final int[][] orders = {{4,3,2,1},
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
	
	protected static boolean running = true;
	
	
	public abstract void generateMaze();
	
	public static final void stopMazeGeneration() {
		running = false;
	}
	
}
