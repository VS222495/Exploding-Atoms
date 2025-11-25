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


    private StackPane[][] cells;


    private Circle playerIndicator;
    private Label playerLabel;
    private Button restartButton;

    @Override
    public void start(Stage stage) {


        logic = new ExplodingAtomsLogic(ROWS, COLS);
        cells = new StackPane[ROWS][COLS];


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

                cells[row][col] = cell;

                int r = row;
                int c = col;


                cell.setOnMouseClicked(e -> {
                    logic.handleClick(r, c);
                    redrawBoard();
                    updateStatus();
                });

                grid.add(cell, col, row);
            }
        }


        playerIndicator = new Circle(12, Color.RED);
        playerLabel = new Label("Hráč na tahu:");
        playerLabel.setFont(Font.font(18));

        HBox playerBox = new HBox(10, playerLabel, playerIndicator);
        playerBox.setAlignment(Pos.CENTER);


        restartButton = new Button("Hrát znovu");
        restartButton.setVisible(false);
        restartButton.setOnAction(e -> restartGame());


        VBox root = new VBox(10, playerBox, grid, restartButton);
        root.setAlignment(Pos.CENTER);

        redrawBoard();
        updateStatus();


        Scene scene = new Scene(root, COLS * CELL_SIZE, ROWS * CELL_SIZE + 120);
        stage.setTitle("Exploding Atoms - JavaFX");
        stage.setScene(scene);
        stage.show();
    }


    private void redrawBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                StackPane cell = cells[r][c];
                cell.getChildren().clear();

                int owner = logic.getOwner(r, c);
                int count = logic.getAtoms(r, c);

                if (count == 0) continue;

                Color color = (owner == 1) ? Color.RED : Color.BLUE;


                int visible = Math.min(count, 3);

                for (int i = 0; i < visible; i++) {
                    Circle circle = new Circle(10, color);
                    circle.setTranslateX((i - 1) * 12);
                    cell.getChildren().add(circle);
                }


                if (count > 3) {
                    Label label = new Label("×" + count);
                    label.setFont(Font.font(22));
                    label.setTextFill(color);
                    cell.getChildren().add(label);
                }
            }
        }
    }


    private void updateStatus() {
        int winner = logic.getWinner();

        if (winner != 0) {
            playerLabel.setText("Vyhrál hráč: " + (winner == 1 ? "Červený" : "Modrý"));
            playerIndicator.setFill(winner == 1 ? Color.RED : Color.BLUE);
            restartButton.setVisible(true);
        } else {
            int player = logic.getCurrentPlayer();
            playerLabel.setText("Hráč na tahu:");
            playerIndicator.setFill(player == 1 ? Color.RED : Color.BLUE);
            restartButton.setVisible(false);
        }
    }


    private void restartGame() {
        logic = new ExplodingAtomsLogic(ROWS, COLS);
        redrawBoard();
        updateStatus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



