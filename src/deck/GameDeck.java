package deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameDeck {

	private static final Random random = new Random();
	private final int amountOfDecks;
	private List<Card> roundDeck;

	public GameDeck(int amountOfDecks) {
		this.amountOfDecks = amountOfDecks;
	}

	public void startRound() {
		this.roundDeck = new ArrayList<>();
		Suite[] suites = Suite.values();
		for (int count = 0; count < amountOfDecks; count++) {
			for (Suite suite : suites) {
				for (int j = 1; j <= 13; j++) {
					this.roundDeck.add(new Card(j, suite));
				}
			}
		}
	}

	public Card pullRandom() {
		int index = random.nextInt(roundDeck.size());
		return roundDeck.remove(index);
	}
}
