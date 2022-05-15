package util;

import hand.PlayerHand;

public class Result {

	private final Outcome outcome;
	private final PlayerHand hand;

	public Result(Outcome outcome, PlayerHand hand) {
		this.outcome = outcome;
		this.hand = hand;
	}

	public Outcome getOutcome() {
		return outcome;
	}

	public int getDepositAmount() {
		return switch (outcome) {
			case BLACKJACK -> (int) (hand.getBet() * 2.5);
			case VICTORY, DEALER_BUST -> hand.getBet() * 2;
			case LOSS, BUST, DEALER_BLACKJACK -> 0;
			case TIE, INSURANCE_PAYS -> hand.getBet();
		};
	}
}
