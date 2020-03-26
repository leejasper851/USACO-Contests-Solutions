/*
NAME: leejasp1
LANG: JAVA
PROG: balance
*/

package USOpen2019;

import java.io.*;
import java.util.*;

public class balance {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("balance.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("balance.out")));
		
		int len = Integer.parseInt(reader.readLine());
		boolean[] board = new boolean[len * 2];
		StringTokenizer st = new StringTokenizer(reader.readLine());
		for (int i = 0; i < len * 2; i++) {
			board[i] = st.nextToken().equals("1");
		}
		reader.close();
		
		Stack<Integer> left0s = new Stack<>();
		Stack<Integer> left1s = new Stack<>();
		Stack<Integer> right0s = new Stack<>();
		Stack<Integer> right1s = new Stack<>();
		for (int i = 0; i < len; i++) {
			if (board[i]) {
				left1s.push(i);
			} else {
				left0s.push(i);
			}
		}
		for (int i = len * 2 - 1; i >= len; i--) {
			if (board[i]) {
				right1s.push(i);
			} else {
				right0s.push(i);
			}
		}
		long cenDiff = left1s.size() + right1s.size() - len;
		
		long origDiff = 0;
		int num1s = 0;
		for (int i = 0; i < len; i++) {
			if (board[i]) {
				num1s++;
			} else {
				origDiff += num1s;
			}
		}
		num1s = 0;
		for (int i = len; i < len * 2; i++) {
			if (board[i]) {
				num1s++;
			} else {
				origDiff -= num1s;
			}
		}
		
		long minMoves = Math.abs(origDiff);
		long moves = 0;
		long diff = origDiff;
		int numCenSwaps = Math.min(left1s.size(), right0s.size());
		for (int i = 0; i < numCenSwaps; i++) {
			int left1 = left1s.pop();
			int right0 = right0s.pop();
			long left1Moves = len-1 - left1;
			moves += left1Moves;
			diff -= left1Moves;
			long right0Moves = right0 - len;
			moves += right0Moves;
			diff += right0Moves;
			moves++;
			diff += cenDiff;
			long newMoves = moves + Math.abs(diff);
			minMoves = Math.min(minMoves, newMoves);
		}
		
		moves = 0;
		diff = origDiff;
		numCenSwaps = Math.min(left0s.size(), right1s.size());
		for (int i = 0; i < numCenSwaps; i++) {
			int left0 = left0s.pop();
			int right1 = right1s.pop();
			long left0Moves = len-1 - left0;
			moves += left0Moves;
			diff += left0Moves;
			long right1Moves = right1 - len;
			moves += right1Moves;
			diff -= right1Moves;
			moves++;
			diff -= cenDiff;
			long newMoves = moves + Math.abs(diff);
			minMoves = Math.min(minMoves, newMoves);
		}
		writer.println(minMoves);
		writer.close();
	}
}