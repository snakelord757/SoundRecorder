package com.snakelord.soundrecorder.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordsMediaPlayer {

    private MediaPlayer mPlayer;
    private File mRecord;
    private FileInputStream mFileInputStream;
    private boolean mPlayerStarted = false;

    public RecordsMediaPlayer(String recordPath)  {
        mRecord = new File(recordPath);
    }

    private void prepareRecord() {
        try {
            mFileInputStream = new FileInputStream(mRecord);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileInputStream.getFD());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayer() {
        isPlayerCreated();
        mPlayer.start();
        mPlayerStarted = true;
    }

    public void pausePlayer() {
        mPlayer.pause();
        mPlayerStarted = false;
    }

    public void stopPlayer() {
        if (mPlayer != null) mPlayer.stop();
    }

    private void isPlayerCreated() {
        if (mPlayer == null) {
            prepareRecord();
            initPlayer();
        }
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            if (isPlayerStarted()) pausePlayer();
            mPlayer.reset();
            mPlayer.release();
        }
    }

    public boolean isPlayerStarted() { return mPlayerStarted; }
}
