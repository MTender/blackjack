package hand;

import deck.Card;
import deck.GameDeck;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	protected final GameDeck deck;
	protected final List<Card> cards;

	protected Hand(GameDeck deck) {
		this.deck = deck;
		this.cards = new ArrayList<>();
	}

	public void addRandom() {
		cards.add(deck.pullRandom());
	}

	public int value() {
		int totalValue = 0;
		boolean hasAce = false;

		for (Card card : cards) {
			int value = card.getValue();
			if (value == 1) {
				hasAce = true;
			}
			totalValue += value;
		}

		if (totalValue > 21) {
			return -1;
		}
		if (!hasAce || totalValue + 10 > 21) {
			return totalValue;
		}
		return totalValue + 10;
	}

	public boolean notBust() {
		return value() != -1;
	}

	public boolean isBlackjack() {
		if (cards.size() != 2) return false;
		return value() == 21;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void display() {
		for (Card card : cards) {
			System.out.println(card);
		}
	}
}
