package com.snakelord.incode_soundrecorder.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.snakelord.incode_soundrecorder.R;
import com.snakelord.incode_soundrecorder.mediaplayer.RecordsMediaPlayer;
import com.snakelord.incode_soundrecorder.workingFolder.WorkWithFiles;

public final class BottomSheetPlayer extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mRecordNameTextView;
    private ImageButton mStartPlayingImageButton;
    private ImageButton mStopPlayingImageButton;
    private RecordsMediaPlayer mRecordsPlayer;
    private static final String RECORD_PATH = "Record path";

    public static BottomSheetPlayer newInstance(String path) {
       BottomSheetPlayer bottomSheet = new BottomSheetPlayer();
       Bundle args = new Bundle();
       args.putString(RECORD_PATH, path);
       bottomSheet.setArguments(args);
       return bottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View bottomSheet = View.inflate(getContext(), R.layout.bottom_sheet_record_mediaplayer, null);
        mRecordNameTextView = bottomSheet.findViewById(R.id.record_name_player);
        mStartPlayingImageButton = bottomSheet.findViewById(R.id.start_record);
        mStopPlayingImageButton = bottomSheet.findViewById(R.id.stop_player);
        setRecordName();
        initRecordsPlayer();
        setImageButtonsOnClickListener();
        bottomSheetDialog.setContentView(bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheet.getParent());
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initRecordsPlayer() {
        mRecordsPlayer = new RecordsMediaPlayer(getArguments().getString(RECORD_PATH));
    }

    private void setRecordName() {
        mRecordNameTextView.setText(WorkWithFiles.getFileName(getArguments().getString(RECORD_PATH)));
    }

    private void setImageButtonsOnClickListener() {
        mStartPlayingImageButton.setOnClickListener(v -> {
            if (!mRecordsPlayer.isPlayerStarted())
                startPlayer();
            else
                pausePlayer();
        });
        mStopPlayingImageButton.setOnClickListener(v -> {
            mRecordsPlayer.stopPlayer();
            dismiss();
        });
    }

    private void startPlayer() {
        mStartPlayingImageButton.setImageResource(R.drawable.ic_pause_red_24dp);
        mRecordsPlayer.startPlayer();
    }

    private void pausePlayer() {
        mStartPlayingImageButton.setImageResource(R.drawable.ic_play_arrow_red_24dp);
        mRecordsPlayer.pausePlayer();
    }

    @Override
    public void onDestroy() {
        mRecordsPlayer.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        if (mRecordsPlayer != null)
            mRecordsPlayer.releasePlayer();
        super.dismiss();
    }
}
