import java.util.ArrayList;
import java.util.Arrays;

public class DeckOfCards {
	private static String[] completeDeck;
	private static String[] oneDeck;
	private static ArrayList<String> completeDeckList;
	private static int amountOfDecks;
	private static int[] indexOfTen;


	public static void generateDeck() {
		String[] suites = new String[]{"of Hearts", "of Diamonds", "of Spades", "of Clubs"};
		String[] pictureCards = new String[]{"Jack", "Queen", "King", "Ace"};
		oneDeck = new String[52];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j <= 8; j++) oneDeck[i * 13 + j] = (j + 2) + " " + suites[i];
			for (int j = 0; j <= 3; j++) oneDeck[i * 13 + 9 + j] = pictureCards[j] + " " + suites[i];
		}
	}

	public static void setDeckSize(int amountOfDecks) {
		DeckOfCards.amountOfDecks = amountOfDecks;
		completeDeck = new String[52 * amountOfDecks];
		for (int i = 0; i < amountOfDecks; i++) {
			System.arraycopy(oneDeck, 0, completeDeck, 52 * i, 52);
		}
		completeDeckList = new ArrayList<>();
		completeDeckList.addAll(Arrays.asList(completeDeck));
		findTen();
	}

	public static void findTen() {
		indexOfTen = new int[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 8; j <= 11; j++) {
				indexOfTen[i] = 13 * i + j;
			}
		}
	}

	public static int indexOfCard(String card) {
		for (int i = 0; i < 52; i++) {
			if (oneDeck[i].equals(card)) return i;
		}
		return -1;
	}

	public static boolean hasValueOfTen(int index) {
		for (int j : indexOfTen) {
			if (index == j) return true;
		}
		return false;
	}

	public static String[] getCompleteDeck() {
		return completeDeck;
	}

	public static int[] getIndexOfTen() {
		return indexOfTen;
	}

	public static int getAmountOfDecks() {
		return amountOfDecks;
	}

	public static ArrayList<String> getCompleteDeckList() {
		return completeDeckList;
	}
}
