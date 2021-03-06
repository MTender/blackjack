import java.util.ArrayList;
import java.util.Random;

public class Game {
	public static int dealer(ArrayList<String> dealerCards) {
		Random random = new Random();
		//dealer actions
		int dealerSum, dealerAces, dealerFinalSum;

		while (true) {
			int[] dealerSums = sumOfCards(dealerCards);
			dealerSum = dealerSums[0];
			dealerAces = dealerSums[1];

			//choosing calculation method
			if (dealerAces == 0) {
				if (dealerSum >= 17) {
					dealerFinalSum = dealerSum;
					break;
				}
			} else if (dealerSum + 10 + dealerAces >= 18 && dealerSum + 10 + dealerAces <= 21) {
				dealerFinalSum = dealerSum + 10 + dealerAces;
				break;
			} else if (dealerSum + dealerAces >= 17) {
				dealerFinalSum = dealerSum + dealerAces;
				break;
			}

			//dealer next card
			dealerCards.add(Blackjack.randomCard(random));
			Blackjack.roundDeckRemove(dealerCards.get(dealerCards.size() - 1));
		}

		return dealerFinalSum;
	}

	private static boolean blackjackCheck(ArrayList<String> startingCards) {
		for (int i = 0; i < 4; i++) {
			if (startingCards.contains(DeckOfCards.getCompleteDeck()[13 * i + 12])) {
				for (int j = 0; j < 16; j++) {
					if (startingCards.contains(DeckOfCards.getCompleteDeck()[DeckOfCards.getIndexOfTen()[j]])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean blackjackResult(ArrayList<String> playerCards, ArrayList<String> dealerCards, int bet) {
		boolean player = blackjackCheck(playerCards);
		boolean dealer = blackjackCheck(dealerCards);
		if (player && dealer) {
			Blackjack.addMoney(Payout.bothBlackjack(bet));
			return false;
		} else if (player) {
			Blackjack.addMoney(Payout.playerBlackjack(bet));
			return false;
		} else if (dealer) {
			Blackjack.addMoney(Payout.dealerBlackjack(bet));
			return false;
		}
		return true;
	}

	public static int[] sumOfCards(ArrayList<String> cards) {
		//counting aces and other cards
		int sum = 0, aces = 0;
		for (String card : cards) {
			int index = DeckOfCards.indexOfCard(card) % 13;
			if (index <= 8) {
				sum += (index + 2);
			} else if (index <= 11) {
				sum += 10;
			} else aces += 1;
		}
		return new int[]{sum, aces};
	}

	public static void printList(ArrayList<String> allText) {
		int lastIndex = allText.size() - 1;
		for (int i = 0; i < lastIndex; i++) System.out.print(allText.get(i) + ", ");
		System.out.println(allText.get(lastIndex));
	}
}
