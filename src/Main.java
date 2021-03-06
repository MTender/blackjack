public class Main {
	public static void main(String[] args) {
		DeckOfCards.generateDeck();
		DeckOfCards.setDeckSize(2);
		Wallet.setMoney(0);
		//introduction and rules
		System.out.println("""
				-----------------------------
				This is double deck blackjack.
				Continuous shuffler.
				Hole card.

				Blackjack pays 3 to 2.

				Insurance pays 2 to 1.
				Insurance only offered upon dealer ace.
				Insurance fixed to half the bet (rounded down).

				Dealer must hit soft 17.

				No surrender.
				You can double down after splitting.
				Double down allowed on any two cards.
				No limits on splitting.
				No hitting after splitting aces.
				No blackjacks after splitting.

				Max bet: $300
				Min bet: $1

				Start new round by pressing enter.
				Exit game by typing "exit" instead.
				-----------------------------""");
		Blackjack game = new Blackjack();
		game.begin();
	}
}
