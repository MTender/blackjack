public class Payout {
	public static int normal(int playerFinalSum, int bet) {
		if (playerFinalSum > 21) {
			Blackjack.addConclusion("Player bust");
			return -bet;
		} else {
			if (Blackjack.getConclusions().contains("Dealer bust")) {
				return bet;
			} else {
				int dealerFinalSum = Blackjack.getDealerFinalSum();
				if (dealerFinalSum > 21) {
					Blackjack.addConclusion("Dealer bust");
					return bet;
				} else if (playerFinalSum > dealerFinalSum) {
					Blackjack.addConclusion("Player wins");
					return bet;
				} else if (playerFinalSum < dealerFinalSum) {
					Blackjack.addConclusion("Dealer wins");
					return -bet;
				} else {
					Blackjack.addConclusion("Push");
					return 0;
				}
			}
		}
	}

	public static int bothBlackjack(int bet) {
		Blackjack.addConclusion("Push");
		return -insuranceLoss(bet);
	}

	public static int playerBlackjack(int bet) { // bet is an argument for every method, fix in future
		Blackjack.addConclusion("Blackjack!");
		return (int) Math.floor(bet * 1.5) - insuranceLoss(bet);
	}

	public static int dealerBlackjack(int bet) {
		if (Blackjack.isInsurance()) {
			Blackjack.addConclusion("Dealer blackjack! Insurance pays");
			return (int) (2 * Math.floor(bet * 0.5)) - bet;
		}
		Blackjack.addConclusion("Dealer blackjack!");
		System.out.println();
		return -bet;
	}

	public static int insuranceLoss(int bet) {
		if (Blackjack.isInsurance()) {
			return (int) Math.floor(bet * 0.5);
		}
		return 0;
	}
}
