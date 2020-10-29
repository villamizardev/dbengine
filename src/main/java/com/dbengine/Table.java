package com.dbengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Table {
	private final String TABLE_NAME = "table.txt";
	int key;
	Double value;

	public boolean createTable() {
		try {
			File myObj = new File(TABLE_NAME);
			if (myObj.createNewFile()) {
				System.out.println("Tabla creada");
				return true;
			} else {
				System.out.println("Tabla creada");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean writeTable(String str) {
		if (!createTable()) {
			return false;
		} else {
			try {
				FileWriter wtable = new FileWriter(TABLE_NAME);
				wtable.write(str);
				wtable.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public List<DictionaryPair> readTable() {
		List<DictionaryPair> list = new ArrayList<DictionaryPair>();
		String[] split;
		int count = 0;

		try {
			createTable();
			File table = new File(TABLE_NAME);
			Scanner reader = new Scanner(table);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				split = data.split("-");

				for (String a : split) {
					if (count == 0) {
						this.key = Integer.valueOf(a);
					}
					if (count == 1) {
						this.value = Double.parseDouble(a);
					}
					count++;
				}
				count = 0;

				DictionaryPair dtp = new DictionaryPair(this.key, this.value);
				list.add(dtp);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean updateTable(int keyUpdate, Double valueUpdate) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		String[] split;
		int count = 0;
		String concat = "";

		try {
			File table = new File(TABLE_NAME);
			Scanner reader = new Scanner(table);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				split = data.split("-");

				for (String a : split) {
					if (count == 0) {
						this.key = Integer.valueOf(a);
					}

					if (count == 1) {
						this.value = Double.parseDouble(a);
					}

					count++;
				}

				map.put(key, value);

				count = 0;
			}

			Iterator<Integer> it = map.keySet().iterator();
			while (it.hasNext()) {
				Integer keyMap = (Integer) it.next();
				if (keyMap == keyUpdate) {
					map.put(keyMap, valueUpdate);
				}

				concat += keyMap + "-" + map.get(keyMap) + "\n";
			}

			reader.close();
			table.delete();
			writeTable(concat);

			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}
