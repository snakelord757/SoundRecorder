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

    private EditText reportTitleEditText;
    private EditText reportDescriptionEditText;
    private Button sendReportButton;
    private String mailSubject;
    private String mailBody;
    private static final String DEV_MAIL = "blackkiller1527@gmail.com";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_with_dev);
        reportTitleEditText = findViewById(R.id.report_title);
        reportDescriptionEditText = findViewById(R.id.report_description);
        sendReportButton = findViewById(R.id.send_report);
        setSendReportButtonClickListener();
    }

    private void setSendReportButtonClickListener() { sendReportButton.setOnClickListener(v -> sendReport()); }

    private void sendReport() {
        if (isReportEmpty()) {
            startActivity(generateIntent());
            finish();
        }
        else
            Toast.makeText(this, R.string.empty_report, Toast.LENGTH_SHORT).show();
    }

    private boolean isReportEmpty() { return !reportDescriptionEditText.getText().toString().equals(""); }

    private void prepareReportText() {
        mailSubject = reportTitleEditText.getText().toString();
        mailBody = reportDescriptionEditText.getText().toString();
    }

    private Intent generateIntent() {
        prepareReportText();
        return EmailIntentBuilder.from(this).to(DEV_MAIL).subject(mailSubject).body(mailBody).build();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = reportTitleEditText.getText().toString();
        String description = reportDescriptionEditText.getText().toString();
        outState.putString(TITLE, title);
        outState.putString(DESCRIPTION, description);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        reportTitleEditText.setText(savedInstanceState.getString(TITLE));
        reportDescriptionEditText.setText(savedInstanceState.getString(DESCRIPTION));
    }
}
