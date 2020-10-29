package com.dbengine;

import java.util.Arrays;

public class LeafNode extends Node{
	int maxNumPairs;
    int minNumPairs;
    int numPairs;
    BPlusTree bpt = new BPlusTree ();
    LeafNode leftSibling;
    LeafNode rightSibling;
    DictionaryPair[] dictionary;
    
    public LeafNode(int m, DictionaryPair dp) {
        this.maxNumPairs = m - 1;
        this.minNumPairs = (int) (Math.ceil(m / 2) - 1);
        this.dictionary = new DictionaryPair[m];
        this.numPairs = 0;
        this.insert(dp);
      }

      public LeafNode(int m, DictionaryPair[] dps, InternalNode parent) {
        this.maxNumPairs = m - 1;
        this.minNumPairs = (int) (Math.ceil(m / 2) - 1);
        this.dictionary = dps;
        this.numPairs = bpt.linearNullSearch(dps);
        this.parent = parent;
      }
      
    public void delete(int index) {
      this.dictionary[index] = null;
      numPairs--;
    }

    public boolean insert(DictionaryPair dp) {
      if (this.isFull()) {
        return false;
      } else {
        this.dictionary[numPairs] = dp;
        numPairs++;
        Arrays.sort(this.dictionary, 0, numPairs);
        return true;
      }
    }

    public boolean isDeficient() {
      return numPairs < minNumPairs;
    }

    public boolean isFull() {
      return numPairs == maxNumPairs;
    }

    public boolean isLendable() {
      return numPairs > minNumPairs;
    }

    public boolean isMergeable() {
      return numPairs == minNumPairs;
    }
}
