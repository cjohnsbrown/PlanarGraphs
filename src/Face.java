
import java.util.ArrayList;
import java.util.HashSet;

public class Face {

	public ArrayList<Edge> edges;
	public Vertex dualVertex;
	private Face[][] sharedEdge;
	
	/**
	 * Face Constructor
	 *
	 * @param edges list of edges that are apart of the face
	 * @param label - label used when creating the vertex that corresponds to
	 * the face
	 * @param sharedEdge - array used to find which face an edge is apart of in
	 * constant time
	 */
	public Face(ArrayList<Edge> edges, int label, Face[][] sharedEdge) {
		this.edges = edges;
		dualVertex = new Vertex(label);
		this.sharedEdge = sharedEdge;
		for (Edge e: edges) {
			if (sharedEdge[e.label][0] == null)
				sharedEdge[e.label][0] = this;
			else
				sharedEdge[e.label][1] = this;
		}


	}
	/**
	 * @return true if labels are equal, false otherwise
	 */
	public boolean equals(Face f) {
		return dualVertex.equals(f.dualVertex);	
	}
	
	/**
	 * @return a string reprentation of the list of edges
	 */
	public String toString() {
		return edges.toString();
	}

	/**
	 * Dynamically creates edges between adjacent faces
	 *
	 * @param dual - array used to store dual edges
	 * @param path - set of edges on t-s path. The dual of these edges should
	 * 				 not be apart of the dual graph
	 */
	public void addNeigbors(Edge[] dual, HashSet<Integer> path) {
		Face[] faces;
		int edgeId;
		Face f;
		Edge d = null;
		int index = 0;
		for (Edge e: edges) {
			edgeId = e.label;
			if (!path.contains(edgeId)) {
				faces = sharedEdge[edgeId];
				if (faces[0].equals(this))
					f = faces[1];
				else
					f = faces[0];
				if (f.dualVertex.label < dualVertex.label) {   //The edge has already been created 
					d = dual[edgeId];
					dualVertex.neighbors.add(d.getReverse());  //So get the reverse
				} else {                                       //Create a new dual edge and 
					d = new Edge(dualVertex, f.dualVertex, e.capacity, edgeId);
					dualVertex.neighbors.add(d);
					dual[edgeId] = d;
				}
			}
			
		}
	}


}
