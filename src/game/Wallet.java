package game;

public class Wallet {

	private int money;
	private int reserved;

	public Wallet() {
		this.money = 0;
	}

	public void deposit(int amount) {
		money += amount;
	}

	public void withdraw(int amount) {
		money -= amount;
	}

	public void increaseReserve(int amount) {
		reserved += amount;
	}

	public void deductAndClearReserve() {
		money -= reserved;
		reserved = 0;
	}

	public void clearReserve() {
		reserved = 0;
	}

	public int getAvailable() {
		return money - reserved;
	}

	public int getMoney() {
		return money;
	}
}
