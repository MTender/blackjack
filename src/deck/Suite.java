package deck;

public enum Suite {
	HEARTS("Hearts"),
	DIAMONDS("Diamonds"),
	CLUBS("Clubs"),
	SPADES("Spades");

	private final String text;

	Suite(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
