import java.util.*;

public class CountingCuts {


	public CountingCuts() {}

	public int edmondsKarp(int[][] G, int[][] C, int s, int t, int[][] F) {
		int n = C.length;
		while (true) {
			int[] parents = new int[n]; 
			Arrays.fill(parents, -1);
			parents[s] = s;
			int[] M = new int[n]; //capactiy of path to vertex 
			M[s] = Integer.MAX_VALUE;
			Queue<Integer> Q = new LinkedList<Integer>();
			int v;
			Q.offer(s);
			while (!Q.isEmpty()) {
				int u = Q.poll();
				int j = 0;
				while(G[u][j] != -1) {
					v = G[u][j];
					if (C[u][v] - F[u][v] > 0 && parents[v] == -1) {
						parents[v] = u;
						M[v] = Math.min(M[u], C[u][v] - F[u][v]);
						if (v != t)
							Q.offer(v);
						else {
							while (parents[v] != v) {
								u = parents[v];
								F[u][v] += M[t];
								F[v][u] -= M[t];
								v = u;
							}
							Q.clear();
						}
					}
					j++;
				}
			}
			if (parents[t] == -1) { 
				int sum = 0;
				for (int x : F[s])
					sum += x;
				return sum;
			}
		}
	}

	/**
	 * Find a single SCC using Tarjan's algorithm
	 * @param v - a vertex in the graph
	 * @param visited - array used to know which vertices have been visited
	 * once or not
	 * @param stack - stack used to topologically order vertices
	 * @param inStack - array to keep track of if a vertex has been removed
	 * from the stack
	 * @param F - residual graph
	 * @param time -
	 * @param comps - List that each SCC found gets added to
	 * @param low - the smallest index of any vertex known to be reachable from v, including v itself
	 */
	public static void mkSCC(int v, int[] visited, int[] low, LinkedList<Integer> stack,
			boolean[] inStack, int[][] F, int time, ArrayList<ArrayList<Integer>> comps) {
		visited[v] = low[v] = ++time;
		stack.addFirst(v);
		inStack[v] = true;
		int j=0;
		int u;
		while (F[v][j] != -1) {
			u = F[v][j];
			if (visited[u] == -1) {
				mkSCC(u, visited, low, stack, inStack, F, time, comps);
				low[v] = Math.min(low[v], low[u]);
			} else if (inStack[u] == true)
				low[v] = Math.min(low[v], visited[u]);
			j++;
		}
		int w;
		ArrayList<Integer> scc = new ArrayList<Integer>();
		if (low[v] == visited[v]) {
			while (stack.peek() != v) {
				w = stack.pop();
				inStack[w] = false;
				scc.add(w);
			} 
			w = stack.pop();
			inStack[w] = false;
			scc.add(w);
		}
		if (scc.size() > 0)
			comps.add(scc);

	}
	/**
	 * Constructs a list of all the SCCs in the graph
	 *
	 * @param F - the residual graph
	 * @return comps - list of SCCs
	 */
	public static ArrayList<ArrayList<Integer>> stronglyConnected(int[][] F) {
		int[] visited = new int[F.length];
		int[] low = new int[F.length];
		boolean[] inStack = new boolean[F.length];
		LinkedList<Integer> stack = new LinkedList<Integer>();
		ArrayList<ArrayList<Integer>> comps = new ArrayList<ArrayList<Integer>>();
		for (int i=0; i < F.length; i++) {
			visited[i] = -1;
			low[i] = -1;
			inStack[i] = false;
		}
		int time = 0;
		for (int j=0; j < F.length; j++) {
			if (visited[j] == -1) {
				mkSCC(j, visited, low, stack, inStack, F, time, comps);
			}
		}
		return comps;
	}

	/**
	 * Converts adjacency matrix from edmondsKarp to adjacency lists.
	 *
	 * @param F - residual graph in form of adjacency matrix
	 * @param C - matrix of original capcities
	 *
	 * @return H - residual graph in the form of adjacency lists
	 */
	public int[][] matrixToList(int[][] F, int[][] C) {
		int[][] H = new int[F.length][F.length+2];
		int size;
		for (int i=0; i < F.length; i++) {
			size = 0;
			for (int j=0; j < F.length; j++) {
				if (F[i][j] > 0 && F[i][j] - C[i][j] !=0) {
					H[i][size] = j;
					size++;
				}
				else if ( F[i][j] < 0) {
					H[i][size] = j;
					size++;
				}
			}
			H[i][size] = -1;
		}
		return H;
	}

	/**
	 * Contracts each SCC into one vertex
	 *
	 * @param comps - list of SCCs
	 * @param stLocations - pointer to keep track of which new vertices s and
	 * t are aprart of
	 * @param F - the residual graph
	 *
	 * @return the contracted graph represented as a PlanarGraph
	 */
	public static PlanarGraph contract(ArrayList<ArrayList<Integer>> comps, int[][] F, 
			int[] stLocations, int s, int t, boolean sorted) {
		ArrayList<Vertex> V = new ArrayList<Vertex>(comps.size());
		ArrayList<Edge> E = new ArrayList<Edge>();
		ArrayList<Integer> curr;
		HashSet<Integer> neighbors;
		Edge e, r;
		int edegCount = 0;
		int x, y;
		int n = F.length;
		for (int i = 0; i < comps.size(); i++) {
			if (!sorted) {
				x = F[comps.get(i).get(0)][n];
				y = F[comps.get(i).get(0)][n+1];
				V.add( new Vertex(i, x, y));
			}
			else 
				V.add( new Vertex(i));
		}	
		// Add new edges 
		for (int i=0; i < comps.size(); i++) {
			curr = comps.get(i);
			neighbors = neighborSet(curr, F, i, stLocations, s, t);
			for (int j=0; j < comps.size(); j++) {
				if (i != j) {
					for (int u: comps.get(j)) {
						if (neighbors.contains(u)) {
							e = new Edge(V.get(i), V.get(j), 1, edegCount);
							r = e.getReverse();
							E.add(e);
							V.get(i).addNeighbor(e);
							V.get(j).addNeighbor(r);
							edegCount++;
							break;
						}
					}
				}
			}
		}	
		return new PlanarGraph(V, E);
	}

