package com.snakelord.soundrecorder;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.snakelord.soundrecorder.recorder.SoundRecorder;

public final class RecordFragment extends Fragment {

    private ImageButton startRecordImageButton;
    private ImageButton stopRecordImageButton;
    private Chronometer chronometer;
    private SoundRecorder soundRecorder;
    private static final String CHRONOMETER_STATE = "Chronometer state";
    private static final String IS_RECORD_STARTED = "Record state";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreChronometer(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recordScreen = inflater.inflate(R.layout.fragment_record_screen, container, false);
        chronometer = recordScreen.findViewById(R.id.chronometer);
        startRecordImageButton = recordScreen.findViewById(R.id.start_recording);
        stopRecordImageButton = recordScreen.findViewById(R.id.stop_recording);
        setButtonsClickListener();
        return recordScreen;
    }

    private void initRecorder() { if (soundRecorder == null) soundRecorder = new SoundRecorder(getContext()); }

    private void setButtonsClickListener() {
        startRecordImageButton.setOnClickListener(v -> startRecord());
        stopRecordImageButton.setOnClickListener(v -> stopRecord());
    }

    private void restoreChronometer(Bundle savedState) {
        if (savedState != null) {
            if (savedState.getBoolean(IS_RECORD_STARTED)) {
                chronometer.setBase(savedState.getLong(CHRONOMETER_STATE));
                chronometer.start();
                startRecordImageButton.setEnabled(false);
            }
        }
    }

    private void startRecord() {
        initRecorder();
        if (soundRecorder.startRecording()) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            startRecordImageButton.setClickable(false);
        }
    }

    private void stopRecord() {
        if (isSoundRecorderCreated()) {
            soundRecorder.stopRecording();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            startRecordImageButton.setClickable(true);
        }
    }

    private boolean isSoundRecorderCreated() { return soundRecorder != null; }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CHRONOMETER_STATE, chronometer.getBase());
        if (soundRecorder != null) outState.putBoolean(IS_RECORD_STARTED, soundRecorder.isRecordStarted());
    }

    @Override
    public void onDestroy() {
        if (isSoundRecorderCreated()) soundRecorder.releaseRecorder();
        super.onDestroy();
    }
}
