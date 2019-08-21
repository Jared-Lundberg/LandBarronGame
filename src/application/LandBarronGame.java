package application;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class LandBarronGame {
	/**
	 * Player 1 object
	 */
	private PlayerObject p1;
	/**
	 * Player 2 object
	 */
	private PlayerObject p2;
	/**
	 * ArrayList that holds all the Land Masses
	 */
	private ArrayList<ArrayList<LandMass>> massHolder;
	/**
	 * Keeps track of whose turn it is
	 */
	private int turnCounter;
	/**
	 * Keeps track of if p1 has passed
	 */
	private boolean p1Pass;
	/**
	 * Keeps track of if p2 has passed
	 */
	private boolean p2Pass;
	/**
	 * Contains the size of the board
	 */
	private int size;
	/**
	 * Contains the revenue of player 1
	 */
	private int p1Revenue;
	/**
	 * Contains the revenue
	 */
	private int p2Revenue;

	/**
	 * Keeps the priority for dijkstras
	 */
	private PriorityQueue<LandMass> queue;

	/**
	 * Constructor for Landbarron game
	 * 
	 * @param size of the board
	 */
	public LandBarronGame(int size) {
		this.size = size;
		massHolder = new ArrayList<ArrayList<LandMass>>();
		setUpLandMasses(size);
		turnCounter = 0;
		p1 = new PlayerObject(1, size);
		p2 = new PlayerObject(2, size);
		p1Revenue = 0;
		p2Revenue = 0;

	}

	/**
	 * Gets a speific player object
	 * 
	 * @param n which player
	 * @return Player object
	 */
	public PlayerObject getPlayer(int n) {
		if (n == 1)
			return p1;
		else
			return p2;
	}

	/**
	 * Gets which players turn it is
	 * 
	 * @return String of the player
	 */
	public String getPlayerTurn() {
		if (turnCounter % 2 == 0) {
			return "Player 1's turn";
		}
		return "Player 2's turn";
	}

	/**
	 * Gets a specified land mass
	 * 
	 * @param row of landmass
	 * @param col of landmass
	 * @return LandMass object
	 */
	public LandMass getLandMass(int row, int col) {
		return massHolder.get(row).get(col);
	}

	/**
	 * Creates all the landMasses
	 * 
	 * @param size of the board
	 */
	private void setUpLandMasses(int size) {

		for (int i = 0; i < size; i++) {
			ArrayList<LandMass> al = new ArrayList<LandMass>();
			for (int j = 0; j < size; j++) {
				LandMass lm = new LandMass(i, j);
				if ((i == 0 && j == 0) || (i == size - 1 && j == size - 1)) {
					lm.setSpecialSquare(true);
					lm.setSquareType(3);
				}
				al.add(lm);
			}
			massHolder.add(al);
		}
		randomSetUp(size);
		while (!isLegalRando()) {
			randomSetUp(size);
		}

	}

	/**
	 * Set N landmasses as either company owned or
	 * 
	 * @param size
	 */
	private void randomSetUp(int size) {
		Random rand = new Random();
		int counter = 0;
		while (counter < size) {
			int i = rand.nextInt(size);
			int j = rand.nextInt(size);
			if (!massHolder.get(i).get(j).isSpecialSquare()) {
				int choice = rand.nextInt(2);
				massHolder.get(i).get(j).setSpecialSquare(true);
				massHolder.get(i).get(j).setSquareType(choice);
				counter++;
			}
		}

	}

	/**
	 * Checks to see if the current board is legal
	 * 
	 * @return boolean. true if legal
	 */
	private boolean isLegalRando() {
		boolean isLegal = false;
		dijkstra();
		if (massHolder.get(size - 1).get(size - 1).getPrevious() != null)
			isLegal = true;
		else
			isLegal = false;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				massHolder.get(i).get(j).setPrevious(null);
				massHolder.get(i).get(j).setFinished(false);
				massHolder.get(i).get(j).setShortestPathTo(0);
			}
		}
		return isLegal;

	}

	/**
	 * Manages everything when a bid is made
	 * 
	 * @param row
	 * @param col
	 */
	public void bid(int row, int col) throws IllegalArgumentException{
		//Checks if you are trying to bid on a special square
		if (massHolder.get(row).get(col).isSpecialSquare())
			throw new IllegalArgumentException("You can't bid on that square");
		//Checks to see player 1 still has money
		if (turnCounter % 2 == 0 && p1.getCurrentWallet() == 0)
			throw new IllegalArgumentException("You don't have any money left, Player 1. \nPass to continue the game");
		//Checks to see if player 2 still has money
		if (turnCounter % 2 == 1 && p2.getCurrentWallet() == 0)
			throw new IllegalArgumentException("You don't have any money left, Player 2. \nPass to continue the game");
		//Checks to see if player 1 can outbid a square that player 2 has the highest bid on
		if (turnCounter % 2 == 0 && p1.getCurrentWallet() < massHolder.get(row).get(col).getBid() + 1 
				&& massHolder.get(row).get(col).getOwner() == 2)
			throw new IllegalArgumentException("You don't have enough money to outbid Player 2. \nChoose a different square");
		//Checks to see if player 2 can out bid a square that player 1 has the highest bid on
		if (turnCounter % 2 == 1 && p2.getCurrentWallet() < massHolder.get(row).get(col).getBid() + 1
				&& massHolder.get(row).get(col).getOwner() == 1)
			throw new IllegalArgumentException("You don't have enough money to outbid Player 1. \nChoose a different square");
		//Player 1 making a bid
		if (turnCounter % 2 == 0) {
			//If player 1 owns the current square that player 1 is trying to bid on
			if (massHolder.get(row).get(col).getOwner() == 1) {
				p1.setCurrentWallet(p1.getCurrentWallet() - 1);
				massHolder.get(row).get(col).setBid(massHolder.get(row).get(col).getBid() + 1);
				//If player 2 owns the square that player 1 is trying to bid on
			} else {
				p2.setCurrentWallet(p2.getCurrentWallet() + massHolder.get(row).get(col).getBid());
				massHolder.get(row).get(col).setBid(massHolder.get(row).get(col).getBid() + 1);
				p1.setCurrentWallet(p1.getCurrentWallet() - massHolder.get(row).get(col).getBid());
			}
			massHolder.get(row).get(col).setOwner(1);
		}
		//Player 2 making a bid
		if (turnCounter % 2 == 1) {
			//If player 2 owns the square that player 2 is trying to bid on
			if (massHolder.get(row).get(col).getOwner() == 2) {
				p2.setCurrentWallet(p2.getCurrentWallet() - 1);
				massHolder.get(row).get(col).setBid(massHolder.get(row).get(col).getBid() + 1);
				//If player 1 owns the square that player 2 is trying to bid on
			} else {
				p1.setCurrentWallet(p1.getCurrentWallet() + massHolder.get(row).get(col).getBid());
				massHolder.get(row).get(col).setBid(massHolder.get(row).get(col).getBid() + 1);
				p2.setCurrentWallet(p2.getCurrentWallet() - massHolder.get(row).get(col).getBid());
			}
			massHolder.get(row).get(col).setOwner(2);
		}
		p1Pass = false;
		p2Pass = false;
		turnCounter++;
	}

	/**
	 * Keeps track when the pass button is clicked
	 */
	public void pass() {
		if (turnCounter % 2 == 0)
			p1Pass = true;
		if (turnCounter % 2 == 1)
			p2Pass = true;
		turnCounter++;
	}

	/**
	 * Checks to see if the game is over
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		if (p1Pass && p2Pass)
			return true;
		return false;
	}

	/**
	 * Calculates the winner of the game
	 * @return String telling who the winner is
	 */
	public String calculateWinner() {
		dijkstra();
		LandMass iterator = massHolder.get(size - 1).get(size - 1);
		while (iterator.getPrevious() != null) {
			if (iterator.getPrevious().getOwner() == 1) {
				p1Revenue = p1Revenue + iterator.getPrevious().getBid() * 10;
				int row = iterator.getRow();
				int col = iterator.getCol();
				massHolder.get(row).get(col).setShortestPathSquare(true);
			} else if (iterator.getPrevious().getOwner() == 2) {
				p2Revenue = p2Revenue + iterator.getPrevious().getBid() * 10;
				int row = iterator.getRow();
				int col = iterator.getCol();
				massHolder.get(row).get(col).setShortestPathSquare(true);
			} else {
				int row = iterator.getRow();
				int col = iterator.getCol();
				massHolder.get(row).get(col).setShortestPathSquare(true);
			}
			iterator = iterator.getPrevious();
		}
		int startMoney = size * size * 2;
		int p1Spent = startMoney - p1.getCurrentWallet();
		int p2Spent = startMoney - p2.getCurrentWallet();
		int p1Profit = p1Revenue - p1Spent;
		int p2Profit = p2Revenue - p2Spent;
		if (p1Profit > p2Profit)
			return "Player 1 wins \nPlayer 1 spent: $" + p1Spent + " Player 2 spent: $" + p2Spent + "\n"
					+ "Player 1 was paid: $" + p1Revenue + " Player 2 was paid: $" + p2Revenue + "\n"
					+ "Player 1 profit: $" + p1Profit + " Player 2 profit: $" + p2Profit;
		if (p2Profit > p1Profit)
			return "Player 2 wins \nPlayer 1 spent: $" + p1Spent + " Player 2 spent: $" + p2Spent + "\n"
					+ "Player 1 was paid: $" + p1Revenue + " Player 2 was paid: $" + p2Revenue + "\n"
					+ "Player 1 profit: $" + p1Profit + " Player 2 profit: $" + p2Profit;
		return "It is a tie" + "\nPlayer 1 spent: $" + p1Spent + " Player 2 spent: $" + p2Spent + "\n"
				+ "Player 1 was paid: $" + p1Revenue + " Player 2 was paid: $" + p2Revenue + "\n" + "Player 1 profit: $"
				+ p1Profit + " Player 2 profit: $" + p2Profit;

	}

	/**
	 * Gets player 1 revenue
	 * @return
	 */
	public int getP1Revenue() {
		return p1Revenue;
	}

	/**
	 * Gets player 2 revenue
	 * @return
	 */
	public int getP2Revenue() {
		return p2Revenue;
	}

	/**
	 * Runs dijkstras algorithm to find the shortest path from one endpoint to
	 * another
	 */
	public void dijkstra() {
		queue = new PriorityQueue<LandMass>();
		queue.add(massHolder.get(0).get(0));
		while (queue.size() != 0) {
			int col = queue.peek().getCol();
			int row = queue.peek().getRow();
			dijkstraQueue(row - 1, col, row, col);
			dijkstraQueue(row + 1, col, row, col);
			dijkstraQueue(row, col - 1, row, col);
			dijkstraQueue(row, col + 1, row, col);
			massHolder.get(row).get(col).setFinished(true);
			queue.remove(massHolder.get(row).get(col));
		}
	}

	/**
	 * Manages the priority queue in Dijkstras
	 * 
	 * @param newRow
	 * @param newCol
	 * @param row
	 * @param col
	 */
	private void dijkstraQueue(int newRow, int newCol, int row, int col) {
		// Checks up 1 spot and for End&OffLimit Squares
		if (newRow >= 0 && newCol >= 0 && newRow < size && newCol < size
				&& massHolder.get(newRow).get(newCol).getSquareType() != 0
				&& !massHolder.get(newRow).get(newCol).isFinished()) {
			// Checks for null previous spots
			if (massHolder.get(newRow).get(newCol).getPrevious() == null) {
				// Sets shortest path
				massHolder.get(newRow).get(newCol).setShortestPathTo(
						queue.peek().getShortestPathTo() + massHolder.get(newRow).get(newCol).getBid());
				// Sets previous
				massHolder.get(newRow).get(newCol).setPrevious(massHolder.get(row).get(col));
				queue.add(massHolder.get(newRow).get(newCol));
			} else {
				if (queue.peek().getShortestPathTo() + massHolder.get(newRow).get(newCol).getBid() < massHolder
						.get(newRow).get(newCol).getShortestPathTo()) {
					queue.remove(massHolder.get(newRow).get(newCol));
					// Sets shortest path
					massHolder.get(newRow).get(newCol).setShortestPathTo(
							queue.peek().getShortestPathTo() + massHolder.get(newRow).get(newCol).getBid());
					// Sets previous
					massHolder.get(newRow).get(newCol).setPrevious(massHolder.get(row).get(col));
					queue.add(massHolder.get(newRow).get(newCol));
				}
			}
		}
	}

}
