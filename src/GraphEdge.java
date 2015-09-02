public abstract class GraphEdge {

	public Vertex start, end;
	public int capacity;
	
	/**
	 * used so that edges and their reverse are seen as equal
	 */
	public final int label;   
	public Edge reverseEdge = null;
	/**
	 * Corresponds to the index of the edge once the list is sorted
	 */
	public int index = 0; 
	/**
	 * Angle between the vertices connected by the edge (if the graph is
	 * embedded)
	 */
	public double angle;


	/**
	 * Edge Constructor
	 */
	public GraphEdge (Vertex v, Vertex u, int capacity, int label) {
		start = v;
		end = u;
		this.capacity = capacity;
		this.label = label;
		findAngle();
	}

	
	
	// Used to sort edges cyclically
	public  abstract void findAngle();
		
	/**
	 * Creates a new edge that is this edge's reverse and sets a referrence to
	 * it
	 *
	 * @return reverse of th edge
	 */
	public abstract Edge getReverse();
	

	public abstract String toString();
	


	/**
	 * @return the index of the reverse edge in the adjacency list
	 */
	public abstract int getReverseEdgeIndex();

	
	/**
	 * Used for sorting edges based on the angle
	 */
	public abstract int compareTo(Edge e);

	public abstract int hashCode();		

}
