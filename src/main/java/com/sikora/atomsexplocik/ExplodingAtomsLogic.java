package com.sikora.atomsexplocik;

public class ExplodingAtomsLogic {

    private final int rows;
    private final int cols;

    private Cell[][] board;

    private int currentPlayer = 1;
    private int totalMoves = 0;

    public ExplodingAtomsLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Cell();
            }
        }
    }

    public int getOwner(int r, int c) {
        return board[r][c].getOwner();
    }

    public int getAtoms(int r, int c) {
        return board[r][c].getAtoms();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void handleClick(int r, int c) {
        if (!board[r][c].canBeClaimedBy(currentPlayer)) return;
        if (getWinner() != 0) return;

        addAtom(r, c, currentPlayer);
        totalMoves++;
        currentPlayer = 3 - currentPlayer; // přepnutí hráče 1<->2
    }

    private void addAtom(int r, int c, int player) {
        board[r][c].addAtom(player);

        if (board[r][c].isOverloaded(getMaxAtoms(r, c))) {
            explode(r, c, player);
        }
    }

    private int getMaxAtoms(int r, int c) {
        int edges = 0;
        if (r == 0) edges++;
        if (r == rows - 1) edges++;
        if (c == 0) edges++;
        if (c == cols - 1) edges++;
        return 4 - edges;
    }

    private void explode(int r, int c, int player) {
        board[r][c].reset();

        sendTo(r - 1, c, player);
        sendTo(r + 1, c, player);
        sendTo(r, c - 1, player);
        sendTo(r, c + 1, player);
    }

    private void sendTo(int r, int c, int player) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return;

        board[r][c].addAtom(player);

        if (board[r][c].isOverloaded(getMaxAtoms(r, c))) {
            explode(r, c, player);
        }
    }

    public int getWinner() {
        if (totalMoves < 2) return 0;

        boolean p1 = false, p2 = false;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int owner = board[r][c].getOwner();
                if (owner == 1) p1 = true;
                if (owner == 2) p2 = true;
            }
        }
        if (p1 && !p2) return 1;
        if (!p1 && p2) return 2;
        return 0;
    }

    // --- Cell class ---
    private static class Cell {
        private int atoms = 0;
        private int owner = 0;

        public int getAtoms() {
            return atoms;
        }

        public int getOwner() {
            return owner;
        }

        public boolean canBeClaimedBy(int player) {
            return owner == 0 || owner == player;
        }

        public void addAtom(int player) {
            atoms++;
            owner = player;
        }

        public boolean isOverloaded(int maxAtoms) {
            return atoms >= maxAtoms;
        }

        public void reset() {
            atoms = 0;
            owner = 0;
        }
    }
}
