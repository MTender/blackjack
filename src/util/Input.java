package util;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Input {

	private static final String UNRECOGNISED_ERROR = "ERROR: Unrecognised input";

	private Input() {
	}

	private static final Scanner scanner = new Scanner(System.in);

	public static boolean exit() {
		System.out.println("Press enter or type \"exit\" to exit");
		String input = scanner.nextLine();
		return input.equals("exit");
	}

	public static int betSize(int max) {
		int bet;
		System.out.println("Enter bet size: ");
		while (true) {
			try {
				bet = scanner.nextInt();
				if (5 <= bet && bet <= max) {
					break;
				} else {
					System.out.printf("ERROR: Enter a whole number from 1 to %s%n", max);
				}
			} catch (InputMismatchException ime) {
				System.out.println("ERROR: Enter a whole number");
				scanner.nextLine();
			}
		}
		scanner.nextLine();

		return bet;
	}

	public static boolean insurance() {
		while (true) {
			System.out.println("Would you like insurance? (y/n)");
			String input = scanner.nextLine().toUpperCase(Locale.ROOT);
			switch (input) {
				case "Y":
					return true;
				case "N":
					return false;
				default:
					System.out.println(UNRECOGNISED_ERROR);
			}
		}
	}

	public static Move hitStand() {
		System.out.println("\nHit or Stand (h/s): ");
		while (true) {
			String move = scanner.nextLine().toUpperCase(Locale.ROOT);
			switch (move) {
				case "H":
					return Move.HIT;
				case "S":
					return Move.STAND;
				default:
					System.out.println(UNRECOGNISED_ERROR);
			}
		}
	}

	public static Move standSplit() {
		System.out.println("\nStand or Split (s/sp): ");
		while (true) {
			String move = scanner.nextLine().toUpperCase(Locale.ROOT);
			switch (move) {
				case "S":
					return Move.STAND;
				case "SP":
					return Move.SPLIT;
				default:
					System.out.println(UNRECOGNISED_ERROR);
			}
		}
	}

	public static Move hitStandDouble() {
		System.out.println("\nHit, Stand or Double (h/s/d): ");
		while (true) {
			String move = scanner.nextLine().toUpperCase(Locale.ROOT);
			switch (move) {
				case "H":
					return Move.HIT;
				case "S":
					return Move.STAND;
				case "D":
					return Move.DOUBLE;
				default:
					System.out.println(UNRECOGNISED_ERROR);
			}
		}
	}

	public static Move hitStandDoubleSplit() {
		System.out.println("\nHit, Stand, Double or Split (h/s/d/sp): ");
		while (true) {
			String move = scanner.nextLine().toUpperCase(Locale.ROOT);
			switch (move) {
				case "H":
					return Move.HIT;
				case "S":
					return Move.STAND;
				case "D":
					return Move.DOUBLE;
				case "SP":
					return Move.SPLIT;
				default:
					System.out.println(UNRECOGNISED_ERROR);
			}
		}
	}
}
