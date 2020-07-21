package com.snakelord.soundrecorder.dialogs;

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
import com.snakelord.soundrecorder.R;
import com.snakelord.soundrecorder.mediaplayer.RecordsMediaPlayer;
import com.snakelord.soundrecorder.workingFolder.WorkWithFiles;

public final class BottomSheetPlayer extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView recordNameTextView;
    private ImageButton startPlayingImageButton;
    private ImageButton stopPlayingImageButton;
    private RecordsMediaPlayer recordsPlayer;
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
        recordNameTextView = bottomSheet.findViewById(R.id.record_name_player);
        startPlayingImageButton = bottomSheet.findViewById(R.id.start_record);
        stopPlayingImageButton = bottomSheet.findViewById(R.id.stop_player);
        setRecordName();
        initRecordsPlayer();
        setImageButtonsOnClickListener();
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheet.getParent());
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initRecordsPlayer() {
        recordsPlayer = new RecordsMediaPlayer(getArguments().getString(RECORD_PATH));
    }

    private void setRecordName() {
        recordNameTextView.setText(WorkWithFiles.getFileName(getArguments().getString(RECORD_PATH)));
    }

    private void setImageButtonsOnClickListener() {
        startPlayingImageButton.setOnClickListener(v -> {
            if (!recordsPlayer.isPlayerStarted())
                startPlayer();
            else
                pausePlayer();
        });
        stopPlayingImageButton.setOnClickListener(v -> {
            recordsPlayer.stopPlayer();
            dismiss();
        });
    }

    private void startPlayer() {
        startPlayingImageButton.setImageResource(R.drawable.ic_pause_red_24dp);
        recordsPlayer.startPlayer();
    }

    private void pausePlayer() {
        startPlayingImageButton.setImageResource(R.drawable.ic_play_arrow_red_24dp);
        recordsPlayer.pausePlayer();
    }

    @Override
    public void onDestroy() {
        recordsPlayer.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        if (recordsPlayer != null)
            recordsPlayer.releasePlayer();
        super.dismiss();
    }
}
