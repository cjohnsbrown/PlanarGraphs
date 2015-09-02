import java.util.ArrayList;
import java.util.Collections;


public class Vertex {

	/**
	 * x coordinate
	 */
	public double x = Double.NaN;
	/**
	 * y coordinate
	 */
	public double y = Double.NaN;
	
	/**
	 * A vertex's adjacency list
	 */
	public ArrayList<Edge> neighbors = new ArrayList<Edge>();
	
	/**
	 * Each label is unique to each vertex
	 */
	public final int label; 

	/**
	 * Constructor used if the edges are already sorted
	 */
	public Vertex(int label) {
		this.label = label;
	}
	
	/**
	 * Constructor used if graph is embedded 
	 */
	public Vertex(int label, double x, double y) {
		this.label = label;
		this.y = y;
		this.x = x;
	}
	
	
	public void addNeighbor(Edge e) {
		if (this.equals(e.start)) {
			neighbors.add(e);
		}
		else
			neighbors.add(e.getReverse());
	}
	
	/**
	 * If the graph is embedded, sort the list of edges in counter-clockwise
	 * order
	 */
	public void ccwSort() {
		if (!Double.isNaN(x)) {
			Collections.sort(neighbors);	
			for(int i=0; i < neighbors.size(); i++) {
				neighbors.get(i).index = i;
			}

		}	
	}

	/**
	 * @return true if labels are the same, false otherwise	
	 */
	public boolean equals(Vertex u) {
		return label == u.label;
	}

	public int hashCode() {
		return label;
	}

	public String toString() {
		return Integer.toString(label);
	}

}





