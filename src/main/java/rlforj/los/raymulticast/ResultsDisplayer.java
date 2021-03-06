package rlforj.los.raymulticast;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * <p>A ResultsDisplayer object provides views of the results generated by an 
 * {@link MultiRaysCaster} object. Provided here for utility, and includes 
 * a sample main method for demonstration.</p> 
 * 
 */
public class ResultsDisplayer extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String END_LINE = System.getProperty("line.separator");
	private static final int PREFERRED_SIZE = 600;
	
	private int tileSize;
	private World mapData;
	private RayData[][] resultData;
	private Point origin;
	
	public ResultsDisplayer() {
		this.setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
	}
	
	
	public void assignData(World map, 
						   RayData[][] results, 
						   Point origin) {
		this.mapData = map;
		this.resultData = results;
		this.origin = origin;
		this.tileSize = (PREFERRED_SIZE / mapData.getSize());
	}
	
	
	/**
	 * <p>Prints {@code resultData} to System.out using the {@link RayData.toChar()} 
	 * method.</p>
	 */
	public void displayText() {
		StringBuilder displayText = new StringBuilder();
		for(int y = 0; y < mapData.getSize(); y++) {
			for(int x = 0; x < mapData.getSize(); x++) {
				if(mapData.obstructionAt(x, y)) displayText.append("W ");
				else if((x == origin.x) && (y == origin.y)) displayText.append("V ");
				else if(resultData[x][y] == null) displayText.append(". ");
				else displayText.append(resultData[x][y].toChar() + " ");
			}
			displayText.append(END_LINE);
		}
		System.out.println(displayText.toString());
	}
	
	
	public void paintComponent(Graphics g) {
		if(mapData == null) return;
		
		// painting the background with the 'null' color
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, PREFERRED_SIZE, PREFERRED_SIZE);

		RayData currentData;
		for(int y = 0; y < resultData[0].length; y++) {
			for(int x = 0; x < resultData.length; x++) {
				currentData = resultData[x][y];
				if(mapData.obstructionAt(x, y)) {
					g.setColor(Color.RED);
					g.fillRect((x * tileSize), 
							   (y * tileSize),
							   tileSize, 
							   tileSize);
				}
				else if(currentData != null) paintData(currentData, x, y, g);
			}
		}

		// painting the origin
		g.setColor(Color.BLUE);
		g.fillRect(origin.x * tileSize, origin.y * tileSize, tileSize, tileSize);
	}
	
	
	// Handles painting of a single tile.
	private void paintData(RayData rayData, int mapX, int mapY, Graphics g) {
		boolean obscure = false;

		if(rayData.obscure()) obscure = true;
		
		if(rayData.ignore) {
			g.setColor(Color.DARK_GRAY);
		}
		else if(obscure) {
			g.setColor(Color.BLACK);
		}
		else g.setColor(Color.WHITE);
		
		g.fillRect(mapX * tileSize, mapY * tileSize, tileSize, tileSize);
	}
	
/*
	// Example main method. 
	public static void main(String[] args) {
		Random randGen = new Random(System.currentTimeMillis());
		
		// set up a World to use, and add some obstructions to it
		int size = 60;
		World testMap = new SimpleWorld(size);
		for(int i = 0; i < 100; i++) {
			testMap.addObstruction(randGen.nextInt(size), randGen.nextInt(size));
		}
		
		// determine the point from which to cast rays
		int originX = randGen.nextInt(size);
		int originY = randGen.nextInt(size);
		
		// set up the ray casting object and perform the search
		MultiRaysCaster caster=null;
//		=new MultiRaysCaster(testMap, originX, originY);
		caster.castRays();
		
		// create a ResultsDisplayer to show the results
		ResultsDisplayer displayer = new ResultsDisplayer();
		displayer.assignData(testMap, caster.getResults(), caster.getOrigin());
		
		// uncomment the line below to print results data to system out
//		displayer.displayText(); 

		// create a container to hold the ResultsDisplayer
		javax.swing.JFrame container = new javax.swing.JFrame("Rays");
		WindowAdapter closeAdapter = new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent event) {
				System.exit(1);
			}
		};		
		container.addWindowListener(closeAdapter);
		container.getContentPane().add(displayer);
	    container.pack();
		container.setVisible(true);
		container.repaint();
	}
*/	
}