	/**
	 * Creates a set of vertices that are reachable from the given SCC and set
	 * s and t locations in the contracted graph
	 *
	 * @param SCC - the strongly connected component
	 * @param F - the residual graph
	 * @param index - index of the SCC in the list of all components
	 * @param stLocations - array to store s and t locations
	 *
	 * @return a set of vertices outside of the SCC
	 */
	public static HashSet<Integer> neighborSet(ArrayList<Integer> SCC, int[][] F, int index,
			int[] stLocations, int s, int t) {
		HashSet<Integer> neighbors = new HashSet<Integer>();
		for (int v: SCC) {
			if (v == s)
				stLocations[0] = index;
			if (v == t) 
				stLocations[1] = index;
			int j =0;
			while (F[v][j] != -1) {
				neighbors.add(F[v][j]);
				j++;
			}
		}
		return neighbors;
	}

	/**
	 * BFS to find the shoretest path between t and s in the contracted graph
	 *
	 * @param D - contracted planar graph
	 * @param t - vertex that corresponds to t's location in the contracted
	 * graph
	 * @param s - vertex that corresponds to s's location in the contracted
	 * graph 
	 *
	 * @return an array of parent pointers used to construct the path
	 */
	public Edge[] BFS(PlanarGraph D, Vertex t, Vertex s) {
		boolean[] seen = new boolean[D.V.size()];
		Edge[] parents = new Edge[D.V.size()];
		LinkedList<Vertex> que = new LinkedList<Vertex>();
		Vertex v,u;
		seen[t.label] = true;
		que.add(t);
		Edge e, d, r;
		while (que.size() != 0) {
			v = que.poll();
			if (v.equals(s))
				break;
			for (int i=0; i < v.neighbors.size(); i++) {
				e = v.neighbors.get(i);
				if (e.capacity > 0) {
					u = e.end;
					if (!seen[u.label]) {
						parents[u.label] = e;
						seen[u.label] = true;
						que.add(u);
					}
				}
			}
		}
		return parents;
	}

	/**
	 * Constructs t-s path 
	 *
	 * @param parents - parent pointer array created by the BFS
	 * @param t - t's vertex in the contracted graph
	 * @param s - s's vertex in the contracted graph
	 * @param D contracted planar graph
	 *
	 * @return a set containg the edges on the path
	 */
	public HashSet<Integer> shortestPath(Edge[] parents, Vertex t, Vertex s) {
		HashSet<Integer> path = new HashSet<Integer>();
		Edge e= parents[s.label];
		path.add(e.label);
		while (!e.start.equals(t)) {
			e = parents[e.start.label];
			path.add(e.label);
		}
		return path;
	}


	/**
	 * Dynamically finds all a-b paths in a DAG
	 * a and b are vertices in the dual graph separated by an edge on the t-s
	 * path
	 *
	 * @return the number of a-b paths
	 */
	public int countCycles(Vertex a, Vertex b, int[] cycleCounts) {
		if (a.equals(b))
			return 1;
		if (cycleCounts[a.label] != 0)
			return cycleCounts[a.label];

		int count = 0;
		Vertex c;
		for (Edge e: a.neighbors) {
			if (e.capacity > 0) {
					c = e.end;
					count += countCycles(c, b, cycleCounts);
					cycleCounts[a.label] = count;
			}
		}
		return count;
	}

	/**
	 * Computes the number of minimum s-t cuts given the resdidual of a graph
	 * 
	 * @param F - resdidual graph
	 * @param s - source
	 * @param t - sink
	 * @param sorted - boolean to decied if the graph is embedded and the edges
	 * are unsorted
	 *
	 * @return number of minimum s-t cuts
	 */
	public int count(int[][] F, int s, int t, boolean sorted) {
		int[] stLocations = new int[2];
		ArrayList<ArrayList<Integer>> comps = stronglyConnected(F);
		PlanarGraph D = contract(comps, F, stLocations, s, t, sorted);
		Vertex sComp = D.V.get(stLocations[0]);
		Vertex tComp = D.V.get(stLocations[1]);
		if (!sorted)
			for (Vertex v: D.V) 
				v.ccwSort();
		Face[][] sharedEdge = D.getFaces(); 
		Edge[] parents = BFS(D, tComp, sComp);
		HashSet<Integer> path = shortestPath(parents, tComp, sComp);
		D.constructDual(path);
		int count = 0;
		for (int i: path) {
			int[] cycleCounts = new int[D.V.size()];
			Face f1 = sharedEdge[i][0];
			Face f2 = sharedEdge[i][1];
			int tmp = countCycles(f1.dualVertex, f2.dualVertex, cycleCounts);
			if (tmp == 0) 
				tmp = countCycles(f2.dualVertex, f1.dualVertex,cycleCounts);
			count += tmp;
		}

		return count;	
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		int u,v,c;
		int[][] G = new int[n][n+2];
		for (int i=0; i < n; i++) {
			u = s.nextInt();
			int j=0;
			while(u != -1) {
				G[i][j] = u;
				j++;
				u = s.nextInt();
			}
			G[i][j] = u;
			G[i][n] = s.nextInt();
			G[i][n+1] = s.nextInt();
		}
		CountingCuts counter = new CountingCuts();
		System.out.println(counter.count(G, 0, 1, false)); 
	}
		

}
