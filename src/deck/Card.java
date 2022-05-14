package deck;

public class Card {

	private final int num;
	private final Suite suite;

	public Card(int num, Suite suite) {
		this.num = num;
		this.suite = suite;
	}

	public int getNum() {
		return num;
	}

	public int getValue() {
		return Math.min(num, 10);
	}

	@Override
	public String toString() {
		return switch (num) {
			case 1 -> String.format("Ace of %s", suite.getText());
			case 11 -> String.format("Jack of %s", suite.getText());
			case 12 -> String.format("Queen of %s", suite.getText());
			case 13 -> String.format("King of %s", suite.getText());
			default -> String.format("%s of %s", num, suite.getText());
		};
	}
}
