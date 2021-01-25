import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
	//creating global variables and objects
	boolean playerBlackjack, dealerBlackjack, insurance, displayConclusions = true, hasSplit;
	int money=1000, totalBet, dealerFinalSum;
	ArrayList<String> roundDeck, dealerCards, completeDeck = new ArrayList<>(), conclusions = new ArrayList<>();
	ArrayList<Integer> indexOfTen = new ArrayList<>();
	Scanner input = new Scanner(System.in);
	Random random = new Random();


	public boolean blackjackCheck(ArrayList<String> startingCards){
		for(int i = 0; i<4; i++){
			if(startingCards.contains(completeDeck.get(13 * i + 12))){
				for(int j = 0; j<16; j++){
					if(startingCards.contains(completeDeck.get(indexOfTen.get(j)))){
						return true;
					}
				}
			}
		}
		return false;
	}


	public int[] sumOfCards(ArrayList<String> cards){
		//counting aces and other cards
		int sum=0, aces=0;
		for(String card : cards){
			int index = completeDeck.indexOf(card)%13;
			if(index<=8){
				sum += (index+2);
			}else if(index<=11){
				sum += 10;
			}else aces += 1;
		}
		return new int[]{sum, aces};
	}


	public int dealer(){
		//dealer actions
		int dealerSum, dealerAces, dealerFinalSum;
		
		while(true){
			int[] dealerSums = sumOfCards(dealerCards);
			dealerSum = dealerSums[0];
			dealerAces = dealerSums[1];

			//choosing calculation method
			if(dealerAces == 0){
				if(dealerSum >= 17){
					dealerFinalSum = dealerSum;
					break;
				}
			}else if(dealerSum + 10 + dealerAces >= 18 && dealerSum + 10 + dealerAces <=21){
				dealerFinalSum = dealerSum + 10 + dealerAces;
				break;
			}else if(dealerSum + dealerAces >= 17){
				dealerFinalSum = dealerSum + dealerAces;
				break;
			}

			//dealer next card
			dealerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
			roundDeck.remove(dealerCards.get(dealerCards.size()-1));
		}
		
		return dealerFinalSum;
	}

	
	public void begin(){
		//deck creation
		String[] suites = new String[]{"of Hearts", "of Diamonds", "of Spades", "of Clubs"};
		String[] pictures = new String[]{"Jack", "Queen", "King", "Ace"};
		for(int i=0; i<4; i++){
			for(int j=0; j<=8; j++) completeDeck.add((j+2)+" "+suites[i]);
			for(int j=0; j<=3; j++) completeDeck.add(pictures[j]+" "+suites[i]);
		}
		ArrayList<String> tempDeck = new ArrayList<>(completeDeck); //IDE doesn't like adding ArrayList directly to itself
		completeDeck.addAll(tempDeck); //two decks
		
		//arraylist of indexes of cards with value 10
		for(int j = 0; j<4; j++) for(int k = 8; k<=11; k++) indexOfTen.add(j*13+k);
		
		//introduction and rules
		System.out.println("-----------------------------\nThis is double deck blackjack. \nContinuous shuffler.\nHole card."+
				                   "\n\nBlackjack pays 3 to 2.\n\nInsurance pays 2 to 1.\nInsurance only offered upon dealer ace."+
				                   "\nInsurance fixed to half the bet (rounded down).\n\nDealer must hit soft 17.\n\nNo surrender."+
				                   "\nYou can double down after splitting.\nDouble down allowed on any two cards."+
				                   "\nNo limits on splitting.\nNo hitting after splitting aces.\nNo blackjacks after splitting.\n\nMax bet: $300\nMin bet: $1"+
				                   "\n\nStart new round by pressing enter.\nExit game by typing \"exit\" instead.\n-----------------------------");
		
		//round start
		while(true){
			//remaining money display
			System.out.println("\nRemaining cash: $" +money+ "\nPress enter.");
			
			//exit conditions
			if(money==0){
				System.out.println("Out of money. Exiting game...");
				break;
			}
			String quit = input.nextLine();
			if(quit.equals("exit")){
				System.out.println("Exiting game...");
				break;
			}
			
			//bet placement
			int bet;
			System.out.println("Enter bet size: ");
			while(true){
				try{
					bet = input.nextInt();
					if(bet >= 5 && bet<=300 && bet<=money){
						break;
					}else{
						System.out.println("ERROR: Enter a whole number from 1 to " + (money>300?"300": money));
					}
				}catch(InputMismatchException ime){
					System.out.println("ERROR: Enter a whole number!");
					input.nextLine();
				}
			}
			input.nextLine();
			
			//creating deck for this round
			roundDeck = new ArrayList<>(completeDeck);

			//first cards
			ArrayList<String> playerStartingCards = new ArrayList<>(); //!!!check whether double ace split works
			dealerCards = new ArrayList<>();
			playerStartingCards.add(roundDeck.get(random.nextInt(104)));
			roundDeck.remove(playerStartingCards.get(0));
			dealerCards.add(roundDeck.get(random.nextInt(103)));
			roundDeck.remove(dealerCards.get(0));
			playerStartingCards.add(roundDeck.get(random.nextInt(102)));
			roundDeck.remove(playerStartingCards.get(1));
			dealerCards.add(roundDeck.get(random.nextInt(101)));
			roundDeck.remove(dealerCards.get(1));
			
			//blackjack check
			playerBlackjack = blackjackCheck(playerStartingCards);
			dealerBlackjack = blackjackCheck(dealerCards);

			//display starting cards
			System.out.println("Your cards: " + playerStartingCards.get(0) + ", " + playerStartingCards.get(1));
			if(!dealerBlackjack){
				System.out.println("Dealer shows: " + dealerCards.get(0));
				dealerFinalSum = dealer(); //playing dealer hand
			}else System.out.println("Dealer shows: " + dealerCards.get(0) +", "+ dealerCards.get(1));
			
			//insurance
			insurance = false;
			while(money >= Math.floor(bet*1.5) && (completeDeck.indexOf(dealerCards.get(0))+1)%13==0){
				System.out.println("Would you like insurance? (y/n)");
				String insuranceChoice = input.nextLine();
				if(insuranceChoice.equals("y")){
					insurance = true;
					break;
				}else if(insuranceChoice.equals("n")){
					break;
				}else System.out.println("ERROR: Input not recognised.");
			}
			
			//round start default global variables
			totalBet = bet;
			hasSplit = false;
			conclusions.clear();
			displayConclusions = true;
			hand(playerStartingCards, bet);
		}
	}
	
	
	public void hand(ArrayList<String> playerCards, int previousBet){
		//hand start defaults
		int playerFinalSum = 0, bet = previousBet;
		boolean playerBust = false, hasNotPaid = true, calcPlayerScore = false, breakNow = false;
		boolean splitAllowed = false, doubleAllowed = false, hitAllowed = true;
		
		//game
		if(!(playerBlackjack || dealerBlackjack)){
			//starting options display
			if(hasSplit && (completeDeck.indexOf(playerCards.get(0))+1)%13 == 0){
				if(money >= totalBet + bet && (completeDeck.indexOf(playerCards.get(1))+1)%13 == 0){
					hitAllowed = false;
					splitAllowed = true;
					System.out.println("Stand or Split (s/sp): ");
				}else breakNow = true;
			}else if(money >= totalBet + bet && playerCards.size() == 2){
				int index1 = completeDeck.indexOf(playerCards.get(0)), index2 = completeDeck.indexOf(playerCards.get(1));
				if((index1 - index2) % 13 == 0 || (indexOfTen.contains(index1) && indexOfTen.contains(index2))){
					System.out.println("Hit, Stand, Double or Split (h/s/d/sp): ");
					splitAllowed = true;
				}else System.out.println("Hit, Stand or Double (h/s/d): ");
				doubleAllowed = true;
			}else System.out.println("Hit or Stand (h/s): ");
			
			//subsequent choices and calculation
			while(true){
				//player score calculation
				if(calcPlayerScore){
					int[] playerSums = sumOfCards(playerCards);
					int playerSum = playerSums[0];
					int playerAces = playerSums[1];

					//choosing calculation method
					if(playerAces == 0 && playerSum != 21){
						playerFinalSum = playerSum;
					}else if(playerSum + 10 + playerAces<21){
						playerFinalSum = playerSum + 10 + playerAces;
					}else if(playerSum + 10 + playerAces == 21 || playerSum + playerAces == 21){
						System.out.println("21");
						playerFinalSum = 21;
						breakNow = true;
					}else playerFinalSum = playerSum + playerAces;
					
					//player bust control
					if(playerSum + playerAces>21){
						if (hasSplit) System.out.println("Player bust.");
						playerBust = true;
						breakNow = true;
					}
					//end hand
					if(breakNow){
						//display dealer cards
						if(displayConclusions && !hasSplit){
							System.out.print("Dealer shows: ");
							for(String s : dealerCards) System.out.print(s+"  ");
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
				if(playChoice.equals("h") && hitAllowed){
					//action hit
					calcPlayerScore = true;
					playerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
					roundDeck.remove(playerCards.get(playerCards.size() - 1));
					System.out.print("Your cards: " + playerCards.get(0) + ", " + playerCards.get(1));
					for(int i = 2; i<playerCards.size(); i++) System.out.print(", " + playerCards.get(i));
					System.out.println();
					splitAllowed = false;
				}else if(playChoice.equals("s")){
					//action stand
					calcPlayerScore = true;
					breakNow = true;
				}else if(playChoice.equals("d")  && doubleAllowed){
					//action double down
					calcPlayerScore = true;
					bet *= 2;
					totalBet += bet;
					System.out.println("Bet size now: $" + bet);
					playerCards.add(roundDeck.get(random.nextInt(roundDeck.size())));
					roundDeck.remove(playerCards.get(2));
					System.out.println("Your cards: " + playerCards.get(0) + ", " + playerCards.get(1) + ", " + playerCards.get(2));
					breakNow = true;
				}else if(playChoice.equals("sp") && splitAllowed){
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
					System.out.println("Playing first hand: " + playerCards.get(0) +", "+ playerCards.get(1));
					System.out.println("Dealer shows: "+dealerCards.get(0));
					hand(playerCards, bet);
					System.out.println("Playing second hand: " + playerCards2.get(0) +", "+ playerCards2.get(1));
					System.out.println("Dealer shows: "+dealerCards.get(0));
					hand(playerCards2, bet);
					hasNotPaid = false;
					//display dealer cards and conclusions
					if(displayConclusions){
						System.out.print("Dealer shows: ");
						for(String s : dealerCards) System.out.print(s+"  ");
						System.out.println();
						for(String s : conclusions) System.out.print(s+"  ");
						displayConclusions = false;
					}
					break;
				}else{
					//unrecognised input
					calcPlayerScore = false;
					System.out.println("ERROR: Input not recognised.");
				}
			}
			//no splits or done with splits
			if(hasNotPaid){
				//payout
				if(!playerBust){
					conclusions.add(payout(dealerFinalSum, playerFinalSum, bet));
				}else{
					money -= bet;
					conclusions.add("Player bust.");
				}
				//display conclusion
				if(!hasSplit) System.out.println("\n" + conclusions.get(0));
			}
		}else System.out.println(payout(0, 0, bet)); //payout if dealer or player has blackjack
	}

	
	public String payout(int dealerFinalSum, int playerFinalSum, int bet){
		//insurance loss check
		if(insurance && !dealerBlackjack){
			money -= Math.floor(bet*0.5);
		}
		//finding winner and paying
		if(dealerFinalSum>21){
			money +=bet;
			return "Dealer bust";
		}else if(playerFinalSum>dealerFinalSum){
			money +=bet;
			return "Player wins";
		}else if(playerFinalSum<dealerFinalSum || (dealerBlackjack && !playerBlackjack)){
			if(insurance && dealerBlackjack){
				money -=bet;
				money +=(2*Math.floor(bet*0.5));
				return "Dealer blackjack. Insurance pays";
			}else{
				money -= bet;
				return "Dealer wins";
			}
		}else if(playerBlackjack && !dealerBlackjack){
			money +=Math.floor(bet*1.5);
			return "Blackjack!";
		}else return "Push";
	}

	
	public static void main(String[] args){
		Blackjack game = new Blackjack();
		game.begin();
	}
}