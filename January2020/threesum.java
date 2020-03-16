/*
NAME: leejasp1
LANG: JAVA
PROG: threesum
*/

package January2020;

import java.io.*;
import java.util.*;

public class threesum {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("threesum.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("threesum.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int len = Integer.parseInt(st.nextToken());
		int numQuers = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		long[][] bound = new long[len][len];
		int[] freq = new int[2000001];
		for (int i = 0; i < len - 2; i++) {
			for (int j = i + 1; j < len; j++) {
				int ind = 1000000 - arr[i] - arr[j];
				if (ind >= 0 && ind < 2000001) {
					bound[i][j] = freq[ind];
				}
				freq[1000000 + arr[j]]++;
			}
			for (int j = i + 1; j < len; j++) {
				freq[1000000 + arr[j]]--;
			}
		}
		
		for (int i = 2; i <= len - 1; i++) {
			for (int j = 0; j < len - i; j++) {
				bound[j][j + i] += bound[j][j + i - 1] + bound[j + 1][j + i] - bound[j + 1][j + i - 1];
			}
		}
		
		for (int i = 0; i < numQuers; i++) {
			st = new StringTokenizer(reader.readLine());
			int left = Integer.parseInt(st.nextToken());
			int right = Integer.parseInt(st.nextToken());
			writer.println(bound[left-1][right-1]);
		}
		reader.close();
		writer.close();
	}
}