package game;

public class Main {

	public static void main(String[] args) {
		System.out.println("""
				-----------------------------
				This is double deck blackjack.
				Continuous shuffler.
				Hole card.

				Blackjack pays 3 to 2.

				Insurance pays 2 to 1.
				Insurance only offered upon dealer ace.
				Insurance fixed to half the bet (rounded down).

				Dealer stands on soft 17.

				No surrender.
				You can double down after splitting.
				Double down allowed on any two cards.
				No limits on splitting.
				No hitting after splitting aces.
				Blackjack possible after splitting.

				Min bet: $5
				No max bet.
				-----------------------------""");

		// TODO insurance

		Settings settings = new Settings();
		settings.setAmountOfDecks(2);

		Wallet wallet = new Wallet();
		wallet.deposit(100);

		Game game = new Game(settings, wallet);
		game.start();
	}
}
