/*
NAME: leejasp1
LANG: JAVA
PROG: boards
*/

package January2020;

import java.io.*;
import java.util.*;

public class boards {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("boards.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("boards.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int dest = Integer.parseInt(st.nextToken());
		int numBoards = Integer.parseInt(st.nextToken());
		PriorityQueue<ArrayList<Integer>> coords = new PriorityQueue<>(new CoordComp());
		HashMap<ArrayList<Integer>, ArrayList<Integer>> pairings = new HashMap<>();
		for (int i = 0; i < numBoards; i++) {
			st = new StringTokenizer(reader.readLine());
			int x1 = Integer.parseInt(st.nextToken());
			int y1 = Integer.parseInt(st.nextToken());
			int x2 = Integer.parseInt(st.nextToken());
			int y2 = Integer.parseInt(st.nextToken());
			ArrayList<Integer> coord1 = new ArrayList<>();
			coord1.add(x1);
			coord1.add(y1);
			coord1.add(0);
			ArrayList<Integer> coord2 = new ArrayList<>();
			coord2.add(x2);
			coord2.add(y2);
			coord2.add(1);
			coords.add(coord1);
			coords.add(coord2);
			pairings.put(coord1, coord2);
		}
		reader.close();
		
		HashMap<ArrayList<Integer>, Integer> dists = new HashMap<>();
		TreeMap<Integer, Integer> prevs = new TreeMap<>();
		prevs.put(0, 0);
		
		while (!coords.isEmpty()) {
			ArrayList<Integer> coord = coords.remove();
			if (coord.get(2) == 0) {
				Integer minVal = prevs.floorEntry(coord.get(1)).getValue();
				int dist = minVal + coord.get(0) + coord.get(1);
				dists.put(coord, dist);
				dists.put(pairings.get(coord), dist);
			} else {
				int coordVal = dists.get(coord) - coord.get(0) - coord.get(1);
				int floorVal = prevs.floorEntry(coord.get(1)).getValue();
				if (coordVal >= floorVal) {
					continue;
				}
				while (!prevs.isEmpty()) {
					Integer ceilKey = prevs.ceilingKey(coord.get(1));
					if (ceilKey == null) {
						break;
					}
					int ceilVal = prevs.get(ceilKey);
					if (ceilVal >= coordVal) {
						prevs.remove(ceilKey);
					} else {
						break;
					}
				}
				prevs.put(coord.get(1), coordVal);
			}
		}
		
		Integer minVal = prevs.floorEntry(dest).getValue();
		int dist = minVal + dest * 2;
		writer.println(dist);
		writer.close();
	}
	
	private static class CoordComp implements Comparator<ArrayList<Integer>> {
		public int compare(ArrayList<Integer> a, ArrayList<Integer> b) {
			if (!a.get(0).equals(b.get(0))) {
				return a.get(0) - b.get(0);
			}
			return a.get(1) - b.get(1);
		}
	}
}