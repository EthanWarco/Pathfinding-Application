package pathfinding_algorithms;

import java.awt.Color;

public abstract class PathFinder {
	
	protected static final Color checkColor = new Color(201, 62, 253);
	protected static final Color openColor = new Color(60, 157, 255);
	
	protected static boolean running;
	
	public abstract boolean findPath();
	
	public static void stopPathFinding() {
		running = false;
	}
	
}
