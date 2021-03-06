import java.util.ArrayList;

public class CurrentHand {
	private final ArrayList<String> playerHand;

	public CurrentHand(ArrayList<String> playerHand) {
		this.playerHand = playerHand;
	}

	public ArrayList<String> getPlayerHand() {
		return playerHand;
	}

	public void addCard(String card) {
		playerHand.add(card);
	}

	public boolean blackjackCheck() {
		if (playerHand.size() == 2) {
			for (int i = 0; i < 4; i++) {
				if (playerHand.contains(DeckOfCards.getCompleteDeck()[13 * i + 12])) {
					for (int j = 0; j < 16; j++) {
						if (playerHand.contains(DeckOfCards.getCompleteDeck()[DeckOfCards.getIndexOfTen()[j]])) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
