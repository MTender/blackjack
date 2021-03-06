public class Wallet {
	private static int money;

	public static void addMoney(int amount) {
		money += amount;
	}

	public static int getMoney() {
		return money;
	}

	public static void setMoney(int money) {
		Wallet.money = money;
	}
}
