package dk.itu.e2013.sad2.exam;

import java.util.*;

//Maximum matching for bipartite graph. Kuhn's algorithm in O(E*V)

public class MaxMatching2 {

  static boolean findPath(List<Integer>[] g, int u1, int[] matching, boolean[] vis) {
    vis[u1] = true;
    for (int v : g[u1]) {
      int u2 = matching[v];
      if (u2 == -1 || !vis[u2] && findPath(g, u2, matching, vis)) {
        matching[v] = u1;
        return true;
      }
    }
    return false;
  }

  public static int maxMatching(List<Integer>[] g, int n2) {
    int n1 = g.length;
    int[] matching = new int[n2];
    Arrays.fill(matching, -1);
    int matches = 0;
    for (int u = 0; u < n1; u++) {
      if (findPath(g, u, matching, new boolean[n1]))
        ++matches;
    }
    return matches;
  }

  // Usage example
 // public static void main(String[] args) {
 //   int n1 = 2;
 //   int n2 = 3;
 //   List<Integer>[] g = new List[n1];
 //   for (int i = 0; i < n1; i++) {
 //     g[i] = new ArrayList<Integer>();
 //   }
 //   g[0].add(2);
 //   g[0].add(0);
 //   g[1].add(2);
 //   System.out.println(2 == maxMatching(g, n2));
 // }
}
