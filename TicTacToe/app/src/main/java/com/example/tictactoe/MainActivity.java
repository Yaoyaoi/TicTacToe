package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static com.example.tictactoe.TicTacToeGame.ARE_YOU_FIRST;
import static com.example.tictactoe.TicTacToeGame.COMPUTER_PLAYER;
import static com.example.tictactoe.TicTacToeGame.GAME_STATUS;
import static com.example.tictactoe.TicTacToeGame.HUMAN_PLAYER;
import static com.example.tictactoe.TicTacToeGame.LEVEL;
import static com.example.tictactoe.TicTacToeGame.LEVEL_1;
import static com.example.tictactoe.TicTacToeGame.LEVEL_2;
import static com.example.tictactoe.TicTacToeGame.LEVEL_3;
import static com.example.tictactoe.TicTacToeGame.WIN_COUNT;

public class MainActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private VideoView videoView;
    private TextView mRecordTextView;
    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView;
    // Restart Button
    private Button startButton;
    // Game Over
    Boolean mGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        mBoardButtons = new Button[mGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.button0);
        mBoardButtons[1] = (Button) findViewById(R.id.button1);
        mBoardButtons[2] = (Button) findViewById(R.id.button2);
        mBoardButtons[3] = (Button) findViewById(R.id.button3);
        mBoardButtons[4] = (Button) findViewById(R.id.button4);
        mBoardButtons[5] = (Button) findViewById(R.id.button5);
        mBoardButtons[6] = (Button) findViewById(R.id.button6);
        mBoardButtons[7] = (Button) findViewById(R.id.button7);
        mBoardButtons[8] = (Button) findViewById(R.id.button8);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mRecordTextView = findViewById(R.id.winRecord);
        mGame = new TicTacToeGame();
        recoveryView(savedInstanceState);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);
    }

    // 屏幕转向时保存数据
    private void recoveryView(Bundle savedInstanceState) {
        char[] status = null;
        int[] winCount = null;
        boolean youFirst = true;
        int level = 1;

        if (savedInstanceState != null) {
            status = savedInstanceState.getCharArray(GAME_STATUS);
            winCount = savedInstanceState.getIntArray(WIN_COUNT);
            youFirst = savedInstanceState.getBoolean(ARE_YOU_FIRST);
            level = savedInstanceState.getInt(LEVEL);
        }
        if (status != null) {
            for (int i = 0; i < status.length; i++) {
                if (status[i] == HUMAN_PLAYER) {
                    setMove(HUMAN_PLAYER, i);
                } else if (status[i] == COMPUTER_PLAYER) {
                    setMove(COMPUTER_PLAYER, i);
                } else {
                    mBoardButtons[i].setText("");
                    mBoardButtons[i].setEnabled(true);
                    mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
                }
            }
            checkWinner(false);
            if (winCount != null && winCount.length >= 3) {
                mGame.setHumanWinCount(winCount[0]);
                mGame.setAIWinCount(winCount[1]);
                mGame.setTieCount(winCount[2]);
            }
            showWinCount();
            if (mGame.areYouFirstPlayer() != youFirst) {
                mGame.changeFirstPlayer();
            }
            mGame.setLevel(level);
        } else {
            startNewGame();
        }
    }

    public void newGame(View v) {
        videoView.stopPlayback();
        videoView.setVisibility(View.INVISIBLE);
        startNewGame();
    }
    private void playVideo() {
        //show the videoView
        videoView.setVisibility(View.VISIBLE);
        //load video
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //play
        videoView.start();
        //when finish the video set the videoView invisible.
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        char[] status = mGame.getCurrentStatus();
        outState.putCharArray(GAME_STATUS, status);
        int[] winCount = new int[]{mGame.getHumanWinCount(), mGame.getAIWinCount(), mGame.getTieCount()};
        outState.putIntArray(WIN_COUNT, winCount);
        outState.putBoolean(ARE_YOU_FIRST, mGame.areYouFirstPlayer());
        outState.putInt(LEVEL, mGame.getLevel());
    }


    //--- Set up the game board.
    private void startNewGame() {
        mGameOver = false;
        mGame.clearBoard();
        //---Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        if (mGame.areYouFirstPlayer()) {
            //---Human goes first
            mInfoTextView.setText(R.string.youGoFirst);
            mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
        } else {
            mInfoTextView.setText(R.string.androidTurn);
            mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }
    }

    private void checkWinner(boolean isMove) {
        //--- If no winner yet, let the computer make a move
        int winner = mGame.checkForWinner();
        if (winner == 0 && isMove) {
            mInfoTextView.setText(R.string.androidTurn);
            int move = mGame.getComputerMove();
            setMove(COMPUTER_PLAYER, move);
            winner = mGame.checkForWinner();
        }
        if (winner == 0) {
            mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
            mInfoTextView.setText(R.string.yourTurn);
            mGameOver = false;
        } else if (winner == 1) {
            mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
            mInfoTextView.setText(R.string.tie);
            mGameOver = true;
            mGame.addTieCount();
        } else if (winner == 2) {
            mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
            mInfoTextView.setText(R.string.youWon);
            mGameOver = true;
            mGame.addHumanWinCount();
            playVideo();
        } else {
            mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
            mInfoTextView.setText(R.string.androidWon);
            mGameOver = true;
            mGame.addAIWinCount();
        }
        if (isMove && winner!=0){
            mGame.changeFirstPlayer();
        }
    }

    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View v) {
            if (!mGameOver) {
                if (mBoardButtons[location].isEnabled()) {
                    setMove(HUMAN_PLAYER, location);
                    checkWinner(true);
                    showWinCount();
                }
            }
        }

        private void setMove(char player, int location) {
            mGame.setMove(player, location);
            mBoardButtons[location].setEnabled(false);
            mBoardButtons[location].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            else mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
        }
    }

    private void showWinCount() {
        mRecordTextView.setText(getString(R.string.winRecordDetail, mGame.getHumanWinCount(), mGame.getAIWinCount(), mGame.getTieCount()));
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    //按钮--目前只有离开功能
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the aaction bar if it is present
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_exit:
                finish();
                return true;
            case R.id.level1:
                mGame.setLevel(LEVEL_1);
                Toast.makeText(this, R.string.level1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.level2:
                mGame.setLevel(LEVEL_2);
                Toast.makeText(this, R.string.level2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.level3:
                mGame.setLevel(LEVEL_3);
                Toast.makeText(this, R.string.level3, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

