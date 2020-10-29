package com.dbengine;

import java.util.ArrayList;
import java.util.List;

public class Operation {
	int m;
	BPlusTree bpt;
	Table table;

	public Operation(int m) {
		this.m = m;
		bpt = new BPlusTree(this.m);
		table = new Table();
	}

	public Double search(int key) {

		Double found = null;
		found = bpt.search(key);

		if (found != null) {
			return found;
		}

		return found;
	}

	public boolean update(DictionaryPair dtp) {
		int key;
		Double value;

		if (dtp != null) {
			key = dtp.key;
			value = dtp.value;
			bpt.update(key, value);
			table.updateTable(key, value);
			return true;
		}

		return false;
	}

	public boolean load() {
		int i;
		int key;
		double value;

		List<DictionaryPair> dtp = new ArrayList<DictionaryPair>();
		dtp = table.readTable();

		if (dtp != null) {
			for (i = 0; i < dtp.size(); i++) {
				key = dtp.get(i).key;
				value = dtp.get(i).value;
				bpt.insert(key, value);
			}

			return true;
		}
		return false;
	}

	public boolean insert(List<DictionaryPair> dtp) {
		int i;
		int key;
		double value;
		String concat = "";

		if (dtp != null) {
			for (i = 0; i < dtp.size(); i++) {
				key = dtp.get(i).key;
				value = dtp.get(i).value;
				bpt.insert(key, value);

				concat += key + "-" + value + "\n";
			}

			table.writeTable(concat);
		}

		return true;
	}

}
