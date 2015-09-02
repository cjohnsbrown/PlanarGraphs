
import java.util.*;

public class GraphTest {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		//Construct Graph
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
			G[i][j] = -1;
			//G[i][n] = s.nextInt();
			//G[i][n+1] = s.nextInt();
		}
		int[][] C = new int[n][n];
		int m = s.nextInt();
		for (int j=0; j < m; j++) {
			C[s.nextInt()][s.nextInt()] = s.nextInt();
		}
		int[][] F = new int[n][n];
		CountingCuts counter = new CountingCuts();
		System.out.println(counter.edmondsKarp(G, C, 2, 6, F));
		int[][] H = counter.matrixToList(F, C);	
		for (int[] k: C)
			System.out.println(Arrays.toString(k));
		
		//for (int k=0; k< n; k++) {
			//H[k][n] = G[k][n];
			//H[k][n+1] = G[k][n+1];
		//}
		//System.out.println(counter.count(H, 0, 3, false));
		
		



	}


}
			
		


