import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
	private static ArrayList<String> dealerCards;
	private static ArrayList<String> roundDeck;
	private static boolean insurance;
	private static ArrayList<String> conclusions;
	private static int dealerFinalSum;
	private static int totalBet;
	private static int moneyAtRoundStart;

	public static int dealer() {
		Random random = new Random();
		//dealer actions
		int dealerSum, dealerAces, dealerFinalSum;

		while (true) {
			int[] dealerSums = Game.sumOfCards(dealerCards);
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
			dealerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
			roundDeck.remove(dealerCards.get(dealerCards.size() - 1));
		}

		return dealerFinalSum;
	}


	public static void begin() {
		Scanner input = new Scanner(System.in);
		Random random = new Random();
		//round start
		while (true) {
			//remaining money display
			System.out.println("\nRemaining cash: $" + Wallet.getMoney() + "\nPress enter.");

			//exit conditions
			if (Wallet.getMoney() == 0) {
				System.out.println("Out of money. Exiting game...");
				break;
			}
			String quit = input.nextLine();
			if (quit.equals("exit")) {
				System.out.println("Exiting game...");
				break;
			}

			//bet placement
			int bet;
			System.out.println("Enter bet size: ");
			while (true) {
				try {
					bet = input.nextInt();
					if (bet >= 5 && bet <= 300 && bet <= Wallet.getMoney()) {
						break;
					} else {
						System.out.println("ERROR: Enter a whole number from 1 to " + (Wallet.getMoney() > 300 ? "300" : Wallet.getMoney()));
					}
				} catch (InputMismatchException ime) {
					System.out.println("ERROR: Enter a whole number!");
					input.nextLine();
				}
			}
			input.nextLine();

			//creating deck for this round
			roundDeck = new ArrayList<>(DeckOfCards.getCompleteDeckList());

			//first cards
			ArrayList<String> playerStartingCards = new ArrayList<>(); //!!!check whether double ace split works
			dealerCards = new ArrayList<>();
			for (int i = 0; i < 2; i++) {
				playerStartingCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(playerStartingCards.get(i));
				dealerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
				roundDeck.remove(dealerCards.get(i));
			}

			//display starting cards
			System.out.println("Your cards: " + playerStartingCards.get(0) + ", " + playerStartingCards.get(1));
			System.out.println("Dealer shows: " + dealerCards.get(0));

			//insurance
			insurance = false;
			while (Wallet.getMoney() >= Math.floor(bet * 1.5) && (DeckOfCards.indexOfCard(dealerCards.get(0)) + 1) % 13 == 0) {
				System.out.println("Would you like insurance? (y/n)");
				String insuranceChoice = input.nextLine();
				if (insuranceChoice.equals("y")) {
					insurance = true;
					break;
				} else if (insuranceChoice.equals("n")) {
					break;
				} else System.out.println("ERROR: Input not recognised.");
			}

			//round start default global variables
			totalBet = bet;
			conclusions = new ArrayList<>();
			// displayConclusions = true;
			moneyAtRoundStart = Wallet.getMoney();
			PlayerHand hand = new PlayerHand(playerStartingCards, bet, false);

			//blackjack check
			if (Game.blackjackResult(playerStartingCards, dealerCards, bet)) {
				dealerFinalSum = dealer(); //playing dealer hand
				hand.gameplay();
			}
		}
	}

	public static boolean isInsurance() {
		return insurance;
	}

	public static int getTotalBet() {
		return totalBet;
	}

	public static int getMoneyAtRoundStart() {
		return moneyAtRoundStart;
	}

	public static int getDealerFinalSum() {
		return dealerFinalSum;
	}

	public static ArrayList<String> getConclusions() {
		return conclusions;
	}

	public static void increaseTotalBet(int sum) {
		totalBet += sum;
	}

	public static void addConclusion(String conclusion) {
		conclusions.add(conclusion);
	}

	public static String dealerOpenCard() {
		return dealerCards.get(0);
	}

	public static void roundDeckRemove(String card) {
		roundDeck.remove(card);
	}

	public static String randomCard(Random random) {
		return roundDeck.get(random.nextInt(roundDeck.size()));
	}
}