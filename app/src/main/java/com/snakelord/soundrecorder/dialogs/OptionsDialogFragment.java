package com.snakelord.soundrecorder.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.snakelord.soundrecorder.R;
import com.snakelord.soundrecorder.interfaces.DialogCallback;
import com.snakelord.soundrecorder.workingFolder.WorkWithFiles;

public final class OptionsDialogFragment extends DialogFragment  {

    private Button renameRecordButton;
    private Button shareRecordButton;
    private Button removeRecordButton;
    private DialogCallback dialogCallback;
    private static final String CURRENT_RECORD_PATH = "Current record path";

    public static OptionsDialogFragment newInstance(String recordPath) {
        OptionsDialogFragment dialogFragment = new OptionsDialogFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_RECORD_PATH, recordPath);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_record_options, container, false);
        renameRecordButton = dialogView.findViewById(R.id.rename_record_button);
        shareRecordButton = dialogView.findViewById(R.id.share_record_button);
        removeRecordButton = dialogView.findViewById(R.id.remove_record_button);
        setButtonsClickListener(getArguments().getString(CURRENT_RECORD_PATH));
        return dialogView;
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    private void setButtonsClickListener(String recordPath) {
        renameRecordButton.setOnClickListener(v -> {
            EditRecordNameDialog dialog = EditRecordNameDialog.newInstance(recordPath);
            dialog.setDialogCallback(dialogCallback);
            dialog.show(getFragmentManager(),null);
            dismiss();
        });
        shareRecordButton.setOnClickListener(v -> {
            WorkWithFiles.shareRecord(getActivity(), recordPath);
            dismiss();
            dialogCallback.updateRecyclerView();
        });
        removeRecordButton.setOnClickListener(v -> {
            WorkWithFiles.removeRecord(recordPath);
            dismiss();
            dialogCallback.updateRecyclerView();
        });
    }
}
