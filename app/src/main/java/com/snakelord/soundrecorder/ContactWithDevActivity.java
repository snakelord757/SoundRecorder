package com.snakelord.soundrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.cketti.mailto.EmailIntentBuilder;

public class ContactWithDevActivity extends AppCompatActivity {

    private EditText mReportTitleEditText;
    private EditText mReportDescriptionEditText;
    private Button mSendReportButton;
    private String mMailSubject;
    private String mMailBody;
    private static final String DEV_MAIL = "blackkiller1527@gmail.com";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_with_dev);
        mReportTitleEditText = findViewById(R.id.report_title);
        mReportDescriptionEditText = findViewById(R.id.report_description);
        mSendReportButton = findViewById(R.id.send_report);
        setSendReportButtonClickListener();
    }

    private void setSendReportButtonClickListener() { mSendReportButton.setOnClickListener(v -> sendReport()); }

    private void sendReport() {
        if (isReportEmpty()) {
            startActivity(generateIntent());
            finish();
        }
        else
            Toast.makeText(this, R.string.empty_report, Toast.LENGTH_SHORT).show();
    }

    private boolean isReportEmpty() { return !mReportDescriptionEditText.getText().toString().equals(""); }

    private void prepareReportText() {
        mMailSubject = mReportTitleEditText.getText().toString();
        mMailBody = mReportDescriptionEditText.getText().toString();
    }

    private Intent generateIntent() {
        prepareReportText();
        return EmailIntentBuilder.from(this).to(DEV_MAIL).subject(mMailSubject).body(mMailBody).build();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = mReportTitleEditText.getText().toString();
        String description = mReportDescriptionEditText.getText().toString();
        outState.putString(TITLE, title);
        outState.putString(DESCRIPTION, description);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mReportTitleEditText.setText(savedInstanceState.getString(TITLE));
        mReportDescriptionEditText.setText(savedInstanceState.getString(DESCRIPTION));
    }
}
