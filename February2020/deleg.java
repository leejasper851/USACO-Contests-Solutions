/*
NAME: leejasp1
LANG: JAVA
PROG: deleg
*/

package February2020;

import java.io.*;
import java.util.*;

public class deleg {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("deleg.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("deleg.out")));
		
		int numPasts = Integer.parseInt(reader.readLine());
		int numRoads = numPasts - 1;
		ArrayList<Integer>[] childs = new ArrayList[numPasts];
		for (int i = 0; i < numPasts; i++) {
			childs[i] = new ArrayList<>();
		}
		for (int i = 0; i < numRoads; i++) {
			StringTokenizer st = new StringTokenizer(reader.readLine());
			int past1 = Integer.parseInt(st.nextToken())-1;
			int past2 = Integer.parseInt(st.nextToken())-1;
			childs[past1].add(past2);
			childs[past2].add(past1);
		}
		reader.close();
		
		int numOdd = 0;
		for (int i = 0; i <= numRoads; i++) {
			if (childs[i].size() % 2 == 1) {
				numOdd++;
			}
		}
		
		makeTree(0, -1, childs);
		
		writer.print(1);
		for (int i = 2; i <= numRoads; i++) {
			if (numRoads % i != 0) {
				writer.print(0);
				continue;
			}
			
			int oddPoints = numRoads / i * 2;
			if (oddPoints < numOdd) {
				writer.print(0);
				continue;
			}
			
			if (findPaths(0, i, childs) == 0) {
				writer.print(1);
			} else {
				writer.print(0);
			}
		}
		writer.close();
	}
	
	private static void makeTree(int node, int currPar, ArrayList<Integer>[] childs) {
		for (int i = 0; i < childs[node].size(); i++) {
			if (childs[node].get(i) == currPar) {
				childs[node].remove(i);
				break;
			}
		}
		
		for (int i = 0; i < childs[node].size(); i++) {
			makeTree(childs[node].get(i), node, childs);
		}
	}
	
	private static int findPaths(int node, int pathLen, ArrayList<Integer>[] childs) {
		if (childs[node].isEmpty()) {
			return 0;
		}
		
		if (childs[node].size() == 1) {
			int childPath = findPaths(childs[node].get(0), pathLen, childs);
			if (childPath == -1) {
				return -1;
			}
			return (childPath + 1) % pathLen;
		}
		
		HashMap<Integer, Integer> childPaths = new HashMap<>();
		int pathCount = 0;
		for (int i = 0; i < childs[node].size(); i++) {
			int childPath = findPaths(childs[node].get(i), pathLen, childs);
			if (childPath == -1) {
				return -1;
			}
			
			childPath = (childPath + 1) % pathLen;
			if (childPath == 0) {
				continue;
			}
			if (childPaths.containsKey(pathLen - childPath)) {
				childPaths.put(pathLen - childPath, childPaths.get(pathLen - childPath) - 1);
				childPaths.remove(pathLen - childPath, 0);
				pathCount--;
			} else {
				if (childPaths.containsKey(childPath)) {
					childPaths.put(childPath, childPaths.get(childPath) + 1);
				} else {
					childPaths.put(childPath, 1);
				}
				pathCount++;
			}
		}
		
		if (pathCount > 1) {
			return -1;
		}
		if (pathCount == 1) {
			return childPaths.keySet().iterator().next();
		}
		return 0;
	}
}