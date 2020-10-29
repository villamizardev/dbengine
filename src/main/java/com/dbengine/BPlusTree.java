package com.dbengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * @author villa
 *
 */
public class BPlusTree {
	int m;
	InternalNode root;
	LeafNode firstLeaf;

	public BPlusTree() {

	}

	public BPlusTree(int m) {
		this.m = m;
		this.root = null;
	}

	/**
	 * Insert new node to tree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(int key, double value) {
		if (isEmpty()) {
			LeafNode leafNode = new LeafNode(this.m, new DictionaryPair(key, value));
			this.firstLeaf = leafNode;		
		} else {
			LeafNode leafNode = (this.root == null) ? this.firstLeaf : findLeafNode(key);
			if (!leafNode.insert(new DictionaryPair(key, value))) {

				leafNode.dictionary[leafNode.numPairs] = new DictionaryPair(key, value);
				leafNode.numPairs++;
				sortDictionary(leafNode.dictionary);

				int midpoint = getMidpoint();
				DictionaryPair[] halfDictionary = splitDictionary(leafNode, midpoint);

				if (leafNode.parent == null) {

					Integer[] parentKeys = new Integer[this.m];
					parentKeys[0] = halfDictionary[0].key;
					InternalNode parent = new InternalNode(this.m, parentKeys);
					leafNode.parent = parent;
					parent.appendChildPointer(leafNode);

				} else {
					int newParentKey = halfDictionary[0].key;
					leafNode.parent.keys[leafNode.parent.degree - 1] = newParentKey;
					Arrays.sort(leafNode.parent.keys, 0, leafNode.parent.degree);
				}

				LeafNode newLeafNode = new LeafNode(this.m, halfDictionary, leafNode.parent);

				int pointerIndex = leafNode.parent.findIndexOfPointer(leafNode) + 1;
				leafNode.parent.insertChildPointer(newLeafNode, pointerIndex);

				newLeafNode.rightSibling = leafNode.rightSibling;
				if (newLeafNode.rightSibling != null) {
					newLeafNode.rightSibling.leftSibling = newLeafNode;
				}
				leafNode.rightSibling = newLeafNode;
				newLeafNode.leftSibling = leafNode;

				if (this.root == null) {

					this.root = leafNode.parent;

				} else {
					InternalNode in = leafNode.parent;
					while (in != null) {
						if (in.isOverfull()) {
							splitInternalNode(in);
						} else {
							break;
						}
						in = in.parent;
					}
				}
			}
		}
	}

	/**
	 * Check root is null
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return firstLeaf == null;
	}

	/**
	 * Find the leaf node
	 * 
	 * @param key
	 * @return
	 */
	public LeafNode findLeafNode(int key) {

		Integer[] keys = this.root.keys;
		int i;

		for (i = 0; i < this.root.degree - 1; i++) {
			if (key < keys[i]) {
				break;
			}
		}

		Node child = this.root.childPointers[i];
		if (child instanceof LeafNode) {
			return (LeafNode) child;
		} else {
			return findLeafNode((InternalNode) child, key);
		}
	}

	/**
	 * Find the leaf node
	 * 
	 * @param node
	 * @param key
	 * @return
	 */
	public LeafNode findLeafNode(InternalNode node, int key) {

		Integer[] keys = node.keys;
		int i;

		for (i = 0; i < node.degree - 1; i++) {
			if (key < keys[i]) {
				break;
			}
		}
		Node childNode = node.childPointers[i];
		if (childNode instanceof LeafNode) {
			return (LeafNode) childNode;
		} else {
			return findLeafNode((InternalNode) node.childPointers[i], key);
		}
	}

