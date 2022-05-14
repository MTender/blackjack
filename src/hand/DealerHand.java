package hand;

import deck.GameDeck;

public class DealerHand extends Hand {

	public DealerHand(GameDeck deck) {
		super(deck);
	}

	public void autoplay() {
		int value = value();
		while (value < 17 && value != -1) {
			addRandom();
			value = value();
		}
	}

	@Override
	public void display() {
		System.out.println("\nDealer shows:");
		super.display();
	}

	public void displayFirst() {
		System.out.println("\nDealer shows:");
		System.out.println(cards.get(0));
	}
}
