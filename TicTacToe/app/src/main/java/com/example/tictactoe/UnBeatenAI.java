package com.example.tictactoe;

import java.util.Random;

import static com.example.tictactoe.TicTacToeGame.COMPUTER_PLAYER;
import static com.example.tictactoe.TicTacToeGame.HUMAN_PLAYER;

public class UnBeatenAI {

    private static char[] mCurrentStep = new char[9];

    public static int getUnBeatenStep(char[] currentStep) {
        int nextStep = -1;

        int stepCount = 0;
        for (int i = 0; i < mCurrentStep.length; i++) {
            mCurrentStep[i] = currentStep[i];
            if (mCurrentStep[i] == HUMAN_PLAYER || mCurrentStep[i] == COMPUTER_PLAYER) {
                stepCount++;
            }
        }

        switch (stepCount) {
            case 0:
                nextStep = new Random().nextInt(9);
                break;
            case 1:
                nextStep = stepSecondOrThird(true);
                break;
            case 2:
                nextStep = stepSecondOrThird(false);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                if (mCurrentStep[4] != HUMAN_PLAYER && mCurrentStep[4] != COMPUTER_PLAYER) {
                    nextStep = 4;
                } else if ((mCurrentStep[0] != HUMAN_PLAYER && mCurrentStep[0] != COMPUTER_PLAYER)
                        || (mCurrentStep[2] != HUMAN_PLAYER && mCurrentStep[2] != COMPUTER_PLAYER)
                        || (mCurrentStep[6] != HUMAN_PLAYER && mCurrentStep[6] != COMPUTER_PLAYER)
                        || (mCurrentStep[8] != HUMAN_PLAYER && mCurrentStep[8] != COMPUTER_PLAYER)) {
                    int[] indexes = new int[]{0, 2, 6, 8};
                    do {
                        nextStep = indexes[new Random().nextInt(4)];
                    } while (mCurrentStep[nextStep] == HUMAN_PLAYER || mCurrentStep[nextStep] == COMPUTER_PLAYER);
                } else {
                    do {
                        nextStep = new Random().nextInt(9);
                    } while (mCurrentStep[nextStep] == HUMAN_PLAYER || mCurrentStep[nextStep] == COMPUTER_PLAYER);
                }
                break;
        }

        return nextStep;
    }

    private static int stepSecondOrThird(boolean isSecond) {
        int nextStep = -1;
        if (mCurrentStep[4] == HUMAN_PLAYER) {
            int[] indexes = {0, 2, 6, 8};
            boolean isSafe = false;
            for (int index : indexes) {
                if (mCurrentStep[index] == COMPUTER_PLAYER) {
                    isSafe = true;
                    break;
                }
            }
            if (!isSafe) {
                nextStep = indexes[new Random().nextInt(4)];
            } else {
                do {
                    nextStep = new Random().nextInt(9);
                } while (mCurrentStep[nextStep] == HUMAN_PLAYER || mCurrentStep[nextStep] == COMPUTER_PLAYER);
            }
        }
        if (mCurrentStep[0] == HUMAN_PLAYER || mCurrentStep[2] == HUMAN_PLAYER
                || mCurrentStep[6] == HUMAN_PLAYER || mCurrentStep[8] == HUMAN_PLAYER) {
            if (mCurrentStep[4] != COMPUTER_PLAYER) {
                nextStep = 4;
            } else {
                do {
                    nextStep = new Random().nextInt(9);
                } while (mCurrentStep[nextStep] == HUMAN_PLAYER || mCurrentStep[nextStep] == COMPUTER_PLAYER);
            }
        }
        if (mCurrentStep[1] == HUMAN_PLAYER || mCurrentStep[3] == HUMAN_PLAYER
                || mCurrentStep[5] == HUMAN_PLAYER || mCurrentStep[7] == HUMAN_PLAYER) {
            int[] indexes;
            if (mCurrentStep[1] == HUMAN_PLAYER) {
                indexes = new int[]{0, 2, 4, 7};
            } else if (mCurrentStep[3] == HUMAN_PLAYER) {
                indexes = new int[]{0, 4, 5, 6};
            } else if (mCurrentStep[5] == HUMAN_PLAYER) {
                indexes = new int[]{2, 3, 4, 8};
            } else {
                indexes = new int[]{1, 4, 6, 8};
            }
            boolean isSafe = false;
            for (int index : indexes) {
                if (mCurrentStep[index] == COMPUTER_PLAYER) {
                    isSafe = true;
                    break;
                }
            }
            if (!isSafe) {
                nextStep = indexes[new Random().nextInt(4)];
            } else {
                do {
                    nextStep = new Random().nextInt(9);
                } while (mCurrentStep[nextStep] == HUMAN_PLAYER || mCurrentStep[nextStep] == COMPUTER_PLAYER);
            }
        }
        return nextStep;
    }
}
