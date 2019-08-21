package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Main extends Application implements EventHandler<ActionEvent> {
	/**
	 * Grid pane that holds all the land masses
	 */
	private GridPane buttonGrid;
	/**
	 * Root border pane
	 */
	private BorderPane root;
	/**
	 * ComboBox to change size of board
	 */
	private ComboBox<Integer> cb;
	/**
	 * Button that resets the game
	 */
	private Button resetButton;
	/**
	 * Gives info about the current game status
	 */
	private Label progress;
	/**
	 * GridPane which holds the label progress
	 */
	private GridPane info;
	/**
	 * Button to pass
	 */
	private Button passButton;
	/**
	 * Land Barron game object
	 */
	private LandBarronGame game;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			Scene scene = new Scene(root, 800, 1000);
			game = new LandBarronGame(4);
			// ButtonGrid
			buttonGrid = new GridPane();
			setUpButtons(4);
			root.setTop(buttonGrid);

			// ComboBox
			cb = new ComboBox<Integer>();
			ObservableList<Integer> options = FXCollections.observableArrayList(4, 5, 6, 7);
			cb = new ComboBox<Integer>(options);
			cb.getSelectionModel().select(0);
			cb.setPrefSize(99, 20);
			cb.setOnAction((e) -> {
				game = new LandBarronGame(cb.getValue());
				setUpButtons(cb.getValue());
				progress = new Label(game.getPlayerTurn() + "\n" + game.getPlayer(1) + "\n" + game.getPlayer(2));
				info.getChildren().clear();
				info.add(progress, 0, 0);
				root.getChildren().remove(passButton);
				passButton = new Button("PASS");
				passButton.setStyle("-fx-background-color: LightBlue");
				passButton.setPrefSize(100, 20);
				passButton.setOnAction(this);
				root.setCenter(passButton);
			});
			root.setLeft(cb);

			// Reset Button
			resetButton = new Button("CLEAR");
			resetButton.setStyle("-fx-background-color: red");
			resetButton.setPrefSize(100, 20);
			resetButton.setOnAction(this);
			root.setRight(resetButton);

			// Pass Button
			passButton = new Button("PASS");
			passButton.setStyle("-fx-background-color: lightblue");
			passButton.setPrefSize(100, 20);
			passButton.setOnAction(this);
			root.setCenter(passButton);

			// Labels
			info = new GridPane();
			progress = new Label(game.getPlayerTurn() + "\n" + game.getPlayer(1) + "\n" + game.getPlayer(2));
			info.add(progress, 0, 0);

			root.setBottom(info);
			primaryStage.setTitle("Land Barron");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up buttons initially and makes a grid of buttons agree with the back end
	 * 
	 * @param size
	 */
	private void setUpButtons(int size) {
		buttonGrid.getChildren().clear();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				SpecialButton b = new SpecialButton(game.getLandMass(i, j) + "", i, j);
				b.setStyle("-fx-background-color: LightGray");
				if (game.getLandMass(i, j).getSquareType() == 0)
					b.setStyle("-fx-background-color: White");
				if (game.getLandMass(i, j).getSquareType() == 1)
					b.setStyle("-fx-background-color: LightGreen");
				if (game.getLandMass(i, j).getSquareType() == 2) {
					if (game.getLandMass(i, j).getOwner() == 1)
						b.setStyle("-fx-background-color: Red");
					if (game.getLandMass(i, j).getOwner() == 2)
						b.setStyle("-fx-background-color: LightBlue");
				}
				if (game.getLandMass(i, j).getSquareType() == 3)
					b.setStyle("-fx-background-color: Green");
				if (size == 4)
					b.setPrefSize(200, 200);
				if (size == 5)
					b.setPrefSize(160, 160);
				if (size == 6)
					b.setPrefSize(133, 133);
				if (size == 7)
					b.setPrefSize(114, 114);
				b.setOnAction(this);
				buttonGrid.add(b, i, j);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Handles when an action is made in the game
	 */
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == resetButton) {
			game = new LandBarronGame(cb.getValue());
			setUpButtons(cb.getValue());
			progress = new Label(game.getPlayerTurn() + "\n" + game.getPlayer(1) + "\n" + game.getPlayer(2));
			info.getChildren().clear();
			info.add(progress, 0, 0);
			root.getChildren().remove(passButton);
			passButton = new Button("PASS");
			passButton.setStyle("-fx-background-color: LightBlue");
			passButton.setPrefSize(100, 20);
			passButton.setOnAction(this);
			root.setCenter(passButton);

		}
		if (event.getSource() == passButton) {
			game.pass();
			if (game.isGameOver()) {
				progress = new Label(game.calculateWinner() + "");
				info.getChildren().clear();
				info.add(progress, 0, 0);
				deactivateButtons(cb.getValue());
			} else {
				progress = new Label(game.getPlayerTurn() + "\n" + game.getPlayer(1) + "\n" + game.getPlayer(2)
						+ "\nPASSING ENDS THE GAME");
				info.getChildren().clear();
				info.add(progress, 0, 0);
			}
		} else {
			try {
				for (int i = 0; i < cb.getValue(); i++) {
					for (int j = 0; j < cb.getValue(); j++) {
						if (event.getSource() == buttonGrid.getChildren().get(cb.getValue() * i + j)) {
							game.bid(i, j);

						}
					}
				}
				setUpButtons(cb.getValue());
				progress = new Label(game.getPlayerTurn() + "\n" + game.getPlayer(1) + "\n" + game.getPlayer(2));
				info.getChildren().clear();
				info.add(progress, 0, 0);
			}catch(Exception e) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Invalid Move");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}

	}

	/**
	 * Deactivates the buttons when the game is over
	 * 
	 * @param size
	 */
	private void deactivateButtons(int size) {
		buttonGrid.getChildren().clear();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				SpecialButton b = new SpecialButton(game.getLandMass(i, j) + "", i, j);
				b.setStyle("-fx-background-color: LightGray");
				if (game.getLandMass(i, j).getSquareType() == 0)
					b.setStyle("-fx-background-color: White");
				if (game.getLandMass(i, j).getSquareType() == 1)
					b.setStyle("-fx-background-color: LightGreen");
				if (game.getLandMass(i, j).getSquareType() == 2) {
					if (game.getLandMass(i, j).getOwner() == 1)
						b.setStyle("-fx-background-color: Red");
					if (game.getLandMass(i, j).getOwner() == 2)
						b.setStyle("-fx-background-color: LightBlue");
				}
				if (game.getLandMass(i, j).getSquareType() == 1 && game.getLandMass(i, j).isShortestPathSquare())
					b.setStyle("-fx-background-color: DarkGreen");
				if (game.getLandMass(i, j).getSquareType() == 2 && game.getLandMass(i, j).isShortestPathSquare()) {
					if (game.getLandMass(i, j).getOwner() == 1)
						b.setStyle("-fx-background-color: DarkRed");
					else if (game.getLandMass(i, j).getOwner() == 2)
						b.setStyle("-fx-background-color: DarkBlue");
					else
						b.setStyle("-fx-background-color: DarkGray");
				}
				if (game.getLandMass(i, j).getSquareType() == 3)
					b.setStyle("-fx-background-color: Green");
				if (size == 4)
					b.setPrefSize(200, 200);
				if (size == 5)
					b.setPrefSize(160, 160);
				if (size == 6)
					b.setPrefSize(133, 133);
				if (size == 7)
					b.setPrefSize(114, 114);
				buttonGrid.add(b, i, j);
			}
		}
		root.getChildren().remove(passButton);
		passButton = new Button("PASS");
		passButton.setStyle("-fx-background-color: LightBlue");
		passButton.setPrefSize(100, 20);
		root.setCenter(passButton);
	}
}
