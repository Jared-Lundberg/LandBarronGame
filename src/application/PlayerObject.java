package application;

/**
 * PlayerObject
 * 
 * @author joreneklund
 * @author JaredLundberg
 * @version 5/10/19
 */
public class PlayerObject {
	/* Identifies player 1 or player 2 */
	private int playerNum;
	/* How much money the player has left */
	private int currentWallet;

	/**
	 * Constructor for player object
	 * 
	 * @param playerNum
	 * @param n
	 */
	public PlayerObject(int playerNum, int n) {
		setPlayerNum(playerNum);
		setCurrentWallet(n * n * 2);
	}

	/**
	 * Gets the player number
	 * 
	 * @return
	 */
	public int getPlayerNum() {
		return playerNum;
	}

	/**
	 * Sets player number
	 * 
	 * @param playerNum
	 */
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	/**
	 * Gets current wallet of player
	 * 
	 * @return
	 */
	public int getCurrentWallet() {
		return currentWallet;
	}

	/**
	 * Sets current wallet of player
	 * 
	 * @param currentWallet
	 */
	public void setCurrentWallet(int currentWallet) {
		this.currentWallet = currentWallet;
	}

	/**
	 * To String for Player
	 */
	public String toString() {
		return "Player " + playerNum + " has $" + currentWallet + " remaining.";
	}

}
