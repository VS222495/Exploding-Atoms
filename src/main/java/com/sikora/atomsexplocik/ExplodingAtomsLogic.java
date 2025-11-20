package com.sikora.atomsexplocik;

public class ExplodingAtomsLogic {

    private final int rows;
    private final int cols;

    private int[][] atoms;
    private int[][] owner;

    private int currentPlayer = 1;
    private int totalMoves = 0;

    public ExplodingAtomsLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        atoms = new int[rows][cols];
        owner = new int[rows][cols];
    }


    public int getOwner(int r, int c) {
        return owner[r][c];
    }


    public int getAtoms(int r, int c) {
        return atoms[r][c];
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }


    public void handleClick(int r, int c) {

        if (owner[r][c] != 0 && owner[r][c] != currentPlayer)
            return;


        if (getWinner() != 0) return;

        addAtom(r, c, currentPlayer);

        totalMoves++;


        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }


    private void addAtom(int r, int c, int player) {
        atoms[r][c]++;
        owner[r][c] = player;

        int maxAtoms = getMaxAtoms(r, c);

        if (atoms[r][c] >= maxAtoms) {
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
        atoms[r][c] = 0;
        owner[r][c] = 0;

        sendTo(r - 1, c, player);
        sendTo(r + 1, c, player);
        sendTo(r, c - 1, player);
        sendTo(r, c + 1, player);
    }


    private void sendTo(int r, int c, int player) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return;

        atoms[r][c]++;
        owner[r][c] = player;

        int maxAtoms = getMaxAtoms(r, c);
        if (atoms[r][c] >= maxAtoms) {
            explode(r, c, player);
        }
    }


    public int getWinner() {
        if (totalMoves < 2) return 0;

        boolean p1 = false, p2 = false;
        for (int r = 0; r < atoms.length; r++) {
            for (int c = 0; c < atoms[0].length; c++) {
                if (owner[r][c] == 1) p1 = true;
                if (owner[r][c] == 2) p2 = true;
            }
        }
        if (p1 && !p2) return 1;
        if (!p1 && p2) return 2;
        return 0;
    }
}

