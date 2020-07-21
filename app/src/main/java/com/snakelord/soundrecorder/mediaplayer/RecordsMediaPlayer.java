package com.snakelord.soundrecorder.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordsMediaPlayer {

    private MediaPlayer player;
    private File record;
    private FileInputStream fileInputStream;
    private boolean mPlayerStarted = false;

    public RecordsMediaPlayer(String recordPath)  {
        record = new File(recordPath);
    }

    private void prepareRecord() {
        try {
            fileInputStream = new FileInputStream(record);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileInputStream.getFD());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayer() {
        isPlayerCreated();
        player.start();
        mPlayerStarted = true;
    }

    public void pausePlayer() {
        player.pause();
        mPlayerStarted = false;
    }

    public void stopPlayer() {
        if (player != null) player.stop();
    }

    private void isPlayerCreated() {
        if (player == null) {
            prepareRecord();
            initPlayer();
        }
    }

    public void releasePlayer() {
        if (player != null) {
            if (isPlayerStarted()) pausePlayer();
            player.reset();
            player.release();
        }
    }

    public boolean isPlayerStarted() { return mPlayerStarted; }
}
