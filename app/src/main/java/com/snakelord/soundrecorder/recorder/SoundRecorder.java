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

    private MediaRecorder recorder;
    private WorkingFolder recordsPath;
    private Context context;
    private String recordName;
    private boolean recordStarted = false;
    private static final String DATE_PATTERN = "dd-M-yyyy hh:mm:ss";
    private static final String RECORD_NAME = "/Record_";
    private static final String RECORD_FORMAT = ".ogg";
    private static final int RECORD_BIT_RATE = 128000;
    private static final int RECORD_SAMPLING_RATE = 44100;

    public SoundRecorder(Context context) {
        this.context = context;
        recordsPath = new WorkingFolder(context);
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioEncodingBitRate(RECORD_BIT_RATE);
        recorder.setAudioSamplingRate(RECORD_SAMPLING_RATE);
        recordName = generateRecordName();
        recorder.setOutputFile(recordName);
        try {
            recorder.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateRecordName() {
        return recordsPath.getFolder() + RECORD_NAME + new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(new Date()) + RECORD_FORMAT;
    }

    public boolean startRecording() {
        if (!isRecordStarted()) {
            if (recorder == null) initRecorder();
            try {
                recorder.start();
                recordStarted = true;
            } catch (IllegalStateException e) {
                showErrorToast();
                WorkWithFiles.removeRecord(recordName);
                recordStarted = false;
            }
        }
        return isRecordStarted();
    }

    public void stopRecording() {
        if (isRecordStarted()) {
            recorder.stop();
            recordStarted = false;
        }
    }

    public boolean isRecordStarted() { return recordStarted; }

    private void showErrorToast() {
        Toast.makeText(context, R.string.illegal_exception_when_mic_used,Toast.LENGTH_SHORT).show();
    }

    public void releaseRecorder() {
        recorder.reset();
        recorder.release();
        recorder = null;
    }
}
