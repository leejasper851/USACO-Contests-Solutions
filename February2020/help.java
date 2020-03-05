/*
NAME: leejasp1
LANG: JAVA
PROG: help
*/

package February2020;

import java.io.*;
import java.util.*;

public class help {
	private static final int MOD_NUM = 1000000007;
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("help.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("help.out")));
		
		int numSegs = Integer.parseInt(reader.readLine());
		int[] layChg = new int[2 * numSegs + 1];
		int[] segLs = new int[numSegs];
		for (int i = 0; i < numSegs; i++) {
			StringTokenizer st = new StringTokenizer(reader.readLine());
			int segL = Integer.parseInt(st.nextToken());
			int segR = Integer.parseInt(st.nextToken());
			layChg[segL]++;
			layChg[segR]--;
			segLs[i] = segL;
		}
		reader.close();
		
		int[] pow2 = new int[numSegs];
		pow2[0] = 1;
		for (int i = 1; i < numSegs; i++) {
			pow2[i] = (pow2[i - 1] * 2) % MOD_NUM;
		}
		
		int layCount = 0;
		int[] lays = new int[numSegs * 2 + 1];
		for (int i = 0; i < lays.length; i++) {
			lays[i] = layCount;
			layCount += layChg[i];
		}
		
		int compCount = 0;
		for (int segL : segLs) {
			compCount += pow2[numSegs - 1 - lays[segL]];
			compCount %= MOD_NUM;
		}
		writer.println(compCount);
		writer.close();
	}
}