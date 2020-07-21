package com.snakelord.soundrecorder.recorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.widget.Toast;
import com.snakelord.soundrecorder.R;
import com.snakelord.soundrecorder.workingFolder.WorkWithFiles;
import com.snakelord.soundrecorder.workingFolder.WorkingFolder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SoundRecorder {

    private MediaRecorder mRecorder;
    private WorkingFolder mRecordsPath;
    private Context mContext;
    private String mRecordName;
    private boolean mRecordStarted = false;
    private static final String DATE_PATTERN = "dd-M-yyyy hh:mm:ss";
    private static final String RECORD_NAME = "/Record_";
    private static final String RECORD_FORMAT = ".ogg";
    private static final int RECORD_BIT_RATE = 128000;
    private static final int RECORD_SAMPLING_RATE = 44100;

    public SoundRecorder(Context context) {
        this.mContext = context;
        mRecordsPath = new WorkingFolder(context);
    }

    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioEncodingBitRate(RECORD_BIT_RATE);
        mRecorder.setAudioSamplingRate(RECORD_SAMPLING_RATE);
        mRecordName = generateRecordName();
        mRecorder.setOutputFile(mRecordName);
        try {
            mRecorder.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateRecordName() {
        return mRecordsPath.getFolder() + RECORD_NAME + new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(new Date()) + RECORD_FORMAT;
    }

    public boolean startRecording() {
        if (!isRecordStarted()) {
            if (mRecorder == null) initRecorder();
            try {
                mRecorder.start();
                mRecordStarted = true;
            } catch (IllegalStateException e) {
                showErrorToast();
                WorkWithFiles.removeRecord(mRecordName);
                mRecordStarted = false;
            }
        }
        return isRecordStarted();
    }

    public void stopRecording() {
        if (isRecordStarted()) {
            mRecorder.stop();
            mRecordStarted = false;
        }
    }

    public boolean isRecordStarted() { return mRecordStarted; }

    private void showErrorToast() {
        Toast.makeText(mContext, R.string.illegal_exception_when_mic_used,Toast.LENGTH_SHORT).show();
    }

    public void releaseRecorder() {
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }
}
