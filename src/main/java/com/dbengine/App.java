package com.dbengine;

public class App {
	public static void main(String[] args) {
		BPlusTree bpt = null;
		bpt = new BPlusTree(3);
		bpt.insert(20, 21);
		bpt.insert(21, 22);
		bpt.insert(23, 24);
		bpt.insert(25, 26);
		bpt.insert(27, 28);
		bpt.insert(29, 30);
		bpt.insert(31, 32);
		
		Double found = null; 
		found = bpt.search(31);
		
		if ( found != null) {
			System.out.println("Valor encontrado: " + found);
		} else {
			System.out.println("No encontrado");
		}
	}
}
