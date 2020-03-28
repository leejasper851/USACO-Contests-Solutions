/*
NAME: leejasp1
LANG: JAVA
PROG: cowland
*/

package February2019;

import java.io.*;
import java.util.*;

public class cowland {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("cowland.in"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("cowland.out")));
		
		StringTokenizer st = new StringTokenizer(reader.readLine());
		int numAttrs = Integer.parseInt(st.nextToken());
		int numQuers = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(reader.readLine());
		Attr[] attrs = new Attr[numAttrs];
		for (int i = 0; i < numAttrs; i++) {
			int val = Integer.parseInt(st.nextToken());
			attrs[i] = new Attr(i, val);
		}
		ArrayList<Integer>[] paths = new ArrayList[numAttrs];
		for (int i = 0; i < numAttrs; i++) {
			paths[i] = new ArrayList<>();
		}
		for (int i = 0; i < numAttrs - 1; i++) {
			st = new StringTokenizer(reader.readLine());
			int attrA = Integer.parseInt(st.nextToken())-1;
			int attrB = Integer.parseInt(st.nextToken())-1;
			paths[attrA].add(attrB);
			paths[attrB].add(attrA);
		}
		
		buildTree(0, -1, paths, attrs);
		setTreeDepths(attrs[0], 0);
		setTreeSizes(attrs[0]);
		setTreeWeights(attrs[0], attrs[0]);
		setTreeAncs(attrs[0]);
		
		ArrayList<Integer> segOrd = new ArrayList<>();
		setSegOrd(attrs[0], segOrd);
		int minPow2 = (int) Math.ceil(Math.log(numAttrs) / Math.log(2));
		int segSize = (int) (2 * Math.pow(2, minPow2) - 1);
		int[] segTree = new int[segSize];
		buildSegTree(segTree, 0, 0, numAttrs-1, segOrd, attrs);
		
		for (int i = 0; i < numQuers; i++) {
			st = new StringTokenizer(reader.readLine());
			int quer = Integer.parseInt(st.nextToken());
			if (quer == 1) {
				int attrInd = Integer.parseInt(st.nextToken())-1;
				int val = Integer.parseInt(st.nextToken());
				
				Attr attr = attrs[attrInd];
				attr.val = val;
				updateSegTree(segTree, attr.segInd, val);
			} else {
				int attrAInd = Integer.parseInt(st.nextToken())-1;
				int attrBInd = Integer.parseInt(st.nextToken())-1;
				
				Attr attrA = attrs[attrAInd];
				Attr attrB = attrs[attrBInd];
				Attr ancA = attrA;
				Attr ancB = attrB;
				while (ancA.depth > ancB.depth) {
					for (int j = ancA.ancs.size()-1; j >= 0; j--) {
						if (ancA.ancs.get(j).depth >= ancB.depth) {
							ancA = ancA.ancs.get(j);
							break;
						}
					}
				}
				while (ancB.depth > ancA.depth) {
					for (int j = ancB.ancs.size()-1; j >= 0; j--) {
						if (ancB.ancs.get(j).depth >= ancA.depth) {
							ancB = ancB.ancs.get(j);
							break;
						}
					}
				}
				while (ancA.ind != ancB.ind) {
					for (int j = ancA.ancs.size()-1; j >= 0; j--) {
						if (ancA.ancs.get(j).ind == ancB.ancs.get(j).ind) {
							if (j == 0) {
								ancA = ancA.par;
								ancB = ancB.par;
								break;
							}
						} else {
							ancA = ancA.ancs.get(j);
							ancB = ancB.ancs.get(j);
							break;
						}
					}
				}
				Attr anc = ancA;
				
				int val = 0;
				while (true) {
					if (attrA.head.depth <= anc.depth) {
						int left = Math.min(attrA.segOrdInd, anc.segOrdInd);
						int right = Math.max(attrA.segOrdInd, anc.segOrdInd);
						val ^= querSegTree(segTree, left, right, 0, 0, numAttrs-1);
						break;
					} else {
						int left = Math.min(attrA.segOrdInd, attrA.head.segOrdInd);
						int right = Math.max(attrA.segOrdInd, attrA.head.segOrdInd);
						val ^= querSegTree(segTree, left, right, 0, 0, numAttrs-1);
						attrA = attrA.head.par;
					}
				}
				while (true) {
					if (attrB.head.depth <= anc.depth) {
						int left = Math.min(attrB.segOrdInd, anc.segOrdInd);
						int right = Math.max(attrB.segOrdInd, anc.segOrdInd);
						val ^= querSegTree(segTree, left, right, 0, 0, numAttrs-1);
						break;
					} else {
						int left = Math.min(attrB.segOrdInd, attrB.head.segOrdInd);
						int right = Math.max(attrB.segOrdInd, attrB.head.segOrdInd);
						val ^= querSegTree(segTree, left, right, 0, 0, numAttrs-1);
						attrB = attrB.head.par;
					}
				}
				val ^= anc.val;
				writer.println(val);
			}
		}
		reader.close();
		writer.close();
	}
	
	private static class Attr {
		public int ind;
		public int val;
		public Attr par;
		public ArrayList<Attr> childs;
		public int depth;
		public int size;
		public boolean heavy;
		public int heavyChild;
		public Attr head;
		public ArrayList<Attr> ancs;
		public int segOrdInd;
		public int segInd;
		
		public Attr(int i, int v) {
			ind = i;
			val = v;
			par = null;
			childs = new ArrayList<>();
			depth = -1;
			size = 1;
			heavy = false;
			heavyChild = -1;
			head = null;
			ancs = new ArrayList<>();
			segOrdInd = 0;
			segInd = -1;
		}
		
		public String toString() {
			return "" + ind;
		}
	}
	
	private static void buildTree(int attr, int par, ArrayList<Integer>[] paths, Attr[] attrs) {
		if (par != -1) {
			attrs[attr].par = attrs[par];
		}
		for (int path : paths[attr]) {
			if (path != par) {
				attrs[attr].childs.add(attrs[path]);
				buildTree(path, attr, paths, attrs);
			}
		}
	}
	
	private static void setTreeDepths(Attr attr, int depth) {
		attr.depth = depth;
		for (Attr child : attr.childs) {
			setTreeDepths(child, depth + 1);
		}
	}
	
	private static int setTreeSizes(Attr attr) {
		for (Attr child : attr.childs) {
			attr.size += setTreeSizes(child);
		}
		return attr.size;
	}
	
	private static void setTreeWeights(Attr attr, Attr head) {
		attr.head = head;
		int maxSize = -1;
		for (int i = 0; i < attr.childs.size(); i++) {
			Attr child = attr.childs.get(i);
			if (child.size > maxSize) {
				maxSize = child.size;
				attr.heavyChild = i;
			}
		}
		for (int i = 0; i < attr.childs.size(); i++) {
			Attr child = attr.childs.get(i);
			if (i == attr.heavyChild) {
				child.heavy = true;
				setTreeWeights(child, head);
			} else {
				child.heavy = false;
				setTreeWeights(child, child);
			}
		}
	}
	
	private static void setTreeAncs(Attr attr) {
		if (attr.depth > 0) {
			int maxAnc = (int) (Math.log(attr.depth) / Math.log(2));
			attr.ancs.add(attr.par);
			for (int i = 1; i <= maxAnc; i++) {
				attr.ancs.add(attr.ancs.get(i - 1).ancs.get(i - 1));
			}
		}
		for (Attr child : attr.childs) {
			setTreeAncs(child);
		}
	}
	
	private static void setSegOrd(Attr attr, ArrayList<Integer> segOrd) {
		segOrd.add(attr.ind);
		attr.segOrdInd = segOrd.size()-1;
		if (attr.heavyChild != -1) {
			setSegOrd(attr.childs.get(attr.heavyChild), segOrd);
		}
		for (int i = 0; i < attr.childs.size(); i++) {
			if (i != attr.heavyChild) {
				setSegOrd(attr.childs.get(i), segOrd);
			}
		}
	}
	
	private static int buildSegTree(int[] segTree, int ind, int left, int right, ArrayList<Integer> segOrd, Attr[] attrs) {
		if (left == right) {
			Attr attr = attrs[segOrd.get(left)];
			segTree[ind] = attr.val;
			attr.segInd = ind;
			return segTree[ind];
		}
		
		int mid = (left + right) / 2;
		segTree[ind] = buildSegTree(segTree, ind * 2 + 1, left, mid, segOrd, attrs) ^ buildSegTree(segTree, ind * 2 + 2, mid + 1, right, segOrd, attrs);
		return segTree[ind];
	}
	
	private static void updateSegTree(int[] segTree, int ind, int val) {
		segTree[ind] = val;
		while (ind != 0) {
			ind = (ind - 1) / 2;
			segTree[ind] = segTree[2 * ind + 1] ^ segTree[2 * ind + 2];
		}
	}
	
	private static int querSegTree(int[] segTree, int leftQuer, int rightQuer, int ind, int left, int right) {
		if (ind >= segTree.length) {
			return 0;
		}
		if (left >= leftQuer && right <= rightQuer) {
			return segTree[ind];
		}
		if (right < leftQuer || left > rightQuer) {
			return 0;
		}
		int mid = (left + right) / 2;
		return querSegTree(segTree, leftQuer, rightQuer, ind * 2 + 1, left, mid) ^ querSegTree(segTree, leftQuer, rightQuer, ind * 2 + 2, mid + 1, right);
	}
}