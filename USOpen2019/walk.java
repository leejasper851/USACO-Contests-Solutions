/*
NAME: leejasp1
LANG: JAVA
PROG: walk
*/

package USOpen2019;

import java.io.*;
import java.util.*;

public class walk {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("walk.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("walk.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numCows = Integer.parseInt(st.nextToken());
		int numGroups = Integer.parseInt(st.nextToken());
		reader.close();
		
		ArrayList<Integer> edges = new ArrayList<>();
		boolean[] visited = new boolean[numCows];
		int[] dist = new int[numCows];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[0] = 0;
		int curr = 0;
		while (true) {
			visited[curr] = true;
			if (curr != 0) {
				edges.add(dist[curr]);
			}
			
			int minDist = Integer.MAX_VALUE;
			int next = -1;
			for (int i = 0; i < numCows; i++) {
				if (visited[i]) {
					continue;
				}
				int newDist = distBtwn(curr, i);
				if (newDist < dist[i]) {
					dist[i] = newDist;
				}
				if (dist[i] < minDist) {
					minDist = dist[i];
					next = i;
				}
			}
			if (next == -1) {
				break;
			}
			curr = next;
		}
		
		Collections.sort(edges, new RevComp());
		writer.println(edges.get(numGroups - 2));
		writer.close();
	}
	
	private static int distBtwn(long cowA, long cowB) {
		cowA++;
		cowB++;
		if (cowB < cowA) {
			long temp = cowA;
			cowA = cowB;
			cowB = temp;
		}
		return (int) ((2019201913 * cowA + 2019201949 * cowB) % 2019201997);
	}
	
	private static class RevComp implements Comparator<Integer> {
		public int compare(Integer a, Integer b) {
			if (b > a) {
				return 1;
			}
			if (a > b) {
				return -1;
			}
			return 0;
		}
	}
}