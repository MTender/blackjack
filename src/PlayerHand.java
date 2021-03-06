import java.util.ArrayList;

public class PlayerHand {
	private final ArrayList<String> playerCards;

	public PlayerHand(ArrayList<String> playerCards) {
		this.playerCards = playerCards;
	}

	public ArrayList<String> getPlayerHand() {
		return playerCards;
	}

	public void addCard(String card) {
		playerCards.add(card);
	}
}
