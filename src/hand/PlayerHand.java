package hand;

import deck.Card;
import deck.GameDeck;
import game.Wallet;
import util.Input;
import util.Move;

public class PlayerHand extends Hand {

	private final Wallet wallet;
	private int bet;

	public PlayerHand(GameDeck deck, Wallet wallet, int bet) {
		super(deck);
		this.wallet = wallet;
		this.bet = bet;
	}

	public void add(Card card) {
		cards.add(card);
	}

	/**
	 * @return {@code true} if hand results in a split, otherwise {@code false}
	 */
	public boolean play() {
		if (cards.size() == 1) {
			System.out.println("\nAuto-hitting...");
			addRandom();
			display();

			if (cards.get(0).getValue() == 1) {
				if (cards.get(1).getValue() == 1) {
					Move move = Input.standSplit();
					return move == Move.SPLIT;
				} else {
					return false;
				}
			}
		}

		if (isBlackjack()) {
			System.out.println("Blackjack!");
			return false;
		}

		// two cards, not a blackjack

		switch (twoCardsMove()) {
			case HIT:
				addRandom();
				break;
			case STAND:
				return false;
			case DOUBLE:
				wallet.increaseReserve(bet);
				bet *= 2;
				System.out.printf("Bet is now $%s%n", bet);
				addRandom();
				break;
			case SPLIT:
				wallet.increaseReserve(bet);
				return true;
		}

		// three cards

		display();
		while (notBust()) {
			Move move = Input.hitStand();
			if (move == Move.HIT) {
				addRandom();
			} else {
				break;
			}

			display();
		}

		return false;
	}

	public int getBet() {
		return bet;
	}

	@Override
	public void display() {
		System.out.println("\nYour cards are:");
		super.display();
	}

	private Move twoCardsMove() {
		Move move;
		if (wallet.getAvailable() >= bet) {
			if (cards.get(0).getNum() == cards.get(1).getNum()) {
				move = Input.hitStandDoubleSplit();
			} else {
				move = Input.hitStandDouble();
			}
		} else {
			move = Input.hitStand();
		}

		return move;
	}
}
