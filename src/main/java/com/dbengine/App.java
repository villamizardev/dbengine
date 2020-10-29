package com.dbengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	public static void main(String[] args) {

		List<DictionaryPair> list = new ArrayList<DictionaryPair>();
		Operation operation = new Operation(3);
		operation.load();

		int option = 9;
		int key;
		Double value;
		String keyboard;

		while (true) {
			System.out.println("**************************************");
			System.out.println("*****D A T A B A S E  E N G I N E*****");
			System.out.println("");
			System.out.println("[a] Insertar");
			System.out.println("[b] Buscar");
			System.out.println("[c] Actualizar");
			System.out.println("[x] Salir del programa");
			System.out.println("");
			System.out.print("escribe la opcion: ");

			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			keyboard = scan.nextLine();

			if (keyboard.toLowerCase().equals("a")) {
				option = 1;
			}

			if (keyboard.toLowerCase().equals("b")) {
				option = 2;
			}

			if (keyboard.toLowerCase().equals("c")) {
				option = 3;
			}

			if (keyboard.toLowerCase().equals("x")) {
				break;
			}

			switch (option) {
			case 1:
				while (true) {
					System.out.println("**************");
					System.out.print("Inserte el ID: ");
					keyboard = scan.nextLine();
					key = parserInt(keyboard);

					System.out.print("Inserte el valor: ");
					keyboard = scan.nextLine();
					value = parserDouble(keyboard);

					DictionaryPair dtp = new DictionaryPair(key, value);
					list.add(dtp);

					System.out.println("¿Agregar otro? S/N: ");
					keyboard = scan.nextLine();
					if (keyboard.toLowerCase().equals("s")) {
						continue;
					} else {
						break;
					}
				}

				if (operation.insert(list)) {
					System.out.println("Datos Agregados con Éxito");
				} else {
					System.out.println("Error al registrar los datos");
				}
				
				break;
			case 2:
				while (true) {
					System.out.print("Inserte el ID a buscar: ");
					keyboard = scan.nextLine();
					key = parserInt(keyboard);

					Double found = operation.search(key);

					if (found != null) {
						System.out.println("Valor encontrado: " + found);
					} else {
						System.out.println("No Existe");
					}
					
					System.out.println("¿Buscar otro? S/N: ");
					keyboard = scan.nextLine();
					if (keyboard.toLowerCase().equals("s")) {
						continue;
					} else {
						break;
					}
				} 
				
				break;
			case 3:
				while (true) {
					System.out.print("Inserte el ID a Actualizar: ");
					keyboard = scan.nextLine();
					key = parserInt(keyboard);

					System.out.print("Inserte el nuevo valor: ");
					keyboard = scan.nextLine();
					value = parserDouble(keyboard);

					DictionaryPair dtp = new DictionaryPair(key, value);
					boolean update = operation.update(dtp);

					if (update) {
						System.out.println("Valor actualizado con éxito: " + operation.search(key));
					} else {
						System.out.println("Error al actualizar");
					}

					System.out.println("¿Actualizar otro? S/N: ");
					keyboard = scan.nextLine();
					if (keyboard.toLowerCase().equals("s")) {
						continue;
					} else {
						break;
					}
				}
				
				break;
			default:
				System.out.println("Esa no es una opción válida");
				break;
			}
		}
		System.out.println("");
		System.out.println("Has salido del programa");
	}

	public static int parserInt(String str) {
		int num;
		num = Integer.valueOf(str);
		return num;
	}

	public static Double parserDouble(String str) {
		Double num;
		num = Double.parseDouble(str);
		return num;
	}
}
