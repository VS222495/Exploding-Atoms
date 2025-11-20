package com.sikora.atomsexplocik;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int ROWS = 6;
    private static final int COLS = 9;
    private static final int CELL_SIZE = 70;

    private ExplodingAtomsLogic logic;
    private StackPane[][] cellPanes;

    private Circle currentPlayerIndicator;
    private Label currentPlayerLabel;
    private Button restartButton;

    @Override
    public void start(Stage primaryStage) {

        logic = new ExplodingAtomsLogic(ROWS, COLS);
        cellPanes = new StackPane[ROWS][COLS];

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

         for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                StackPane cell = new StackPane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setStyle("-fx-border-color: grey; -fx-background-color: #eeeeee;");
                cellPanes[r][c] = cell;

                int row = r;
                int col = c;

                cell.setOnMouseClicked(e -> {
                    logic.handleClick(row, col);
                    redrawBoard();
                    updateStatus();
                });

                grid.add(cell, c, r);
            }
        }


        currentPlayerIndicator = new Circle(12, Color.RED);
        currentPlayerLabel = new Label("Hráč na tahu:");
        currentPlayerLabel.setFont(Font.font(18));

        HBox playerBox = new HBox(10, currentPlayerLabel, currentPlayerIndicator);
        playerBox.setAlignment(Pos.CENTER);


        restartButton = new Button("Hrát znovu");
        restartButton.setVisible(false);
        restartButton.setOnAction(e -> restartGame());

        VBox root = new VBox(10, playerBox, grid, restartButton);
        root.setAlignment(Pos.CENTER);

        redrawBoard();
        updateStatus();

        Scene scene = new Scene(root, COLS * CELL_SIZE, ROWS * CELL_SIZE + 100);
        primaryStage.setTitle("Exploding Atoms - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Vykreslení celé mřížky */
    private void redrawBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane cell = cellPanes[r][c];
                cell.getChildren().clear();

                int owner = logic.getOwner(r, c);
                int atoms = logic.getAtoms(r, c);

                if (atoms == 0) continue;

                Color color = getColor(owner);

                int count = Math.min(atoms, 3);

                for (int i = 0; i < count; i++) {
                    Circle circle = new Circle(10, color);
                    circle.setTranslateX((i - 1) * 12);
                    cell.getChildren().add(circle);
                }

                if (atoms > 3) {
                    Label label = new Label("×" + atoms);
                    label.setFont(Font.font(22));
                    label.setTextFill(color);
                    cell.getChildren().add(label);
                }
            }
        }
    }

    /** Aktualizace indikátoru hráče a zobrazení vítěze */
    private void updateStatus() {
        int winner = logic.getWinner();
        if (winner != 0) {
            currentPlayerLabel.setText("Vyhrál hráč: " + (winner == 1 ? "Červený" : "Modrý"));
            currentPlayerIndicator.setFill((winner == 1) ? Color.RED : Color.DODGERBLUE);
            restartButton.setVisible(true);
        } else {
            int player = logic.getCurrentPlayer();
            currentPlayerLabel.setText("Hráč na tahu:");
            currentPlayerIndicator.setFill((player == 1) ? Color.RED : Color.DODGERBLUE);
            restartButton.setVisible(false);
        }
    }

    /** Restart hry */
    private void restartGame() {
        logic = new ExplodingAtomsLogic(ROWS, COLS); // reset logiky
        redrawBoard();
        updateStatus();
    }

    private Color getColor(int owner) {
        return (owner == 1) ? Color.RED : Color.DODGERBLUE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}



