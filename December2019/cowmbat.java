/*
NAME: leejasp1
LANG: JAVA
PROG: cowmbat
*/

package December2019;

import java.io.*;
import java.util.*;

public class cowmbat {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("cowmbat.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("cowmbat.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int comboLen = Integer.parseInt(st.nextToken());
		int numLets = Integer.parseInt(st.nextToken());
		int streakLen = Integer.parseInt(st.nextToken());
		int[] origCombo = new int[comboLen];
		String origStr = reader.readLine();
		for (int i = 0; i < comboLen; i++) {
			origCombo[i] = origStr.charAt(i) - 97;
		}
		int[][] adjMat = new int[numLets][numLets];
		for (int i = 0; i < numLets; i++) {
			st = new StringTokenizer(reader.readLine());
			for (int j = 0; j < numLets; j++) {
				adjMat[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		reader.close();
		
		for (int k = 0; k < numLets; k++) {
			for (int i = 0; i < numLets; i++) {
				for (int j = 0; j < numLets; j++) {
					if (adjMat[i][k] + adjMat[k][j] < adjMat[i][j]) {
						adjMat[i][j] = adjMat[i][k] + adjMat[k][j];
					}
				}
			}
		}
		
		int[][] streakCost = new int[comboLen][numLets];
		for (int i = 0; i < numLets; i++) {
			for (int j = 0; j < streakLen; j++) {
				streakCost[streakLen-1][i] += adjMat[origCombo[j]][i];
			}
		}
		for (int i = streakLen; i < comboLen; i++) {
			for (int j = 0; j < numLets; j++) {
				streakCost[i][j] = streakCost[i - 1][j] - adjMat[origCombo[i - streakLen]][j] + adjMat[origCombo[i]][j];
			}
		}
		
		int[][] minLet = new int[comboLen][numLets];
		for (int i = 0; i < comboLen; i++) {
			Arrays.fill(minLet[i], Integer.MAX_VALUE);
		}
		int[] minLen = new int[comboLen];
		Arrays.fill(minLen, Integer.MAX_VALUE);
		
		for (int i = 0; i < numLets; i++) {
			minLet[streakLen-1][i] = streakCost[streakLen-1][i];
			minLen[streakLen-1] = Math.min(minLen[streakLen-1], minLet[streakLen-1][i]);
		}
		
		for (int i = streakLen; i < comboLen; i++) {
			for (int j = 0; j < numLets; j++) {
				int sameCost = minLet[i - 1][j] + adjMat[origCombo[i]][j];
				int diffCost = Integer.MAX_VALUE;
				if (minLen[i - streakLen] != Integer.MAX_VALUE) {
					diffCost = minLen[i - streakLen] + streakCost[i][j];
				}
				minLet[i][j] = Math.min(sameCost, diffCost);
				minLen[i] = Math.min(minLen[i], minLet[i][j]);
			}
		}
		writer.println(minLen[comboLen-1]);
		writer.close();
	}
}