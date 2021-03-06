public class Payout {
	public static int normal(int playerFinalSum, int bet) {
		int dealerFinalSum = Blackjack.getDealerFinalSum();
		if (playerFinalSum > 21) {
			Blackjack.addConclusion("Player bust");
			return -bet;
		} else {
			if (Blackjack.getConclusions().contains("Dealer bust")) { // checking player bust outside of method, fix in future
				return bet;
			} else {
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
		System.out.println("Push");
		return -insuranceLoss(bet);
	}

	public static int playerBlackjack(int bet) { // bet is an argument for every method, fix in future
		System.out.println("Blackjack!");
		return (int) Math.floor(bet * 1.5) - insuranceLoss(bet);
	}

	public static int dealerBlackjack(int bet) {
		System.out.print("Dealer blackjack!");
		if (Blackjack.isInsurance()) {
			System.out.println(" Insurance pays");
			return (int) (2 * Math.floor(bet * 0.5)) - bet;
		}
		System.out.println();
		return -bet;
	}

	public static int insuranceLoss(int bet) {
		if (Blackjack.isInsurance()) { // thought of error in v1.0: insurance loss does not include scenario where player and dealer have blackjacks
			return (int) Math.floor(bet * 0.5);
		}
		return 0;
	}
}
