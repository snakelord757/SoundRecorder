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

    private ImageButton mStartRecordImageButton;
    private ImageButton mStopRecordImageButton;
    private Chronometer mChronometer;
    private SoundRecorder mSoundRecorder;
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
        mChronometer = recordScreen.findViewById(R.id.chronometer);
        mStartRecordImageButton = recordScreen.findViewById(R.id.start_recording);
        mStopRecordImageButton = recordScreen.findViewById(R.id.stop_recording);
        setButtonsClickListener();
        return recordScreen;
    }

    private void initRecorder() { if (mSoundRecorder == null) mSoundRecorder = new SoundRecorder(getContext()); }

    private void setButtonsClickListener() {
        mStartRecordImageButton.setOnClickListener(v -> startRecord());
        mStopRecordImageButton.setOnClickListener(v -> stopRecord());
    }

    private void restoreChronometer(Bundle savedState) {
        if (savedState != null) {
            if (savedState.getBoolean(IS_RECORD_STARTED)) {
                mChronometer.setBase(savedState.getLong(CHRONOMETER_STATE));
                mChronometer.start();
                mStartRecordImageButton.setEnabled(false);
            }
        }
    }

    private void startRecord() {
        initRecorder();
        if (mSoundRecorder.startRecording()) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mStartRecordImageButton.setClickable(false);
        }
    }

    private void stopRecord() {
        if (isSoundRecorderCreated()) {
            mSoundRecorder.stopRecording();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.stop();
            mStartRecordImageButton.setClickable(true);
        }
    }

    private boolean isSoundRecorderCreated() { return mSoundRecorder != null; }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CHRONOMETER_STATE, mChronometer.getBase());
        if (mSoundRecorder != null) outState.putBoolean(IS_RECORD_STARTED, mSoundRecorder.isRecordStarted());
    }

    @Override
    public void onDestroy() {
        if (isSoundRecorderCreated()) mSoundRecorder.releaseRecorder();
        super.onDestroy();
    }
}
