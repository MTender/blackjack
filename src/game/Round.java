package game;

import deck.Card;
import deck.GameDeck;
import hand.DealerHand;
import hand.PlayerHand;
import util.Input;
import util.Outcome;
import util.Result;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Round {

	private final GameDeck deck;
	private final Wallet wallet;
	private final int startingBet;

	public Round(GameDeck deck, Wallet wallet, int startingBet) {
		this.deck = deck;
		this.wallet = wallet;
		this.startingBet = startingBet;
	}

	public List<Result> start() {
		deck.startRound();

		PlayerHand playerHand = new PlayerHand(deck, wallet, startingBet);
		DealerHand dealerHand = new DealerHand(deck);
		playerHand.addRandom();
		dealerHand.addRandom();
		playerHand.addRandom();
		dealerHand.addRandom();

		Result result = handleInitialBlackjacks(playerHand, dealerHand);
		if (result != null) return List.of(result);

		wallet.increaseReserve(startingBet);

		Deque<PlayerHand> hands = new ArrayDeque<>();
		List<PlayerHand> playedHands = new ArrayList<>();

		hands.push(playerHand);
		while (!hands.isEmpty()) {
			PlayerHand hand = hands.pop();

			hand.display();
			dealerHand.displayFirst();

			boolean split = hand.play();

			if (!split) {
				playedHands.add(hand);
			} else {
				List<Card> cards = hand.getCards();

				PlayerHand hand1 = new PlayerHand(deck, wallet, startingBet);
				hand1.add(cards.get(0));

				PlayerHand hand2 = new PlayerHand(deck, wallet, startingBet);
				hand2.add(cards.get(1));

				hands.push(hand2);
				hands.push(hand1);
			}
		}
		wallet.deductAndClearReserve();

		boolean playDealer = false;
		for (PlayerHand hand : playedHands) {
			if (hand.notBust()) {
				playDealer = true;
				break;
			}
		}

		if (playDealer) {
			dealerHand.autoplay();
			dealerHand.display();
		}

		return getResults(playedHands, dealerHand);
	}

	private Result handleInitialBlackjacks(PlayerHand playerHand, DealerHand dealerHand) {
		boolean playerBlackjack = playerHand.isBlackjack();
		boolean dealerBlackjack = dealerHand.isBlackjack();

		if (!playerBlackjack && dealerHand.getCards().get(0).getValue() == 1 && wallet.getAvailable() >= startingBet / 2) {
			playerHand.display();
			dealerHand.displayFirst();

			boolean insurance = Input.insurance();
			if (insurance) {
				wallet.increaseReserve(startingBet / 2);

				if (dealerBlackjack) {
					wallet.withdraw(startingBet);
					wallet.clearReserve();

					playerHand.display();
					dealerHand.display();

					return new Result(Outcome.INSURANCE_PAYS, playerHand);
				} else {
					System.out.println("\nDealer does not have blackjack");

					wallet.deductAndClearReserve();
				}
			}
		}

		if (playerBlackjack || dealerBlackjack) {
			wallet.withdraw(startingBet);

			playerHand.display();
			dealerHand.display();

			Outcome outcome;
			if (playerBlackjack && dealerBlackjack)
				outcome = Outcome.TIE;
			else if (playerBlackjack)
				outcome = Outcome.BLACKJACK;
			else
				outcome = Outcome.DEALER_BLACKJACK;

			return new Result(outcome, playerHand);
		}
		return null;
	}

	private List<Result> getResults(List<PlayerHand> playerHands, DealerHand dealerHand) {
		int dealerScore = dealerHand.value();
		List<Result> results = new ArrayList<>();

		for (PlayerHand hand : playerHands) {
			int playerScore = hand.value();

			Outcome outcome;
			if (playerScore == -1) {
				outcome = Outcome.BUST;
			} else if (hand.isBlackjack()) {
				outcome = Outcome.BLACKJACK;
			} else if (dealerScore == -1) {
				outcome = Outcome.DEALER_BUST;
			} else if (playerScore > dealerScore) {
				outcome = Outcome.VICTORY;
			} else if (playerScore < dealerScore) {
				outcome = Outcome.LOSS;
			} else {
				outcome = Outcome.TIE;
			}

			results.add(new Result(outcome, hand));
		}

		return results;
	}
}
