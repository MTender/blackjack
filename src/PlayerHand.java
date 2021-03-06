import java.util.ArrayList;

public class PlayerHand {
	private final ArrayList<String> playerCards;
	private int previousBet;

	public PlayerHand(ArrayList<String> playerCards, int previousBet) {
		this.playerCards = playerCards;
		this.previousBet = previousBet;
	}

	public void gameplay() {
		//hand start defaults
		int playerFinalSum = 0, bet = previousBet;
		boolean playerBust = false, hasBeenPaid = false, calcPlayerScore = false, breakNow = false;
		boolean splitAllowed = false, doubleAllowed = false, hitAllowed = true;

		//starting options display
		if (hasSplit && (DeckOfCards.indexOfCard(playerCards.get(0)) + 1) % 13 == 0) {
			if (cashAtRoundStart >= totalBet + bet && (DeckOfCards.indexOfCard(playerCards.get(1)) + 1) % 13 == 0) {
				hitAllowed = false;
				splitAllowed = true;
				System.out.println("Dealer shows: " + dealerCards.get(0));
				System.out.println("Stand or Split (s/sp): ");
			} else {
				calcPlayerScore = true;
				breakNow = true;
			}
		} else if (cashAtRoundStart >= totalBet + bet && playerCards.size() == 2) {
			int index1 = DeckOfCards.indexOfCard(playerCards.get(0)), index2 = DeckOfCards.indexOfCard(playerCards.get(1));
			if ((index1 - index2) % 13 == 0 || (DeckOfCards.hasValueOfTen(index1) && DeckOfCards.hasValueOfTen(index2))) {
				System.out.println("Hit, Stand, Double or Split (h/s/d/sp): ");
				splitAllowed = true;
			} else System.out.println("Hit, Stand or Double (h/s/d): ");
			doubleAllowed = true;
		} else System.out.println("Hit or Stand (h/s): ");

		//subsequent choices and calculation
		while (true) {
			//player score calculation
			if (calcPlayerScore) {
				int[] playerSums = Game.sumOfCards(playerCards);
				int playerSum = playerSums[0];
				int playerAces = playerSums[1];

				//choosing calculation method
				if (playerAces == 0 && playerSum != 21) {
					playerFinalSum = playerSum;
				} else if (playerSum + 10 + playerAces < 21) {
					playerFinalSum = playerSum + 10 + playerAces;
				} else if (playerSum + 10 + playerAces == 21 || playerSum + playerAces == 21) {
					System.out.println("21");
					playerFinalSum = 21;
					breakNow = true;
				} else playerFinalSum = playerSum + playerAces;

				//player bust control
				if (playerSum + playerAces > 21) {
					if (hasSplit) System.out.println("Player bust.");
					playerBust = true;
					breakNow = true;
				}
				//end hand
				if (breakNow) {
					//display dealer cards
					if (displayConclusions && !hasSplit) {
						System.out.print("Dealer shows: ");
						Game.printMany(dealerCards);
						displayConclusions = false;
					}
					break;
				}
				//next choice
				System.out.println("Dealer shows: " + dealerCards.get(0));
				System.out.println("Hit or Stand (h/s): ");
			}

			//player actions
			String playChoice = input.nextLine();
			if (playChoice.equals("h") && hitAllowed) {
				//action hit
				calcPlayerScore = true;
				playerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(playerCards.get(playerCards.size() - 1));
				System.out.print("Your cards: ");
				Game.printMany(playerCards);
				splitAllowed = false;
			} else if (playChoice.equals("s")) {
				//action stand
				calcPlayerScore = true;
				breakNow = true;
			} else if (playChoice.equals("d") && doubleAllowed) {
				//action double down
				calcPlayerScore = true;
				bet *= 2;
				totalBet += bet;
				System.out.println("Bet size now: $" + bet);
				playerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(playerCards.get(2));
				System.out.println("Your cards: " + playerCards.get(0) + ", " + playerCards.get(1) + ", " + playerCards.get(2));
				breakNow = true;
			} else if (playChoice.equals("sp") && splitAllowed) {
				//action split
				hasSplit = true;
				totalBet += bet;
				ArrayList<String> playerCards2 = new ArrayList<>();
				playerCards2.add(playerCards.get(1));
				playerCards.remove(1);
				System.out.println("Hitting both hands.");
				playerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(playerCards.get(1));
				playerCards2.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(playerCards2.get(1));
				System.out.println("Playing first hand: " + playerCards.get(0) + ", " + playerCards.get(1));
				hand(playerCards, bet);
				System.out.println("Playing second hand: " + playerCards2.get(0) + ", " + playerCards2.get(1));
				hand(playerCards2, bet);
				hasBeenPaid = true;
				//display dealer cards and conclusions
				if (displayConclusions) {
					System.out.print("Dealer shows: ");
					Game.printMany(dealerCards);
					Game.printMany(conclusions);
					displayConclusions = false;
				}
				break;
			} else {
				//unrecognised input
				calcPlayerScore = false;
				System.out.println("ERROR: Input not recognised.");
			}
		}
		//no splits or done with splits
		if (!hasBeenPaid) {
			//payout
			Wallet.addMoney(Payout.normal(playerFinalSum, bet));
			//display conclusion
			if (!hasSplit) System.out.println(conclusions.get(0));
		}
	}

	public PlayerHand(ArrayList<String> playerCards) {
		this.playerCards = playerCards;
	}

	public ArrayList<String> getPlayerHand() {
		return playerCards;
	}

	public void addCard(String card) {
		playerCards.add(card);
	}
}
