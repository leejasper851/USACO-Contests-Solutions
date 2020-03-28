/*
NAME: leejasp1
LANG: JAVA
PROG: dishes
*/

package February2019;

import java.io.*;
import java.util.*;

public class dishes {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("dishes.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("dishes.out")));
		
		int numPlates = Integer.parseInt(reader.readLine());
		LinkedList<Integer> plates = new LinkedList<>();
		for (int i = 0; i < numPlates; i++) {
			int plate = Integer.parseInt(reader.readLine())-1;
			plates.add(plate);
		}
		reader.close();
		
		int plateCount = 0;
		int maxPlate = -1;
		Stack<Integer>[] stacks = new Stack[numPlates];
		for (int i = 0; i < numPlates; i++) {
			stacks[i] = new Stack<>();
		}
		int[] plateStack = new int[numPlates];
		Arrays.fill(plateStack, -1);
		for (int i = 0; i < numPlates; i++) {
			int plate = plates.remove();
			if (plate < maxPlate) {
				plateCount = i;
				break;
			}
			
			for (int j = plate; j >= 0 && plateStack[j] == -1; j--) {
				plateStack[j] = plate;
			}
			int stack = plateStack[plate];
			while (!stacks[stack].isEmpty() && stacks[stack].peek() < plate) {
				maxPlate = stacks[stack].pop();
			}
			stacks[stack].push(plate);
		}
		writer.println(plateCount);
		writer.close();
	}
}