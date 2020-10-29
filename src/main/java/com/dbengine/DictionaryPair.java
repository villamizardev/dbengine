package com.dbengine;

public class DictionaryPair implements Comparable<DictionaryPair>{
	int key;
	double value;

	public DictionaryPair(int key, double value) {
		this.key = key;
		this.value = value;
	}

	public int compareTo(DictionaryPair o) {
		if (key == o.key) {
			return 0;
		} else if (key > o.key) {
			return 1;
		} else {
			return -1;
		}
	}
}
