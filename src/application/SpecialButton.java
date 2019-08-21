package application;

import javafx.scene.control.Button;

/**
 * Special button that saves row and column
 * 
 * @author joreneklund
 * @author Jared Lundberg
 * @version 5/10/19
 */
public class SpecialButton extends Button {
	/* Row position of the land mass */
	private int row;
	/* Column position of the land mass */
	private int col;

	/**
	 * Special Button contsructor
	 * 
	 * @param label
	 * @param row
	 * @param col
	 */
	public SpecialButton(String label, int row, int col) {
		super(label);
		setRow(row);
		setCol(col);
	}

	/**
	 * Gets row of button
	 * 
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Sets row of button
	 * 
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Sets col of button
	 * 
	 * @return
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Sets col of button
	 * 
	 * @param col
	 */
	public void setCol(int col) {
		this.col = col;
	}
}
