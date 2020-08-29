package com.example.tictactoe;

import java.util.Random;

public class TicTacToeGame {
    // Characters used to represent the human, computer, and open spots
    static final String GAME_STATUS = "game_status";
    static final String WIN_COUNT = "win_count";
    static final String ARE_YOU_FIRST = "are_you_first";
    static final String LEVEL = "LEVEL";

    static final int LEVEL_1 = 1;
    static final int LEVEL_2 = 2;
    static final int LEVEL_3 = 3;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;
    private char mBoard[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private Random mRand;
    private int mHumanWinCount = 0;
    private int mAIWinCount = 0;
    private int mTieCount = 0;
    // first Player
    private boolean mAreYouFirst = true;
    private int mLevel = LEVEL_1;

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
        char turn = HUMAN_PLAYER; // Human starts first
        int win = 0; // Set to 1, 2, or 3 when game is over
    }

    /**
     * Clear the board of all X's and O's.
     */
    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = OPEN_SPOT;
        }
    }

    /**
     * Set the given player at the given location on the game board *
     */
    public void setMove(char player, int location) {
        mBoard[location] = player;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     *
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won, * or 3 if O won.
     */
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i + 1] == HUMAN_PLAYER &&
                    mBoard[i + 2] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i + 1] == COMPUTER_PLAYER &&
                    mBoard[i + 2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i + 3] == HUMAN_PLAYER &&
                    mBoard[i + 6] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i + 3] == COMPUTER_PLAYER &&
                    mBoard[i + 6] == COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public int getComputerMove() {
        switch (mLevel) {
            case LEVEL_1:
            default:
                return getLevel1ComputerMove();
            case LEVEL_2:
                return getLevel2ComputerMove();
            case LEVEL_3:
                return getLevel3ComputerMove();
        }
    }

    private int getNextMove() {
        // First see if there's a move O can
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                } else mBoard[i] = curr;
            }
        }
        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i]; // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                } else mBoard[i] = curr;
            }
        }
        return -1;
    }
    public int getLevel1ComputerMove() {
        int move;
        // Generate random move
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        return move;
    }

    public int getLevel2ComputerMove() {
        int move;
        int i = getNextMove();
        if (i != -1) return i;
        // Generate random move
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        return move;
    }

    public int getLevel3ComputerMove() {
        int move;
        move = getNextMove();
        if (move == -1) {
            move = UnBeatenAI.getUnBeatenStep(mBoard);
        }
        return move;
    }

    public char[] getCurrentStatus() {
        return mBoard;
    }

    public int getHumanWinCount() {
        return mHumanWinCount;
    }

    public int getAIWinCount() {
        return mAIWinCount;
    }

    public int getTieCount() {
        return mTieCount;
    }

    public void setHumanWinCount(int count) {
        mHumanWinCount = count;
    }

    public void setAIWinCount(int count) {
        mAIWinCount = count;
    }

    public void setTieCount(int count) {
        mTieCount = count;
    }

    public void addHumanWinCount() {
        mHumanWinCount++;
    }

    public void addAIWinCount() {
        mAIWinCount++;
    }

    public void addTieCount() {
        mTieCount++;
    }

    public boolean areYouFirstPlayer() {
        return mAreYouFirst;
    }

    public void changeFirstPlayer() {
        mAreYouFirst = !mAreYouFirst;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }
}

