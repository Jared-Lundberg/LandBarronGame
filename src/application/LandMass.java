package application;

/**
 * Land Mass object
 * 
 * @author joreneklund
 * @author JaredLundberg
 * @version 5/10/19
 */
public class LandMass implements Comparable<LandMass> {

	/* Row position of the land mass */
	private int row;

	/* Column position of the land mass */
	private int col;

	/* integer representation of who owns the square */
	private int owner;

	/* Largest bid that the current owner has placed on the lot */
	private int bid;

	/* How much it costs to get to this square using the cheapest route */
	private int shortestPathTo;

	/* Whether or not this square is apart of solution for cheapest path */
	private boolean shortestPathSquare;

	/* Whether or not the landmass is any type of special square */
	private boolean specialSquare;

	/*
	 * What type of special square 0 - off limits 1 - owned by company 2 - normal
	 * square 3 - end square
	 */
	private int squareType;

	/* Shortest path previous */
	private LandMass previous;
	/*
	 * Marks if a landmass is finished
	 */
	private boolean finished;

	/**
	 * Constructor for building a landmass
	 * 
	 * @param row
	 * @param col
	 */
	public LandMass(int row, int col) {
		setRow(row);
		setCol(col);
		setOwner(0);
		setBid(0);
		setShortestPathTo(0);
		setSquareType(2);
		setSpecialSquare(false);
		setShortestPathSquare(false);
		setFinished(false);
		setPrevious(null);
	}

	/**
	 * Gets row of landmass
	 * 
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Sets row of landmass
	 * 
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Gets col of landmass
	 * 
	 * @return
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Sets col of landmass
	 * 
	 * @param col
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * Gets owner of landmass
	 * 
	 * @return
	 */
	public int getOwner() {
		return owner;
	}

	/**
	 * Sets owner of landmass
	 * 
	 * @param owner
	 */
	public void setOwner(int owner) {
		this.owner = owner;
	}

	/**
	 * Gets bid of landmass
	 * 
	 * @return
	 */
	public int getBid() {
		return bid;
	}

	/**
	 * Sets bid of a landmass
	 * 
	 * @param bid
	 */
	public void setBid(int bid) {
		this.bid = bid;
	}

	/**
	 * gets shortest path to the landmass
	 * 
	 * @return distance
	 */
	public int getShortestPathTo() {
		return shortestPathTo;
	}

	/**
	 * sets Shortest path to a landmass
	 * 
	 * @param shortestPathTo
	 */
	public void setShortestPathTo(int shortestPathTo) {
		this.shortestPathTo = shortestPathTo;
	}

	/**
	 * Gets boolean on where landmass is part of the shortest path
	 * 
	 * @return
	 */
	public boolean isShortestPathSquare() {
		return shortestPathSquare;
	}

	/**
	 * Sets landmass as shortest path
	 * 
	 * @param shortestPathSquare
	 */
	public void setShortestPathSquare(boolean shortestPathSquare) {
		this.shortestPathSquare = shortestPathSquare;
	}

	/**
	 * Gets if a landmass is a special square
	 * 
	 * @return
	 */
	public boolean isSpecialSquare() {
		return specialSquare;
	}

	/**
	 * Sets a landmass as a special square
	 * 
	 * @param specialSquare
	 */
	public void setSpecialSquare(boolean specialSquare) {
		this.specialSquare = specialSquare;
	}

	/**
	 * Gets the previous Landmass
	 * 
	 * @return
	 */
	public LandMass getPrevious() {
		return previous;
	}

	/**
	 * Sets previous of a landmass
	 * 
	 * @param previous
	 */
	public void setPrevious(LandMass previous) {
		this.previous = previous;
	}

	/**
	 * Gets a square type for a landmass
	 * 
	 * @return
	 */
	public int getSquareType() {
		return squareType;
	}

	/**
	 * Sets a square type for a landmass
	 * 
	 * @param squareType
	 */
	public void setSquareType(int squareType) {
		this.squareType = squareType;
	}

	/**
	 * To String for the landmass object
	 */
	@Override
	public String toString() {
		if (squareType == 0) {
			return "Off Limits";
		}
		if (squareType == 1) {
			return "Company Owned";
		}
		if (squareType == 2) {
			return "" + bid;
		} else {
			return "End";
		}
	}

	/**
	 * checks if a landmass is finished
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets finished for landmass
	 * 
	 * @param finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Compares landmasses shortestPathTo
	 */
	@Override
	public int compareTo(LandMass other) {
		if (this.shortestPathTo > other.shortestPathTo) {
			return 1;
		}
		if (this.shortestPathTo < other.shortestPathTo) {
			return -1;
		} else {
			if (((this.row * 4) + this.col) > ((other.row * 4) + other.col)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

}
