package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import algorithms.PathFinder;


public class Main implements ActionListener {
	
	private static JFrame frame;
	private static JPanel toolbar;
	private static JButton randomize;
	private static JButton clearGrid;
	private static JButton clearPath;
	private static JButton quit;
	private static JButton play;
	private static JButton randomMaze;
	private static JComboBox<String> actions;
	private static JComboBox<Integer> densities;
	private static JComboBox<Integer> sizes;
	private static JComboBox<String> pathfindingAlgorithms;
	private static JLabel blank;
	private static JLabel actionsLabel;
	private static JLabel randomizedDensity;
	private static JLabel sizesLabel;
	private static JLabel pathAlgLabel;
	private static JLabel noPathFound;
	private static JLabel delayLabel;
	private static JSlider delaySlider;
	private static JCheckBox diagonals;
	private static JCheckBox showNodes;
	private static int delay = 10;
	public static int div = 24;
	private static final Integer[] factors = findFactors(div);
	
	protected static CellState status = CellState.BLANK;
	public static DrawingArea mazePanel;
	
	public Main() {
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		Color toolbarColor = new Color(228, 228, 228);
		Font toolbarFont = new Font(FontUIResource.DIALOG, Font.BOLD, 15);
		mazePanel = new DrawingArea();
		
		
		
		
		quit = new JButton("Quit");
		quit.setPreferredSize(new Dimension(300, 40));
		quit.setFont(toolbarFont);
		quit.addActionListener(this);
		
		clearGrid = new JButton("Clear Grid");
		clearGrid.setPreferredSize(new Dimension(300, 40));
		clearGrid.setFont(toolbarFont);
		clearGrid.addActionListener(this);
		
		clearPath = new JButton("Clear Path");
		clearPath.setPreferredSize(new Dimension(300, 40));
		clearPath.setFont(toolbarFont);
		clearPath.addActionListener(this);
		
		randomizedDensity = new JLabel("Density:");
		randomizedDensity.setPreferredSize(new Dimension(100, 30));
		randomizedDensity.setFont(toolbarFont);
		
		Integer[] nums = {2, 3, 4, 5, 6};
		densities = new JComboBox<>(nums);
		densities.setPreferredSize(new Dimension(200, 30));
		densities.addActionListener(this);
		densities.setFont(toolbarFont);
		
		randomize = new JButton("Randomize");
		randomize.setPreferredSize(new Dimension(300, 40));
		randomize.setFont(toolbarFont);
		randomize.addActionListener(this);
		
		String[] types = {"Blank", "Wall", "Start", "Finish"};
		actions = new JComboBox<>(types);
		actions.setPreferredSize(new Dimension(200, 30));
		actions.setFont(toolbarFont);
		actions.addActionListener(this);
		
		actionsLabel = new JLabel("Actions:");
		actionsLabel.setPreferredSize(new Dimension(100, 30));
		actionsLabel.setFont(toolbarFont);
		
		sizesLabel = new JLabel("Cell Size:");
		sizesLabel.setPreferredSize(new Dimension(100, 30));
		sizesLabel.setFont(toolbarFont);
		
		sizes = new JComboBox<Integer>(factors);
		sizes.setPreferredSize(new Dimension(200, 30));
		sizes.setFont(toolbarFont);
		sizes.setSelectedIndex(factors.length-1);
		sizes.addActionListener(this);
		
		pathAlgLabel = new JLabel("Algorithm:");
		pathAlgLabel.setPreferredSize(new Dimension(100, 30));
		pathAlgLabel.setFont(toolbarFont);
		
		String[] algs = {"A*", "Depth First Search", "Breadth First Search"};
		pathfindingAlgorithms = new JComboBox<String>(algs);
		pathfindingAlgorithms.setPreferredSize(new Dimension(200, 30));
		pathfindingAlgorithms.setFont(toolbarFont);
		pathfindingAlgorithms.setSelectedIndex(0);
		pathfindingAlgorithms.addActionListener(this);
		
		play = new JButton("Play");
		play.setPreferredSize(new Dimension(300, 40));
		play.setFont(toolbarFont);
		play.setEnabled(false);
		play.addActionListener(this);
		
		diagonals = new JCheckBox("Diagonal Movement:                                ");
		diagonals.setPreferredSize(new Dimension(300, 30));
		diagonals.setBackground(toolbarColor);
		diagonals.setFont(toolbarFont);
		diagonals.setHorizontalTextPosition(JCheckBox.LEFT);
		diagonals.addActionListener(this);
		
		noPathFound = new JLabel("");
		noPathFound.setFont(toolbarFont);
		noPathFound.setHorizontalAlignment(JLabel.CENTER);
		noPathFound.setPreferredSize(new Dimension(300, 30));
		
		showNodes = new JCheckBox("Color Checked Nodes:                            ");
		showNodes.setPreferredSize(new Dimension(300, 30));
		showNodes.setBackground(toolbarColor);
		showNodes.setFont(toolbarFont);
		showNodes.setHorizontalTextPosition(JCheckBox.LEFT);
		showNodes.addActionListener(this);
		
		delayLabel = new JLabel("Delay: " + 10 + " ms");
		delayLabel.setFont(toolbarFont);
		delayLabel.setHorizontalAlignment(JLabel.CENTER);
		delayLabel.setPreferredSize(new Dimension(300, 30));
		
		delaySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		delaySlider.setPreferredSize(new Dimension(300, 30));
		delaySlider.setBackground(toolbarColor);
		delaySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				delayLabel.setText("Delay: " + delaySlider.getValue() + " ms");
				delay = delaySlider.getValue();
			}
		});
		
		randomMaze = new JButton("Randomized Maze");
		randomMaze.setPreferredSize(new Dimension(300, 40));
		randomMaze.setFont(toolbarFont);
		randomMaze.addActionListener(this);
		
		toolbar = new JPanel();
		toolbar.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(186, 198, 205), 2), "Pathfinding", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, toolbarFont));
		toolbar.setBounds(10, 0, width/5 - 20, height - 10);
		toolbar.setBackground(toolbarColor);
		toolbar.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		toolbar.add(quit, gbc);

		gbc.gridy = 1;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 5));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		toolbar.add(clearGrid, gbc);
		
		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 5));
		toolbar.add(blank, gbc);

		gbc.gridy++;
		toolbar.add(clearPath, gbc);

		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 100));
		toolbar.add(blank, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		toolbar.add(randomizedDensity, gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		toolbar.add(densities, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 5));
		toolbar.add(blank, gbc);

		gbc.gridy++;
		toolbar.add(randomize, gbc);

		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 100));
		toolbar.add(blank, gbc);

		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 5));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		toolbar.add(randomMaze, gbc);
		
		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 100));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 2;
		toolbar.add(actionsLabel, gbc);
		
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		toolbar.add(actions, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 10));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 2;
		toolbar.add(sizesLabel, gbc);
		
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		toolbar.add(sizes, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 10));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 2;
		toolbar.add(pathAlgLabel, gbc);
		
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		toolbar.add(pathfindingAlgorithms, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 50));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		toolbar.add(diagonals, gbc);
		
		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 5));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		toolbar.add(showNodes, gbc);
		
		gbc.gridy++;
		blank = new JLabel("");
		blank.setPreferredSize(new Dimension(300, 100));
		toolbar.add(blank, gbc);
		
		gbc.gridy++;
		toolbar.add(delayLabel, gbc);
		
		gbc.gridy++;
		toolbar.add(delaySlider, gbc);
		
		gbc.gridy++;
		toolbar.add(noPathFound, gbc);
		
		gbc.gridy++;
		toolbar.add(play, gbc);
		
		
		
		
		frame = new JFrame("Pathfinder");
		frame.setFocusable(true);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize(new Dimension(width, height));
		frame.setResizable(false);
		frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.getContentPane().setBackground(toolbarColor);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(null);
		frame.getContentPane().add(mazePanel);
		frame.getContentPane().add(toolbar);
		frame.pack();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			public void run() {
				if(e.getSource() == clearGrid) {
					Maze.stopMazeGeneration();
					PathFinder.stopPathFinding();
					mazePanel.clear(div);
				} else if(e.getSource() == randomize) {
					Maze.stopMazeGeneration();
					PathFinder.stopPathFinding();
					mazePanel.clear(div);
					mazePanel.randomize((int)densities.getSelectedItem());
				} else if(e.getSource() == clearPath) {
					mazePanel.clearPath();
				} else if(e.getSource() == actions) {
				
					switch((String)actions.getSelectedItem()) {
						case "Blank":	status = CellState.BLANK;
										break;
						case "Wall":	status = CellState.WALL;
										break;
						case "Finish":	status = CellState.FINISH;
										break;
						case "Start":	status = CellState.START;
										break;
					}
				} else if(e.getSource() == sizes) {
					Maze.stopMazeGeneration();
					PathFinder.stopPathFinding();
					div = (int) sizes.getSelectedItem();
					mazePanel.clear(div);
				} else if(e.getSource() == quit) {
					Maze.stopMazeGeneration();
					PathFinder.stopPathFinding();
					frame.dispose();
				} else if(e.getSource() == play) {
					Maze.stopMazeGeneration();
					PathFinder.stopPathFinding();
					for(Component comp : toolbar.getComponents()) {
						if(comp != quit) {
							comp.setEnabled(false);
						}
					}
					
					mazePanel.setCanDraw(false);
					boolean found = mazePanel.drawPath((String)pathfindingAlgorithms.getSelectedItem(), showNodes.isSelected(), diagonals.isSelected(), delay);
					
					if(found) {
						noPathFound.setText("");
					} else {
						noPathFound.setText("No path found!");
					}
					
					for(Component comp : toolbar.getComponents()) {
						comp.setEnabled(true);
					}
					mazePanel.setCanDraw(true);
				} else if(e.getSource() == randomMaze) {
					mazePanel.generateRandomMaze();
				} else if(e.getSource() == pathfindingAlgorithms) {
					//Removes Option to allow diagonals when using BFS
					boolean bfs = pathfindingAlgorithms.getSelectedItem() == "Breadth First Search";
					if(bfs) diagonals.setSelected(false);
					diagonals.setEnabled(!bfs);
				}
			}
		}).start();
	}
	
	//turns the play button on/off
	public static void setPlayStatus(boolean value) {
		play.setEnabled(value);
	}
	
	//finds factors of number rather than making them constant, to allow the "div" variable to be custom
	private static Integer[] findFactors(int num) {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i = 2; i < num; i++) {
			if(num%i == 0) {
				nums.add(i);
			}
		}
		nums.add(num);
		Integer[] array = nums.toArray(new Integer[(nums.size())]);
		return array;
	}
	
	
	
	public static void main(String[] args) {
		new Main();
	}
	
}
