/*
NAME: leejasp1
LANG: JAVA
PROG: snakes
*/

package USOpen2019;

import java.io.*;
import java.util.*;

public class snakes {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("snakes.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("snakes.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numGroups = Integer.parseInt(st.nextToken());
		int numChanges = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		int[] groups = new int[numGroups];
		for (int i = 0; i < numGroups; i++) {
			groups[i] = Integer.parseInt(st.nextToken());
		}
		reader.close();
		
		int[][] netSum = new int[numGroups+1][numChanges+1];
		int maxGroup = 0;
		for (int i = 1; i <= numGroups; i++) {
			maxGroup = Math.max(maxGroup, groups[i-1]);
			netSum[i][0] = i * maxGroup;
		}
		for (int i = 1; i <= numGroups; i++) {
			for (int j = 1; j <= numChanges; j++) {
				netSum[i][j] = netSum[i][j - 1];
				maxGroup = groups[i-1];
				for (int k = i - 1; k > 0; k--) {
					int currSum = netSum[k][j - 1] + (i - k) * maxGroup;
					netSum[i][j] = Math.min(netSum[i][j], currSum);
					maxGroup = Math.max(maxGroup, groups[k-1]);
				}
			}
		}
		
		int waste = netSum[numGroups][numChanges];
		for (int i = 0; i < numGroups; i++) {
			waste -= groups[i];
		}
		writer.println(waste);
		writer.close();
	}
}