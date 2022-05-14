package game;

import deck.GameDeck;
import util.Input;
import util.Result;

import java.util.List;

public class Game {

	private final Wallet wallet;
	private final GameDeck deck;

	public Game(Settings settings, Wallet wallet) {
		this.wallet = wallet;
		this.deck = new GameDeck(settings.getAmountOfDecks());
	}

	public void start() {
		while (true) {
			int money = wallet.getMoney();

			System.out.printf("%nRemaining cash: $%s%n", money);

			boolean exit = Input.exit();
			if (exit) {
				System.out.println("Exiting game...");
				break;
			}

			int bet = Input.betSize(wallet.getMoney());
			Round round = new Round(deck, wallet, bet);
			List<Result> results = round.start();

			System.out.println("\nRound results:");
			for (Result result : results) {
				System.out.println(result.getOutcome());
				wallet.deposit(result.getDepositAmount());
			}
		}
	}
}
