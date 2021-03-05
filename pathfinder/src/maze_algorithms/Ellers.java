package maze_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.Cell;
import main.CellState;

public class Ellers extends Maze {
	
	private final Cell[][] grid;
	private final Random random;
	private Map<Integer, List<Cell>> sets;
	
	public Ellers(Cell[][] grid) {
		this.grid = grid;
		this.random = new Random();
		this.sets = new HashMap<Integer, List<Cell>>();
	}
	
	public void generateMaze() {
		running = true;
		constructCells();
		
		for(int x = 0; x < grid.length; x += 2) sets.put(x/2, listOf(grid[x][0]));
		for(int y = 0; y < grid[0].length && running; y += 2)
			generateRow(y, y + 2 >= grid[0].length);
	}
	
	private void generateRow(int y, boolean last) {
		//Randomly connects adjacent cells
		for(int x = 0; x < grid.length && running; x += 2) {
			int id = (int) grid[x][y].heuristic;
			
			if(x + 2 < grid.length && id != (int) grid[x + 2][y].heuristic && (random.nextBoolean() || last)) {
				grid[x + 1][y].setState(CellState.BLANK);
				merge(id, (int) grid[x + 2][y].heuristic);
			}
		}
		
		if(last) return;
		Map<Integer, List<Cell>> temp = new HashMap<Integer, List<Cell>>();
		for(List<Cell> set : sets.values()) {
			Collections.shuffle(set);
			int num = random.nextInt(set.size());
			
			//Randomly adds vertical connections downward, at least 1 per row
			List<Cell> newSet = new ArrayList<Cell>();
			int id = set.get(0).x/2;
			for(int i = 0; i <= num && running; i++) {
				grid[set.get(i).x][y + 1].setState(CellState.BLANK);
				
				Cell cell = grid[set.get(i).x][y + 2];
				cell.heuristic = id;
				newSet.add(cell);
			}
			temp.put(id, newSet);
			
			//Puts the remaining cells into their own sets
			for(int i = num + 1; i < set.size(); i++) {
				Cell cell = grid[set.get(i).x][y + 2];
				temp.put(cell.x/2, listOf(cell));
			}
		}
		sets = temp;
	}
	
	private List<Cell> listOf(Cell cell) {
		List<Cell> list = new ArrayList<Cell>();
		list.add(cell);
		return list;
	}
	
	private void merge(int id1, int id2) {
		int newID = id2;
		List<Cell> input = sets.get(id1);
		List<Cell> output = sets.get(id2);
		
		if(input.size() > output.size()) {
			newID = id1;
			
			List<Cell> input2 = input;
			input = output;
			output = input2;
		}
		
		for(Cell cell : input) {
			cell.heuristic = newID;
			output.add(cell);
		}
		
		sets.remove(newID == id1 ? id2 : id1);
	}
	
	private void constructCells() {
		for(int y = 0; y < grid[0].length && running; y++) {
			for(int x = 0; x < grid.length && running; x++) {
				if(x%2 == 0 && y%2 == 0) grid[x][y].setState(CellState.BLANK);
				else grid[x][y].setState(CellState.WALL);
				
				grid[x][y].heuristic = x/2;
			}
		}
	}
	
}
