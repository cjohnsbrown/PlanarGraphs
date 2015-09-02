# PlanarGraphs

This software implemnets the counting minimum s-t cuts algorithm described in 

 "Counting andsampling minimum (s; t)-cuts in weighted planar graphs in polynomial time."
  Ivona Bezakova and Adam Friedlander. 
  Theoret. Comput. Sci. 417, 2{11 (2012)

If you use this software for research purposes, you should cite
the aforementioned paper in any resulting publication.
==============================================================================
 ##Usage

To run the program use GraphTest which reads in a graph from stdin. If giving the original 
graph, the first line indicates the number of vertices, n. The vertices are labelled with numbert 0,1,...n-1. 
The i-th line list    , the x and y values are read in.
After each vertex is read the next number is the number of edges. Each edge is represented as 
	<start> <end> <capacity> 
where start and end are the indexes of the vertices from the order in which they are read in.
See input-1 for an example. The program can also be used from CountingCuts.java where the input
graph is a residual graph. The input is similar to the previous execpt edges do not need to be 
enterted. See input-2 for an example. 
