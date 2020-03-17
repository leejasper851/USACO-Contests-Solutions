/*
NAME: leejasp1
LANG: JAVA
PROG: milkvisits
*/

package December2019;

import java.io.*;
import java.util.*;

public class milkvisits {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("milkvisits.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("milkvisits.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numFarms = Integer.parseInt(st.nextToken());
		int numFriends = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		int[] types = new int[numFarms];
		for (int i = 0; i < numFarms; i++) {
			types[i] = Integer.parseInt(st.nextToken())-1;
		}
		HashSet<Integer>[] roads = new HashSet[numFarms];
		for (int i = 0; i < numFarms; i++) {
			roads[i] = new HashSet<>();
		}
		for (int i = 0; i < numFarms - 1; i++) {
			st = new StringTokenizer(reader.readLine());
			int farmA = Integer.parseInt(st.nextToken())-1;
			int farmB = Integer.parseInt(st.nextToken())-1;
			roads[farmA].add(farmB);
			roads[farmB].add(farmA);
		}
		ArrayList<ArrayList<Integer>>[] querInd = new ArrayList[numFarms];
		for (int i = 0; i < numFarms; i++) {
			querInd[i] = new ArrayList<>();
		}
		for (int i = 0; i < numFriends; i++) {
			st = new StringTokenizer(reader.readLine());
			int farmA = Integer.parseInt(st.nextToken())-1;
			int farmB = Integer.parseInt(st.nextToken())-1;
			int type = Integer.parseInt(st.nextToken())-1;
			ArrayList<Integer> quer = new ArrayList<>();
			quer.add(farmA);
			quer.add(farmB);
			quer.add(type);
			quer.add(i);
			querInd[farmA].add(quer);
			querInd[farmB].add(quer);
		}
		reader.close();
		
		ArrayList<Integer> preOrd = new ArrayList<>();
		ArrayList<Integer> postOrd = new ArrayList<>();
		buildTree(0, 0, roads, preOrd, postOrd);
		int[] preOrdInd = new int[numFarms];
		int[] postOrdInd = new int[numFarms];
		for (int i = 0; i < numFarms; i++) {
			preOrdInd[preOrd.get(i)] = i;
			postOrdInd[postOrd.get(i)] = i;
		}
		
		byte[] ans = new byte[numFriends];
		Stack<Integer>[] farmTypes = new Stack[numFarms];
		Stack<Integer>[] depthTypes = new Stack[numFarms];
		for (int i = 0; i < numFarms; i++) {
			farmTypes[i] = new Stack<>();
			depthTypes[i] = new Stack<>();
		}
		Stack<Integer> ord = new Stack<>();
		travTree(0, 0, ord, farmTypes, depthTypes, ans, roads, preOrdInd, postOrdInd, querInd, types);
		
		for (byte ansDig : ans) {
			writer.print(ansDig);
		}
		writer.println();
		writer.close();
	}
	
	private static void buildTree(int farm, int par, HashSet<Integer>[] roads, ArrayList<Integer> preOrd, ArrayList<Integer> postOrd) {
		roads[farm].remove(par);
		preOrd.add(farm);
		for (int child : roads[farm]) {
			buildTree(child, farm, roads, preOrd, postOrd);
		}
		postOrd.add(farm);
	}
	
	private static void travTree(int farm, int depth, Stack<Integer> ord, Stack<Integer>[] farmTypes, Stack<Integer>[] depthTypes, byte[] ans, HashSet<Integer>[] roads, int[] preOrdInd, int[] postOrdInd, ArrayList<ArrayList<Integer>>[] querInd, int[] types) {
		ord.push(farm);
		int type = types[farm];
		farmTypes[type].push(farm);
		depthTypes[type].push(depth);
		
		for (ArrayList<Integer> quer : querInd[farm]) {
			if (ans[quer.get(3)] == 1) {
				continue;
			}
			
			int otherFarm = quer.get(0);
			if (otherFarm == farm) {
				otherFarm = quer.get(1);
			}
			int querType = quer.get(2);
			
			if (farmTypes[querType].isEmpty()) {
				continue;
			}
			int farmType = farmTypes[querType].peek();
			int depthType = depthTypes[querType].peek();
			if (farmType == farm || !isAnc(otherFarm, farmType, preOrdInd, postOrdInd) || !isAnc(otherFarm, ord.get(depthType + 1), preOrdInd, postOrdInd)) {
				ans[quer.get(3)] = 1;
			}
		}
		
		for (int child : roads[farm]) {
			travTree(child, depth + 1, ord, farmTypes, depthTypes, ans, roads, preOrdInd, postOrdInd, querInd, types);
		}
		
		ord.pop();
		farmTypes[type].pop();
		depthTypes[type].pop();
	}
	
	private static boolean isAnc(int child, int anc, int[] preOrdInd, int[] postOrdInd) {
		return (preOrdInd[child] >= preOrdInd[anc] && postOrdInd[child] <= postOrdInd[anc]);
	}
}