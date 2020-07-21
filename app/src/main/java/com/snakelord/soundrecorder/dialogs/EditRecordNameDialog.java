package com.snakelord.soundrecorder.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.snakelord.soundrecorder.R;
import com.snakelord.soundrecorder.interfaces.DialogCallback;
import com.snakelord.soundrecorder.workingFolder.WorkWithFiles;

public final class EditRecordNameDialog extends DialogFragment {

    private EditText recordNameEditText;
    private Button acceptChangesButton;
    private Button declineChangesButton;
    private DialogCallback dialogCallback;
    private static final String RECORD_PATH = "Record path";

    static EditRecordNameDialog newInstance(String path) {
        EditRecordNameDialog dialog = new EditRecordNameDialog();
        Bundle args = new Bundle();
        args.putString(RECORD_PATH, path);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_change_record_name, container, false);
        recordNameEditText = dialogView.findViewById(R.id.record_name_editor);
        acceptChangesButton = dialogView.findViewById(R.id.accept_change);
        declineChangesButton = dialogView.findViewById(R.id.decline_change);
        setRecordNameEditText();
        setButtonsOnClickListener(getArguments().getString(RECORD_PATH));
        return dialogView;
    }

    private void setRecordNameEditText() {
        recordNameEditText.setText(WorkWithFiles.getFileName(getArguments().getString(RECORD_PATH)));
    }

    void setDialogCallback(DialogCallback mDialogCallback) {
        this.dialogCallback = mDialogCallback;
    }

    private void setButtonsOnClickListener(String path) {
        acceptChangesButton.setOnClickListener(v -> {
            if (!recordNameEditText.getText().toString().isEmpty()) {
                WorkWithFiles.renameRecord(getContext(), path, recordNameEditText.getText().toString());
                dialogCallback.updateRecyclerView();
                dismiss();
            }
        });
        declineChangesButton.setOnClickListener(v -> dismiss());
    }
}
