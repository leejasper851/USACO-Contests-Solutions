/*
NAME: leejasp1
LANG: JAVA
PROG: pump
*/

package December2019;

import java.io.*;
import java.util.*;

public class pump {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("pump.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("pump.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numJuncs = Integer.parseInt(st.nextToken());
		int numPipes = Integer.parseInt(st.nextToken());
		ArrayList<ArrayList<Integer>> juncs = new ArrayList<>();
		ArrayList<ArrayList<Integer>> costs = new ArrayList<>();
		ArrayList<ArrayList<Integer>> flows = new ArrayList<>();
		for (int i = 0; i <= numJuncs; i++) {
			juncs.add(new ArrayList<Integer>());
			costs.add(new ArrayList<Integer>());
			flows.add(new ArrayList<Integer>());
		}
		HashSet<Integer> flowSet = new HashSet<>();
		for (int i = 0; i < numPipes; i++) {
			st = new StringTokenizer(reader.readLine());
			int pipeA = Integer.parseInt(st.nextToken());
			int pipeB = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			int flow = Integer.parseInt(st.nextToken());
			juncs.get(pipeA).add(pipeB);
			juncs.get(pipeB).add(pipeA);
			costs.get(pipeA).add(cost);
			costs.get(pipeB).add(cost);
			flows.get(pipeA).add(flow);
			flows.get(pipeB).add(flow);
			flowSet.add(flow);
		}
		reader.close();
		
		double bestVal = 0;
		for (int flow : flowSet) {
			int[] data = getCost(flow, juncs, costs, flows);
			if (data[0] == -1) {
				continue;
			}
			double val = ((double) data[1]) / data[0];
			bestVal = Math.max(bestVal, val);
		}
		writer.println((int) (bestVal * 1000000));
		writer.close();
	}
	
	private static int[] getCost(int minFlow, ArrayList<ArrayList<Integer>> juncs, ArrayList<ArrayList<Integer>> costs, ArrayList<ArrayList<Integer>> flows) {
		int numJuncs = juncs.size()-1;
		int[] dist = new int[numJuncs+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[1] = 0;
		boolean[] visited = new boolean[numJuncs+1];
		TreeSet<Integer> juncSet = new TreeSet<Integer>(new JuncComp(dist));
		juncSet.add(1);
		int[] parents = new int[numJuncs+1];
		parents[1] = 1;
		int[] parFlow = new int[numJuncs+1];
		while (!juncSet.isEmpty()) {
			int currNode = juncSet.pollFirst();
			if (currNode == numJuncs) {
				break;
			}
			visited[currNode] = true;
			for (int i = 0; i < juncs.get(currNode).size(); i++) {
				int adjNode = juncs.get(currNode).get(i);
				if (visited[adjNode] || flows.get(currNode).get(i) < minFlow) {
					continue;
				}
				if (dist[currNode] + costs.get(currNode).get(i) < dist[adjNode]) {
					dist[adjNode] = dist[currNode] + costs.get(currNode).get(i);
					parents[adjNode] = currNode;
					parFlow[adjNode] = flows.get(currNode).get(i);
					juncSet.remove(adjNode);
					juncSet.add(adjNode);
				}
			}
		}
		if (dist[numJuncs] == Integer.MAX_VALUE) {
			return new int[] {-1};
		}
		
		int realMinFlow = Integer.MAX_VALUE;
		int currNode = numJuncs;
		while (currNode != 1) {
			realMinFlow = Math.min(realMinFlow, parFlow[currNode]);
			currNode = parents[currNode];
		}
		return new int[] {dist[numJuncs], realMinFlow};
	}
	
	private static class JuncComp implements Comparator<Integer> {
		public int[] dist;
		
		public JuncComp(int[] d) {
			dist = d;
		}
		
		public int compare(Integer a, Integer b) {
			if (dist[a] == dist[b]) {
				return a - b;
			}
			return dist[a] - dist[b];
		}
	}
}