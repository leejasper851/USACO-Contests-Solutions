/*
NAME: leejasp1
LANG: JAVA
PROG: timeline
*/

package February2020;

import java.io.*;
import java.util.*;

public class timeline {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("timeline.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("timeline.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numMilks = Integer.parseInt(st.nextToken());
		int numDays = Integer.parseInt(st.nextToken());
		int numMems = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		int[] days = new int[numMilks];
		for (int i = 0; i < numMilks; i++) {
			days[i] = Integer.parseInt(st.nextToken());
		}
		ArrayList<Integer>[] deps = new ArrayList[numMilks];
		ArrayList<Integer>[] gaps = new ArrayList[numMilks];
		HashSet<Integer>[] depSet = new HashSet[numMilks];
		for (int i = 0; i < numMilks; i++) {
			deps[i] = new ArrayList<>();
			gaps[i] = new ArrayList<>();
			depSet[i] = new HashSet<>();
		}
		for (int i = 0; i < numMems; i++) {
			st = new StringTokenizer(reader.readLine());
			int milk1 = Integer.parseInt(st.nextToken())-1;
			int milk2 = Integer.parseInt(st.nextToken())-1;
			int gap = Integer.parseInt(st.nextToken());
			deps[milk2].add(milk1);
			gaps[milk2].add(gap);
			depSet[milk2].add(milk1);
		}
		reader.close();
		
		Integer[] milkSort = new Integer[numMilks];
		for (int i = 0; i < numMilks; i++) {
			milkSort[i] = i;
		}
		Arrays.sort(milkSort, new DepComp(depSet, days));
		
		boolean[] visited = new boolean[numMilks];
		for (int i = 0; i < numMilks; i++) {
			solve(i, days, deps, gaps, visited);
		}
		
		for (int i = 0; i < numMilks; i++) {
			writer.println(days[i]);
		}
		writer.close();
	}
	
	private static void solve(int milk, int[] days, ArrayList<Integer>[] deps, ArrayList<Integer>[] gaps, boolean[] visited) {
		if (visited[milk]) {
			return;
		}
		visited[milk] = true;
		for (int j = 0; j < deps[milk].size(); j++) {
			if (!visited[deps[milk].get(j)]) {
				solve(deps[milk].get(j), days, deps, gaps, visited);
			}
			if (days[milk] < days[deps[milk].get(j)] + gaps[milk].get(j)) {
				days[milk] = days[deps[milk].get(j)] + gaps[milk].get(j);
			}
		}
	}
	
	private static class DepComp implements Comparator<Integer> {
		public HashSet<Integer>[] depSet;
		public int[] origDays;
		
		public DepComp(HashSet<Integer>[] ds, int[] od) {
			depSet = ds;
			origDays = od;
		}
		
		public int compare(Integer a, Integer b) {
			if (depSet[a].contains(b)) {
				return 1;
			}
			if (depSet[b].contains(a)) {
				return -1;
			}
			return origDays[a] - origDays[b];
		}
	}
}