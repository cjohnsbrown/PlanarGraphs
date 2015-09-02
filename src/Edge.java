public class Edge implements Comparable<Edge> {

	public Vertex start, end;
	public int capacity;

	public final int label;
	public Edge reverseEdge = null;

	public int index = 0;

	private double angle;

	/**
	 * Edge Constructor
	 */
	public Edge(Vertex u, Vertex v, int capacity, int label) {
		start = u;
		end = v;
		this.capacity = capacity;
		this.label = label;
		findAngle();	
	}
	
	
	// Used to sort edges cyclically
	public void findAngle() {
		if (!Double.isNaN(start.x)) {
			double distX = start.x - end.x;
			double distY = start.y - end.y;
			angle = Math.atan2(distY, distX);
		}
	}
	/**
	 * Creates a new edge that is this edge's reverse and sets a referrence to
	 * it
	 *
	 * @return reverse of th edge
	 */
	public Edge getReverse() {
		if (reverseEdge == null) {
			reverseEdge = new Edge(end, start, capacity * -1, label);
			reverseEdge.reverseEdge = this;
		}
		return reverseEdge;
	}


	public String toString() {
		if (capacity > 0)
			return start + "->" + end;
		return start + "<-" + end;

	}

	/**
	 * @return the index of the reverse edge in the adjacency list
	 */
	public int getReverseEdgeIndex() {
		return reverseEdge.index;
	}

	
	/**
	 * Used for sorting edges based on the angle
	 */
	public int compareTo(Edge e) {
		if (this.angle > e.angle)
			return 1;
		else if (this.angle < e.angle) 
			return -1;
		else
			return 0;
	}

	public int hashCode() {
		return label;
	}
		

}
