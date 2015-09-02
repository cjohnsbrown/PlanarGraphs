import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;


public class PlanarGraph {

	public ArrayList<Vertex> V;
	public ArrayList<Edge> E;

	public ArrayList<Face> faces = new ArrayList<Face>();
	private int[] seen;

	public PlanarGraph(ArrayList<Vertex> V, ArrayList<Edge> E) {
		this.V = V;
		this.E = E;
		seen = new int[E.size()*2];
	} 

	/**
	 * Recursively constructs a single Face
	 *
	 * @param start - the starting vertex of the face
	 * @param v - a neighbor of a previous vertex
	 * @param apartOfFace - list of edges that are apart of the face
	 * @param curr - current index of the next neighbor in v's adj list
	 *
	 * @return a new Face or null if an edge has been seen more than twice
	 */
	public Face mkFace(Vertex start, Vertex v, ArrayList<Edge> apartOfFace, int curr, int[] seen, 
			int faceLabel, Face[][] sharedEdge) {
		if (curr == v.neighbors.size()) 
			curr = 0;
		Edge nextEdge = v.neighbors.get(curr);
		if (seen[nextEdge.label]  >= 2) 
			return null;
		Vertex next = nextEdge.end;
		int nextIndex = nextEdge.getReverseEdgeIndex() +1; 
		apartOfFace.add(nextEdge);
		seen[nextEdge.label]++;
		if (next.equals(start)) 
			return new Face(apartOfFace, faceLabel, sharedEdge);
		Face f = mkFace(start, next, apartOfFace, nextIndex, seen, faceLabel, sharedEdge);
		if (f == null) 
			seen[nextEdge.label]--;
		return f;

	}

	/**
	 * Iterates through each vertex's adj list and constructs all the
	 * faces in the graph
	 */
	public Face[][] getFaces() {
		Face face;
		ArrayList<Edge> apartOfFace;
		int faceLabel = 0;
		Face[][] sharedEdge = new Face[E.size()][2];
		for (Vertex v: V) {
			for (Edge e: v.neighbors) {
			apartOfFace = new ArrayList<Edge>();
			seen[e.label]++;
			apartOfFace.add(e);
			face = mkFace(e.start, e.end, apartOfFace, e.getReverseEdgeIndex()+1, seen, faceLabel,
					sharedEdge);
			if (face != null) {
				faces.add(face);
				faceLabel++;
				
			} else {
				seen[e.label]--;
			}
		}
		}
		return sharedEdge;
	}	

	/**
	 * Once all faces have been found, the dual graph is created
	 *
	 * @param path - set of edges whose dual should not be created
	 */
	public void constructDual(HashSet<Integer> path) {
		Edge[] dual = new Edge[E.size()];
		for (Face f: faces) {
			f.addNeigbors( dual, path);	
		}
	}

}	

