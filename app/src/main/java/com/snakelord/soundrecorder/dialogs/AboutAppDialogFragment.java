package com.snakelord.soundrecorder.dialogs;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.snakelord.soundrecorder.R;

public final class AboutAppDialogFragment extends DialogFragment {

    private TextView mAppVersionTextView;
    private static final String APP_VERSION = "Version : %s";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_about_app, container, false);
        mAppVersionTextView = dialogView.findViewById(R.id.app_version);
        setAppVersionTex();
        return dialogView;
    }

    private void setAppVersionTex() {
        mAppVersionTextView.setText(String.format(APP_VERSION, getVersionName()));
    }

    private String getVersionName() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }
}
