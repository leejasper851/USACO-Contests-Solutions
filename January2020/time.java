/*
NAME: leejasp1
LANG: JAVA
PROG: time
*/

package January2020;

import java.io.*;
import java.util.*;

public class time {
	private static final int INF = 10000000;
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("time.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("time.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numCities = Integer.parseInt(st.nextToken());
		int numRoads = Integer.parseInt(st.nextToken());
		int costCoeff = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		int[] cityMoons = new int[numCities];
		for (int i = 0; i < numCities; i++) {
			cityMoons[i] = Integer.parseInt(st.nextToken());
		}
		ArrayList<Integer>[] roads = new ArrayList[numCities];
		for (int i = 0; i < numCities; i++) {
			roads[i] = new ArrayList<>();
		}
		for (int i = 0; i < numRoads; i++) {
			st = new StringTokenizer(reader.readLine());
			int fromRoad = Integer.parseInt(st.nextToken())-1;
			int toRoad = Integer.parseInt(st.nextToken())-1;
			roads[toRoad].add(fromRoad);
		}
		reader.close();
		
		int[][] maxMoons = new int[1001][numCities];
		Arrays.fill(maxMoons[0], -INF);
		maxMoons[0][0] = 0;
		int maxMoons0 = 0;
		for (int i = 1; i <= 1000; i++) {
			for (int j = 0; j < numCities; j++) {
				maxMoons[i][j] = -INF;
				boolean isPrev = false;
				for (int adjCity : roads[j]) {
					if (maxMoons[i - 1][adjCity] > maxMoons[i][j]) {
						maxMoons[i][j] = maxMoons[i - 1][adjCity];
						isPrev = true;
					}
				}
				if (isPrev) {
					maxMoons[i][j] += cityMoons[j];
				}
			}
			maxMoons0 = Math.max(maxMoons0, maxMoons[i][0] - costCoeff * i * i);
		}
		writer.println(maxMoons0);
		writer.close();
	}
}