	/**
	 * Sort dictionary only
	 * 
	 * @param dictionary
	 */
	public void sortDictionary(DictionaryPair[] dictionary) {
		Arrays.sort(dictionary, new Comparator<DictionaryPair>() {
			public int compare(DictionaryPair o1, DictionaryPair o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return 1;
				}
				if (o2 == null) {
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
	}

	/**
	 * Get the mid point
	 * 
	 * @return
	 */
	public int getMidpoint() {
		return (int) Math.ceil((this.m + 1) / 2.0) - 1;
	}

	/**
	 * Split elements of dictionary to balance nodes
	 * 
	 * @param ln
	 * @param split
	 * @return
	 */
	public DictionaryPair[] splitDictionary(LeafNode leafNode, int midPoint) {

		DictionaryPair[] dictionary = leafNode.dictionary;

		DictionaryPair[] halfDictionary = new DictionaryPair[this.m];

		for (int i = midPoint; i < dictionary.length; i++) {
			halfDictionary[i - midPoint] = dictionary[i];
			leafNode.delete(i);
		}

		return halfDictionary;
	}

	/**
	 * Binary search
	 * 
	 * @param dps
	 * @param numPairs
	 * @param t
	 * @return
	 */
	public int binarySearch(DictionaryPair[] dps, int numPairs, int t) {
		Comparator<DictionaryPair> c = new Comparator<DictionaryPair>() {
			public int compare(DictionaryPair o1, DictionaryPair o2) {
				Integer a = Integer.valueOf(o1.key);
				Integer b = Integer.valueOf(o2.key);
				return a.compareTo(b);
			}
		};
		return Arrays.binarySearch(dps, 0, numPairs, new DictionaryPair(t, 0), c);
	}

	/**
	 * Finding the index of the pointer
	 * 
	 * @param pointers
	 * @param node
	 * @return
	 */
	public int findIndexOfPointer(Node[] pointers, LeafNode node) {
		int i;
		for (i = 0; i < pointers.length; i++) {
			if (pointers[i] == node) {
				break;
			}
		}
		return i;
	}

	/**
	 * Balance the tree
	 * 
	 * @param in
	 */
	public void handleDeficiency(InternalNode in) {

		InternalNode sibling;
		InternalNode parent = in.parent;

		if (this.root == in) {
			for (int i = 0; i < in.childPointers.length; i++) {
				if (in.childPointers[i] != null) {
					if (in.childPointers[i] instanceof InternalNode) {
						this.root = (InternalNode) in.childPointers[i];
						this.root.parent = null;
					} else if (in.childPointers[i] instanceof LeafNode) {
						this.root = null;
					}
				}
			}
		}

		else if (in.leftSibling != null && in.leftSibling.isLendable()) {
			sibling = in.leftSibling;
		} else if (in.rightSibling != null && in.rightSibling.isLendable()) {
			sibling = in.rightSibling;

			int borrowedKey = sibling.keys[0];
			Node pointer = sibling.childPointers[0];

			in.keys[in.degree - 1] = parent.keys[0];
			in.childPointers[in.degree] = pointer;

			parent.keys[0] = borrowedKey;

			sibling.removePointer(0);
			Arrays.sort(sibling.keys);
			sibling.removePointer(0);
			shiftDown(in.childPointers, 1);
		} else if (in.leftSibling != null && in.leftSibling.isMergeable()) {

		} else if (in.rightSibling != null && in.rightSibling.isMergeable()) {
			sibling = in.rightSibling;
			sibling.keys[sibling.degree - 1] = parent.keys[parent.degree - 2];
			Arrays.sort(sibling.keys, 0, sibling.degree);
			parent.keys[parent.degree - 2] = null;

			for (int i = 0; i < in.childPointers.length; i++) {
				if (in.childPointers[i] != null) {
					sibling.prependChildPointer(in.childPointers[i]);
					in.childPointers[i].parent = sibling;
					in.removePointer(i);
				}
			}

			parent.removePointer(in);

			sibling.leftSibling = in.leftSibling;
		}

		if (parent != null && parent.isDeficient()) {
			handleDeficiency(parent);
		}
	}

	/**
	 * Check null elements of dictionaryPair
	 * 
	 * @param dps
	 * @return
	 */
	public int linearNullSearch(DictionaryPair[] dps) {
		for (int i = 0; i < dps.length; i++) {
			if (dps[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Check null pointer of nodes
	 * 
	 * @param pointers
	 * @return
	 */
	public int linearNullSearch(Node[] pointers) {
		for (int i = 0; i < pointers.length; i++) {
			if (pointers[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Shift down node to balance nodes
	 * 
	 * @param pointers
	 * @param amount
	 */
	public void shiftDown(Node[] pointers, int amount) {
		Node[] newPointers = new Node[this.m + 1];
		for (int i = amount; i < pointers.length; i++) {
			newPointers[i - amount] = pointers[i];
		}
		pointers = newPointers;
	}

	/**
	 * Split child pointers to balance nodes
	 * 
	 * @param in
	 * @param split
	 * @return
	 */
	private Node[] splitChildPointers(InternalNode in, int split) {

		Node[] pointers = in.childPointers;
		Node[] halfPointers = new Node[this.m + 1];

		for (int i = split + 1; i < pointers.length; i++) {
			halfPointers[i - split - 1] = pointers[i];
			in.removePointer(i);
		}

		return halfPointers;
	}

	/**
	 * Split internal nodes to balances all nodes, according degree number
	 * 
	 * @param in
	 */
	private void splitInternalNode(InternalNode in) {

		InternalNode parent = in.parent;

		int midpoint = getMidpoint();
		int newParentKey = in.keys[midpoint];
		Integer[] halfKeys = splitKeys(in.keys, midpoint);
		Node[] halfPointers = splitChildPointers(in, midpoint);

		in.degree = linearNullSearch(in.childPointers);

		InternalNode sibling = new InternalNode(this.m, halfKeys, halfPointers);
		for (Node pointer : halfPointers) {
			if (pointer != null) {
				pointer.parent = sibling;
			}
		}

		sibling.rightSibling = in.rightSibling;
		if (sibling.rightSibling != null) {
			sibling.rightSibling.leftSibling = sibling;
		}
		in.rightSibling = sibling;
		sibling.leftSibling = in;

		if (parent == null) {

			Integer[] keys = new Integer[this.m];
			keys[0] = newParentKey;
			InternalNode newRoot = new InternalNode(this.m, keys);
			newRoot.appendChildPointer(in);
			newRoot.appendChildPointer(sibling);
			this.root = newRoot;

			in.parent = newRoot;
			sibling.parent = newRoot;

		} else {

			parent.keys[parent.degree - 1] = newParentKey;
			Arrays.sort(parent.keys, 0, parent.degree);

			int pointerIndex = parent.findIndexOfPointer(in) + 1;
			parent.insertChildPointer(sibling, pointerIndex);
			sibling.parent = parent;
		}
	}

	/**
	 * Split node keys to balances all nodes
	 * 
	 * @param keys
	 * @param split
	 * @return
	 */
	public Integer[] splitKeys(Integer[] keys, int split) {

		Integer[] halfKeys = new Integer[this.m];

		keys[split] = null;

		for (int i = split + 1; i < keys.length; i++) {
			halfKeys[i - split - 1] = keys[i];
			keys[i] = null;
		}

		return halfKeys;
	}
	
	/**
	 * Search node using node key
	 * 
	 * @param key
	 * @return
	 */
	public Double search(int key) {

		if (isEmpty()) {
			return null;
		}

		LeafNode leafNode = (this.root == null) ? this.firstLeaf : findLeafNode(key);

		DictionaryPair[] dps = leafNode.dictionary;
		int index = binarySearch(dps, leafNode.numPairs, key);

		if (index < 0) {
			return null;
		} else {
			return dps[index].value;
		}
	}
	
	/**
	 * Search node using node key
	 * 
	 * @param key
	 * @return
	 */
	public Boolean update(int key, Double value) {

		if (isEmpty()) {
			return null;
		}

		LeafNode leafNode = (this.root == null) ? this.firstLeaf : findLeafNode(key);

		DictionaryPair[] dps = leafNode.dictionary;
		int index = binarySearch(dps, leafNode.numPairs, key);

		if (index < 0) {
			return null;
		} else {
			dps[index].value = value;
			return true;
		}
	}

	public ArrayList<Double> search(int lowerBound, int upperBound) {

		ArrayList<Double> values = new ArrayList<Double>();

		LeafNode currNode = this.firstLeaf;
		while (currNode != null) {

			DictionaryPair dps[] = currNode.dictionary;
			for (DictionaryPair dp : dps) {

				if (dp == null) {
					break;
				}

				if (lowerBound <= dp.key && dp.key <= upperBound) {
					values.add(dp.value);
				}
			}
			currNode = currNode.rightSibling;

		}

		return values;
	}
}
