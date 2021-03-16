import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PlayerHand {
	private final ArrayList<String> playerCards;
	private final int previousBet;
	private boolean hasSplit;

	public PlayerHand(ArrayList<String> playerCards, int previousBet, boolean hasSplit) {
		this.playerCards = playerCards;
		this.previousBet = previousBet;
		this.hasSplit = hasSplit;
	}

	public void gameplay() {
		//hand start defaults
		int playerFinalSum = 0, bet = previousBet;
		boolean hasBeenPaid = false, mustCalculateScore = false, breakNow = false;
		boolean splitAllowed = false, doubleAllowed = false, hitAllowed = true;
		Scanner input = new Scanner(System.in);
		Random random = new Random();

		//starting options display
		if (hasSplit && (DeckOfCards.indexOfCard(playerCards.get(0)) + 1) % 13 == 0) {
			if (Blackjack.getMoneyAtRoundStart() >= Blackjack.getTotalBet() + bet && (DeckOfCards.indexOfCard(playerCards.get(1)) + 1) % 13 == 0) {
				hitAllowed = false;
				splitAllowed = true;
				System.out.println("Dealer shows: " + Blackjack.dealerOpenCard());
				System.out.println("Stand or Split (s/sp): ");
			} else {
				mustCalculateScore = true;
				breakNow = true;
			}
		} else if (Blackjack.getMoneyAtRoundStart() >= Blackjack.getTotalBet() + bet && playerCards.size() == 2) {
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
			if (mustCalculateScore) {
				playerFinalSum = calculatePlayerScore(Game.sumOfCards(playerCards));
				// end hand
				if (playerFinalSum >= 21 || breakNow) break;
				//next choice
				System.out.println("Dealer shows: " + Blackjack.dealerOpenCard());
				System.out.println("Hit or Stand (h/s): ");
			}

			//player actions
			String playChoice = input.nextLine();
			if (playChoice.equals("h") && hitAllowed) {
				//action hit
				mustCalculateScore = true;
				playerCards.add(Blackjack.randomCard(random));
				Blackjack.roundDeckRemove(playerCards.get(playerCards.size() - 1));
				System.out.print("Your cards: ");
				Game.printList(playerCards);
				splitAllowed = false;
			} else if (playChoice.equals("s")) {
				//action stand
				mustCalculateScore = true;
				breakNow = true;
			} else if (playChoice.equals("d") && doubleAllowed) {
				//action double down
				mustCalculateScore = true;
				bet *= 2;
				Blackjack.increaseTotalBet(bet);
				System.out.println("Bet size now: $" + bet);
				playerCards.add(Blackjack.randomCard(random));
				Blackjack.roundDeckRemove(playerCards.get(2));
				System.out.println("Your cards: " + playerCards.get(0) + ", " + playerCards.get(1) + ", " + playerCards.get(2));
				breakNow = true;
			} else if (playChoice.equals("sp") && splitAllowed) {
				//action split
				hasSplit = true;
				Blackjack.increaseTotalBet(bet);
				ArrayList<String> playerCards1 = new ArrayList<>();
				ArrayList<String> playerCards2 = new ArrayList<>();
				playerCards1.add(playerCards.get(0));
				playerCards2.add(playerCards.get(1));
				playerCards.clear();

				System.out.println("Hitting both hands.");
				playerCards1.add(DeckOfCards.getCompleteDeckList().get(25));
				Blackjack.roundDeckRemove(playerCards1.get(1));
				playerCards2.add(DeckOfCards.getCompleteDeckList().get(25));
				Blackjack.roundDeckRemove(playerCards2.get(1));

				System.out.println("Playing first hand: " + playerCards1.get(0) + ", " + playerCards1.get(1));
				PlayerHand hand1 = new PlayerHand(playerCards1, bet, true);
				hand1.gameplay();
				System.out.println("Playing second hand: " + playerCards2.get(0) + ", " + playerCards2.get(1));
				PlayerHand hand2 = new PlayerHand(playerCards2, bet, true);
				hand2.gameplay();

				hasBeenPaid = true;
				break;
			} else {
				//unrecognised input
				mustCalculateScore = false;
				System.out.println("ERROR: Input not recognised.");
			}
		}
		//no splits or done with splits, payout
		if (!hasBeenPaid) Blackjack.addMoney(Payout.normal(playerFinalSum, bet));
	}

	private static int calculatePlayerScore(int[] playerSums) {
		int playerSum = playerSums[0];
		int playerAces = playerSums[1];

		//choosing calculation method
		int playerFinalSum;
		if (playerAces == 0 && playerSum != 21) {
			playerFinalSum = playerSum;
		} else if (playerSum + 10 + playerAces < 21) {
			playerFinalSum = playerSum + 10 + playerAces;
		} else if (playerSum + 10 + playerAces == 21 || playerSum + playerAces == 21) {
			playerFinalSum = 21;
		} else playerFinalSum = playerSum + playerAces;

		return playerFinalSum;
	}
}
